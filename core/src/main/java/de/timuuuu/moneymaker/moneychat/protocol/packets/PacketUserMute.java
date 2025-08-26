package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class PacketUserMute extends MoneyPacket {

  private UUID uuid;
  private String username;
  private UUID targetUUID;
  private String targetUsername;
  private String reason;
  private long duration;

  public PacketUserMute(UUID uuid, String username, UUID targetUUID, String targetUsername,
      String reason, long duration) {
    this.uuid = uuid;
    this.username = username;
    this.targetUUID = targetUUID;
    this.targetUsername = targetUsername;
    this.reason = reason;
    this.duration = duration;
  }

  public PacketUserMute() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    this.targetUUID = packetBuffer.readUUID();
    this.targetUsername = packetBuffer.readString();
    this.reason = packetBuffer.readString();
    this.duration = packetBuffer.readLong();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeUUID(this.targetUUID);
    packetBuffer.writeString(this.targetUsername);
    packetBuffer.writeString(this.reason);
    packetBuffer.writeLong(this.duration);
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

  public String reason() {
    return reason;
  }

  public long duration() {
    return duration;
  }
}
