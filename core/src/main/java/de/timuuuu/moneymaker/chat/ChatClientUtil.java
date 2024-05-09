package de.timuuuu.moneymaker.chat;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;

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

}
