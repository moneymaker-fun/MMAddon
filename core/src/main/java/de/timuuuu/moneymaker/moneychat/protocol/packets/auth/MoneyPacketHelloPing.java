package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketHelloPing extends MoneyPacket {

  private long timestamp;

  public MoneyPacketHelloPing(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.timestamp = packetBuffer.readLong();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeLong(this.timestamp);
    packetBuffer.writeInt(MoneyChatClient.PROTOCOL_VERSION);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {}

  public long getTimestamp() {
    return timestamp;
  }

}
