package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.badges.MoneyRank;
import java.util.UUID;

public class MoneyPlayer {

  private UUID uuid;
  private String userName;
  private String server;
  private String addonVersion;
  private String minecraftVersion;
  private MoneyRank rank;

  public MoneyPlayer(UUID uuid, String userName, String server, String addonVersion, String minecraftVersion, MoneyRank rank) {
    this.uuid = uuid;
    this.userName = userName;
    this.server = server;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.rank = rank;
  }

  public UUID uuid() {
    return uuid;
  }

  public String userName() {
    return userName;
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

  public MoneyRank rank() {
    return rank;
  }

  public static MoneyRank rankByName(String name) {
    MoneyRank rank = MoneyRank.USER;
    for(MoneyRank ranks : MoneyRank.values()) {
      if(ranks.getName().equalsIgnoreCase(name)) {
        rank = ranks;
      }
    }
    return rank;
  }

}
