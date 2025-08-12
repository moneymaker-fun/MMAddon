package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.group.Group;
import java.util.UUID;

public class MoneyPlayer {

  private UUID uuid;
  private String userName;
  private String server;
  private String addonVersion;
  private String minecraftVersion;
  private Group group;
  private boolean hideOnlineStatus;

  public MoneyPlayer(UUID uuid, String userName, String server, String addonVersion, String minecraftVersion, Group group, boolean hideOnlineStatus) {
    this.uuid = uuid;
    this.userName = userName;
    this.server = server;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
    this.group = group;
    this.hideOnlineStatus = hideOnlineStatus;
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

  public Group group() {
    return group;
  }

  public void group(Group group) {
    this.group = group;
  }

  public boolean hideOnlineStatus() {
    return hideOnlineStatus;
  }

  public void hideOnlineStatus(boolean hideOnlineStatus) {
    this.hideOnlineStatus = hideOnlineStatus;
  }

}
