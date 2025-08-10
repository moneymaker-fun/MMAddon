package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class PacketReport extends MoneyPacket {

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

  public PacketReport() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.targetUUID = packetBuffer.readUUID();
    this.targetUsername = packetBuffer.readString();
    this.reporterUUID = packetBuffer.readUUID();
    this.reporterUsername = packetBuffer.readString();
    this.reason = packetBuffer.readString();
    this.reportedMessage = packetBuffer.readString();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.targetUUID);
    packetBuffer.writeString(this.targetUsername);
    packetBuffer.writeUUID(this.reporterUUID);
    packetBuffer.writeString(this.reporterUsername);
    packetBuffer.writeString(this.reason);
    packetBuffer.writeString(this.reportedMessage);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {}

}
