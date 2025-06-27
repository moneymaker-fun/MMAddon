package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketHelloPong extends Packet {

  private long timestamp;

  public PacketHelloPong(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.timestamp = packetBuffer.readLong();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeLong(this.timestamp);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public long getTimestamp() {
    return timestamp;
  }

}
