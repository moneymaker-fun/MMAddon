package de.timuuuu.moneymaker.utils;

import java.util.UUID;

public class MoneyPlayer {

  private UUID uuid;
  private String userName;
  private String server;
  private String addonVersion;
  private boolean staff;

  public MoneyPlayer(UUID uuid, String userName, String server, String addonVersion, boolean staff) {
    this.uuid = uuid;
    this.userName = userName;
    this.server = server;
    this.addonVersion = addonVersion;
    this.staff = staff;
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

  public boolean staff() {
    return staff;
  }

}
