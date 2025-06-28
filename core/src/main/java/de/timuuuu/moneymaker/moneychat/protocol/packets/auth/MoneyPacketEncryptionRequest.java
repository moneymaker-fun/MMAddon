package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketEncryptionRequest extends MoneyPacket {

  private String serverId;
  private byte[] publicKey;

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.serverId = packetBuffer.readString();
    this.publicKey = packetBuffer.readByteArray();
  }

  public MoneyPacketEncryptionRequest() {}

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String getServerId() {
    return serverId;
  }

  public byte[] getPublicKey() {
    return publicKey;
  }


}
