package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import java.util.UUID;

public class PacketUserUnmute extends Packet {

  private UUID uuid;
  private String username;
  private UUID targetUUID;
  private String targetUsername;

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    this.targetUUID = packetBuffer.readUUID();
    this.targetUsername = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeUUID(this.targetUUID);
    packetBuffer.writeString(this.targetUsername);
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

  public UUID targetUUID() {
    return targetUUID;
  }

  public String targetUsername() {
    return targetUsername;
  }
}
