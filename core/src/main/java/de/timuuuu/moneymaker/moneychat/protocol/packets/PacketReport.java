package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import java.util.UUID;

public class PacketReport extends Packet {

  private UUID targetUUID;
  private String targetUsername;
  private UUID reporterUUID;
  private String reporterUsername;
  private String reason;
  private String reportedMessage;

  public PacketReport(UUID targetUUID, String targetUsername, UUID reporterUUID,
      String reporterUsername, String reason, String reportedMessage) {
    this.targetUUID = targetUUID;
    this.targetUsername = targetUsername;
    this.reporterUUID = reporterUUID;
    this.reporterUsername = reporterUsername;
    this.reason = reason;
    this.reportedMessage = reportedMessage;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.targetUUID = packetBuffer.readUUID();
    this.targetUsername = packetBuffer.readString();
    this.reporterUUID = packetBuffer.readUUID();
    this.reporterUsername = packetBuffer.readString();
    this.reason = packetBuffer.readString();
    this.reportedMessage = packetBuffer.readString();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.targetUUID);
    packetBuffer.writeString(this.targetUsername);
    packetBuffer.writeUUID(this.reporterUUID);
    packetBuffer.writeString(this.reporterUsername);
    packetBuffer.writeString(this.reason);
    packetBuffer.writeString(this.reportedMessage);
  }

  @Override
  public void handle(PacketHandler packetHandler) {}

}
