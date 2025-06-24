package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketDisconnect extends Packet {

  private String reason;

  public PacketDisconnect(String reason) {
    this.reason = reason;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.reason = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(reason);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String reason() {
    return reason;
  }

}
