package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class PacketLeaderboard extends MoneyPacket {

  private UUID uuid;
  private String userName;
  private int raking;
  private int blocks;
  private int pickaxeRanking;
  private int swordRanking;
  private boolean showBlocks;
  private boolean developmentEnvironment;

  public PacketLeaderboard(UUID uuid, String userName, MoneyMakerAddon addon) {
    this.uuid = uuid;
    this.userName = userName;
    this.raking = addon.addonUtil().ranking();
    this.blocks = addon.addonUtil().currentBrokenBlocks();
    this.pickaxeRanking = addon.addonUtil().pickaxeRanking();
    this.swordRanking = addon.addonUtil().swordRanking();
    this.showBlocks = addon.addonUtil().leaderboardShowBlocks();
    this.developmentEnvironment = addon.labyAPI().labyModLoader().isDevelopmentEnvironment();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeUUID(this.uuid);
    packetBuffer.writeString(this.userName);
    packetBuffer.writeInt(this.raking);
    packetBuffer.writeInt(this.blocks);
    packetBuffer.writeInt(this.pickaxeRanking);
    packetBuffer.writeInt(this.swordRanking);
    packetBuffer.writeBoolean(this.showBlocks);
    packetBuffer.writeBoolean(this.developmentEnvironment);
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {}

  @Override
  public void handle(MoneyPacketHandler packetHandler) {}

}
