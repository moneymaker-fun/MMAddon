package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import de.timuuuu.moneymaker.moneychat.util.CryptManager;
import javax.crypto.SecretKey;
import java.security.PublicKey;

public class PacketEncryptionResponse extends Packet {

  private byte[] sharedSecret;
  private byte[] verifyToken;

  public PacketEncryptionResponse(SecretKey key, PublicKey publicKey, byte[] hash) {
    this.sharedSecret = CryptManager.encryptData(publicKey, key.getEncoded());
    this.verifyToken = CryptManager.encryptData(publicKey, hash);
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.sharedSecret = packetBuffer.readByteArray();
    this.verifyToken = packetBuffer.readByteArray();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeByteArray(this.sharedSecret);
    packetBuffer.writeByteArray(this.verifyToken);
  }

  @Override
  public void handle(PacketHandler packetHandler) {}

  public byte[] getSharedSecret() {
    return sharedSecret;
  }

  public byte[] getVerifyToken() {
    return verifyToken;
  }

}
