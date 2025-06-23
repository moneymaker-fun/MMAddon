package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketUserRankUpdate extends Packet {

  private String uuid;
  private String rank;

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readString();
    this.rank = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(this.uuid);
    packetBuffer.writeString(this.rank);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String uuid() {
    return uuid;
  }

  public String rank() {
    return rank;
  }
}
