package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketPing extends Packet {

  @Override
  public void read(PacketBuffer packetBuffer) {}

  @Override
  public void write(PacketBuffer packetBuffer) {}

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

}
