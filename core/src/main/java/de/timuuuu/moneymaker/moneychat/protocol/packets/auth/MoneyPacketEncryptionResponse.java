package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import de.timuuuu.moneymaker.moneychat.util.CryptManager;
import javax.crypto.SecretKey;
import java.security.PublicKey;

public class MoneyPacketEncryptionResponse extends MoneyPacket {

  private byte[] sharedSecret;

  public MoneyPacketEncryptionResponse(byte[] sharedSecret) {
    this.sharedSecret = sharedSecret;
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.sharedSecret = packetBuffer.readByteArray();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeByteArray(this.sharedSecret);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {}

  public byte[] getSharedSecret() {
    return sharedSecret;
  }

}
