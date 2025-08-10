package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class PacketUserRankUpdate extends MoneyPacket {

  private String uuid;
  private String rank;

  public PacketUserRankUpdate(String uuid, String rank) {
    this.uuid = uuid;
    this.rank = rank;
  }

  public PacketUserRankUpdate() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readString();
    this.rank = packetBuffer.readString();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeString(this.uuid);
    packetBuffer.writeString(this.rank);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String uuid() {
    return uuid;
  }

  public String rank() {
    return rank;
  }
}
