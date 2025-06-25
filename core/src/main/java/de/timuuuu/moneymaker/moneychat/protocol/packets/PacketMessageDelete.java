package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import java.util.UUID;

public class PacketMessageDelete extends Packet {

  private UUID uuid;
  private String username;
  private String messageId;

  public PacketMessageDelete(UUID uuid, String username, String messageId) {
    this.uuid = uuid;
    this.username = username;
    this.messageId = messageId;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    this.messageId = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeString(this.messageId);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public UUID uuid() {
    return uuid;
  }

  public String username() {
    return username;
  }

  public String messageId() {
    return messageId;
  }
}
