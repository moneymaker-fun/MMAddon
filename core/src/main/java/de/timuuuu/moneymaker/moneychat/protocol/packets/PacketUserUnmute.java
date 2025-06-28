package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class PacketUserUnmute extends MoneyPacket {

  private UUID uuid;
  private String username;
  private UUID targetUUID;
  private String targetUsername;

  public PacketUserUnmute(UUID uuid, String username, UUID targetUUID, String targetUsername) {
    this.uuid = uuid;
    this.username = username;
    this.targetUUID = targetUUID;
    this.targetUsername = targetUsername;
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    this.targetUUID = packetBuffer.readUUID();
    this.targetUsername = packetBuffer.readString();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeUUID(this.targetUUID);
    packetBuffer.writeString(this.targetUsername);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
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
