package de.timuuuu.moneymaker.utils;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import java.util.UUID;

public class MoneyPlayer {

  private UUID uuid;
  private String userName;
  private String server;
  private String addonVersion;
  private Rank rank;

  public MoneyPlayer(UUID uuid, String userName, String server, String addonVersion, Rank rank) {
    this.uuid = uuid;
    this.userName = userName;
    this.server = server;
    this.addonVersion = addonVersion;
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

  public Rank rank() {
    return rank;
  }

  public static Rank rankByName(String name) {
    Rank rank = Rank.USER;
    for(Rank ranks : Rank.values()) {
      if(ranks.getName().equalsIgnoreCase(name)) {
        rank = ranks;
      }
    }
    return rank;
  }

  public enum Rank {

    USER(4, "user", false, "§e", "§e", null),
    DONATOR(3, "donator", false, "§8[§6$§8] §6", "§6", Component.text(" Donator", TextColor.color(255, 170, 0))),
    STAFF(2, "staff", true, "§8[§cStaff§8] §c", "§c", Component.text(" Staff", TextColor.color(255, 85, 85))),
    DEVELOPER(1, "developer", true, "§8[§4Dev§8] §4", "§4", Component.text(" Dev", TextColor.color(170, 0, 0)));

    private final int id;
    private final String name;
    private final boolean staff;
    private final String chatPrefix;
    private final String onlineColor;
    private final Component nameTag;

    Rank(int id, String name, boolean staff, String chatPrefix, String onlineColor, Component nameTag) {
      this.id = id;
      this.name = name;
      this.staff = staff;
      this.chatPrefix = chatPrefix;
      this.onlineColor = onlineColor;
      this.nameTag = nameTag;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public boolean isStaff() {
      return staff;
    }

    public String getChatPrefix() {
      return chatPrefix;
    }

    public String getOnlineColor() {
      return onlineColor;
    }

    public Component getNameTag() {
      return nameTag;
    }

  }

}
