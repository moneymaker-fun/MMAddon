package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketEncryptionResponse extends MoneyPacket {

  private byte[] sharedSecret;

  public MoneyPacketEncryptionResponse(byte[] sharedSecret) {
    this.sharedSecret = sharedSecret;
  }

  public MoneyPacketEncryptionResponse() {}

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
