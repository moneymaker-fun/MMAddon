package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import java.util.UUID;

public class PacketAddonStatistics extends Packet {

  private String action;
  private UUID uuid;
  private String username;
  private String addonVersion;
  private String minecraftVersion;
  private boolean developmentEnvironment;

  public PacketAddonStatistics(String action, UUID uuid, String username, String addonVersion,
      String minecraftVersion, boolean developmentEnvironment) {
    this.action = action;
    this.uuid = uuid;
    this.username = username;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.developmentEnvironment = developmentEnvironment;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.action = packetBuffer.readString();
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    this.addonVersion = packetBuffer.readString();
    this.minecraftVersion = packetBuffer.readString();
    this.developmentEnvironment = packetBuffer.readBoolean();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(this.action);
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeString(this.addonVersion);
    packetBuffer.writeString(this.minecraftVersion);
    packetBuffer.writeBoolean(this.developmentEnvironment);
  }

  @Override
  public void handle(PacketHandler packetHandler) {}

}
