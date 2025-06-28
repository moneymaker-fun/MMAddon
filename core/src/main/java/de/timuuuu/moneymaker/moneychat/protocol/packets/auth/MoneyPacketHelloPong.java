package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketHelloPong extends MoneyPacket {

  private long timestamp;

  public MoneyPacketHelloPong(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.timestamp = packetBuffer.readLong();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeLong(this.timestamp);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public long getTimestamp() {
    return timestamp;
  }

}
