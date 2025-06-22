package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketMessageDelete extends Packet {

  private String uuid;
  private String username;
  private String messageId;

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readString();
    this.username = packetBuffer.readString();
    this.messageId = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeString(this.messageId);
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

  public String messageId() {
    return messageId;
  }
}
