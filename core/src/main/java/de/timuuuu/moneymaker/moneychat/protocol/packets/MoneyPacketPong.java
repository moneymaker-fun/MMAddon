package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketPong extends MoneyPacket {

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void handle(MoneyPacketHandler packetHandler) {}
}
