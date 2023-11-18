package de.timuuuu.moneymaker.badges;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public enum MoneyRank {

  USER(4, "user", false, "§e", "§e", null, Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"))),
  DONATOR(3, "donator", false, "§8[§6$§8] §6", "§6", Component.text(" Donator", TextColor.color(255, 170, 0)), Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"))),
  STAFF(2, "staff", true, "§8[§cStaff§8] §c", "§c", Component.text(" Staff", TextColor.color(255, 85, 85)), Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"))),
  DEVELOPER(1, "developer", true, "§8[§4Dev§8] §4", "§4", Component.text(" Dev", TextColor.color(170, 0, 0)), Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png")));

  private final int id;
  private final String name;
  private final boolean staff;
  private final String chatPrefix;
  private final String onlineColor;
  private final Component nameTag;
  private final Icon icon;

  MoneyRank(int id, String name, boolean staff, String chatPrefix, String onlineColor, Component nameTag, Icon icon) {
    this.id = id;
    this.name = name;
    this.staff = staff;
    this.chatPrefix = chatPrefix;
    this.onlineColor = onlineColor;
    this.nameTag = nameTag;
    this.icon = icon;
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

  public Icon getIcon() {
    return icon;
  }
}
