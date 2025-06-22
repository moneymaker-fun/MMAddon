package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketHelloPing extends Packet {

  private long timestamp;

  public PacketHelloPing(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.timestamp = packetBuffer.readLong();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeLong(this.timestamp);
    packetBuffer.writeInt(MoneyChatClient.PROTOCOL_VERSION);
  }

  @Override
  public void handle(PacketHandler packetHandler) {}

  public long getTimestamp() {
    return timestamp;
  }

}
