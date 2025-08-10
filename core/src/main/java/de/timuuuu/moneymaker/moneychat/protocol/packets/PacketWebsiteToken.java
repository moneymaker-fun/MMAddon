package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class PacketWebsiteToken extends MoneyPacket {

  private String token;

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.token = packetBuffer.readString();
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String token() {
    return token;
  }

}
