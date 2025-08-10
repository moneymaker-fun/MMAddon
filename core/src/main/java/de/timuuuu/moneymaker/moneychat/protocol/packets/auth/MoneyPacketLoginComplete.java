package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketLoginComplete extends MoneyPacket {

  public MoneyPacketLoginComplete() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

}
