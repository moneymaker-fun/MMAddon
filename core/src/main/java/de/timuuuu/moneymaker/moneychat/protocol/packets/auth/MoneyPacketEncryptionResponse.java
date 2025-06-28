package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import de.timuuuu.moneymaker.moneychat.util.CryptManager;
import javax.crypto.SecretKey;
import java.security.PublicKey;

public class MoneyPacketEncryptionResponse extends MoneyPacket {

  private byte[] sharedSecret;
  private byte[] verifyToken;

  public MoneyPacketEncryptionResponse(SecretKey key, PublicKey publicKey, byte[] hash) {
    this.sharedSecret = CryptManager.encryptData(publicKey, key.getEncoded());
    this.verifyToken = CryptManager.encryptData(publicKey, hash);
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.sharedSecret = packetBuffer.readByteArray();
    this.verifyToken = packetBuffer.readByteArray();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeByteArray(this.sharedSecret);
    packetBuffer.writeByteArray(this.verifyToken);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {}

  public byte[] getSharedSecret() {
    return sharedSecret;
  }

  public byte[] getVerifyToken() {
    return verifyToken;
  }

}
