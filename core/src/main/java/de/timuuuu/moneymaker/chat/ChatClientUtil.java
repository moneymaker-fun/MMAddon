package de.timuuuu.moneymaker.chat;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.icon.Icon;

public class ChatClientUtil {

  private MoneyMakerAddon addon;
  private ChatClient chatClient;

  public ChatClientUtil(MoneyMakerAddon addon, ChatClient chatClient) {
    this.addon = addon;
    this.chatClient = chatClient;
  }

  public void sendLeaderboard(String uuid, String userName) {
    JsonObject object = new JsonObject();
    object.addProperty("uuid", uuid);
    object.addProperty("userName", userName);
    object.addProperty("ranking", this.addon.addonUtil().ranking());
    object.addProperty("blocks", this.addon.addonUtil().brokenBlocks());
    object.addProperty("pickaxe_ranking", this.addon.addonUtil().pickaxeRanking());
    object.addProperty("sword_ranking", this.addon.addonUtil().swordRanking());
    object.addProperty("show_blocks", this.addon.addonUtil().leaderboardShowBlocks());
    this.chatClient.sendMessage("leaderboard", object);
  }

  public void sendPlayerStatus(String uuid, String userName, boolean offline) {
    JsonObject object = new JsonObject();
    object.addProperty("uuid", uuid);
    object.addProperty("userName", userName);
    object.addProperty("server", offline ? "OFFLINE" : this.chatClient.currentServer());
    object.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    object.addProperty("minecraftVersion", this.addon.labyAPI().minecraft().getVersion());
    object.addProperty("development", addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment());
    this.chatClient.sendMessage("playerStatus", object);
  }

  public void sendStatistics(boolean quit, String uuid, String userName) {
    JsonObject object = new JsonObject();
    if(!quit) {
      object.addProperty("data", "add");
      object.addProperty("userName", userName);
      object.addProperty("uuid", uuid);
      object.addProperty("addonVersion", addon.addonInfo().getVersion());
      object.addProperty("gameVersion", addon.labyAPI().minecraft().getVersion());
      object.addProperty("development", addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment());
    } else {
      object.addProperty("data", "remove");
      object.addProperty("userName", userName);
      object.addProperty("uuid", uuid);
    }
    this.chatClient.sendMessage("addonStatistics", object);
  }

  public enum MessageType {
    PLAYER(null, null, null),
    SERVER("00000000-0000-0000-0000-000000000000", Component.text("SYSTEM", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD), SpriteCommon.SERVER),
    DISCORD("00000000-0000-0000-0000-000000000001", Component.text("Discord", TextColor.color(88, 101, 242)).decorate(TextDecoration.BOLD), SpriteCommon.DISCORD);

    private final String uuid;
    private final Component userName;
    private final Icon icon;

    MessageType(String uuid, Component userName, Icon icon) {
      this.uuid = uuid;
      this.userName = userName;
      this.icon = icon;
    }

    public String uuid() {
      return uuid;
    }

    public Component userName() {
      return userName;
    }

    public Icon icon() {
      return icon;
    }
  }

  public static MessageType getMessageType(String uuid) {
    MessageType messageType = MessageType.PLAYER;
    for(MessageType type : MessageType.values()) {
      if(type.uuid() != null) {
        if(uuid.equals(type.uuid())) {
          messageType = type;
        }
      }
    }
    return messageType;
  }

}
