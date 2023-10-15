package de.timuuuu.moneymaker.utils;

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

    USER("user", false),
    DONATOR("donator", false),
    STAFF("staff", true),
    DEVELOPER("developer", true);

    private final String name;
    private final boolean staff;

    Rank(String name, boolean staff) {
      this.name = name;
      this.staff = staff;
    }

    public String getName() {
      return name;
    }

    public boolean isStaff() {
      return staff;
    }
  }

}
