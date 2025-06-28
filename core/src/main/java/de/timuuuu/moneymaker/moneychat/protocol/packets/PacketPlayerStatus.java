package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class PacketPlayerStatus extends MoneyPacket {

  private UUID uuid;
  private String username;
  private MoneyRank rank;
  private String server;
  private String addonVersion;
  private String minecraftVersion;
  private boolean developmentEnvironment;

  public PacketPlayerStatus(UUID uuid, String username, MoneyRank rank, String server,
      String addonVersion, String minecraftVersion, boolean developmentEnvironment) {
    this.uuid = uuid;
    this.username = username;
    this.rank = rank;
    this.server = server;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.developmentEnvironment = developmentEnvironment;
  }

  public PacketPlayerStatus() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.uuid = packetBuffer.readUUID();
    this.username = packetBuffer.readString();
    String rankeName = packetBuffer.readString();
    this.rank = MoneyRank.byName(rankeName) != null ? MoneyRank.byName(rankeName) : MoneyRank.USER;
    this.server = packetBuffer.readString();
    this.addonVersion = packetBuffer.readString();
    this.minecraftVersion = packetBuffer.readString();
    this.developmentEnvironment = packetBuffer.readBoolean();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.username);
    packetBuffer.writeString(this.rank.getName());
    packetBuffer.writeString(this.server);
    packetBuffer.writeString(this.addonVersion);
    packetBuffer.writeString(this.minecraftVersion);
    packetBuffer.writeBoolean(this.developmentEnvironment);
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

  public MoneyRank rank() {
    return rank;
  }

  public String server() {
    return server;
  }

  public String addonVersion() {
    return addonVersion;
  }

  public String minecraftVersion() {
    return minecraftVersion;
  }

  public boolean developmentEnvironment() {
    return developmentEnvironment;
  }
}
