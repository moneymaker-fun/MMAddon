package de.timuuuu.moneymaker.enums;

import de.timuuuu.moneymaker.utils.MoneyTextures.Common;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;

public enum MoneyRank {

  //TODO: Add Icon for Translator | Change color of developer?
  ADMIN(1, "admin", true, "§8[§4Admin§8] §4", "§4", Component.text(" Admin", NamedTextColor.DARK_RED), Common.ICON_DARK_RED, "https://cdn.terramc.net/moneymaker/lore_red.png"),
  DEVELOPER(2, "developer", true, "§8[§4Dev§8] §4", "§4", Component.text(" Dev", NamedTextColor.DARK_RED), Common.ICON_DARK_RED, "https://cdn.terramc.net/moneymaker/lore_red.png"),
  STAFF(3, "staff", true, "§8[§cStaff§8] §c", "§c", Component.text(" Staff", NamedTextColor.RED), Common.ICON_RED, "https://cdn.terramc.net/moneymaker/lore_light_red.png"),
  TRANSLATOR(4, "translator", false, "§8[§2Translator§8] §2", "§2", Component.text(" Translator", NamedTextColor.DARK_GREEN), Common.ICON_RED, "https://cdn.terramc.net/moneymaker/lore_light_red.png"),
  DONATOR(5, "donator", false, "§8[§6$§8] §6", "§6", Component.text(" Donator", NamedTextColor.GOLD), Common.ICON_ORANGE, "https://cdn.terramc.net/moneymaker/lore_orange.png"),
  USER(6, "user", false, "§e", "§e", null, Common.ICON, "https://cdn.terramc.net/moneymaker/lore.png");

  private final int id;
  private final String name;
  private final boolean staff;
  private final String chatPrefix;
  private final String onlineColor;
  private final Component nameTag;
  private final Icon icon;
  private final String iconUrl;

  MoneyRank(int id, String name, boolean staff, String chatPrefix, String onlineColor, Component nameTag, Icon icon, String iconUrl) {
    this.id = id;
    this.name = name;
    this.staff = staff;
    this.chatPrefix = chatPrefix;
    this.onlineColor = onlineColor;
    this.nameTag = nameTag;
    this.icon = icon;
    this.iconUrl = iconUrl;
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

  public String getIconUrl() {
    return iconUrl;
  }
}
