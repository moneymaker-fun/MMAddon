package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketClearChat extends Packet {

  private String uuid;
  private String username;

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readString();
    this.username = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(uuid);
    packetBuffer.writeString(username);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String uuid() {
    return uuid;
  }

  public String username() {
    return username;
  }

}
