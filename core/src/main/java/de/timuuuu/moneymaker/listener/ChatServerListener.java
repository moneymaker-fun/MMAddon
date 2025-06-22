package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatClient;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.events.ChatServerMessageReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;

public class ChatServerListener {

  private MoneyMakerAddon addon;

  public ChatServerListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatServerMessageReceive(ChatServerMessageReceiveEvent event) {
    JsonObject message = event.message();

    if(message.has("playerRankChanged") && message.get("playerRankChanged").isJsonObject()) {
      JsonObject rankChange = message.get("playerRankChanged").getAsJsonObject();
      if(rankChange.has("uuid") && rankChange.has("rank")) {
        UUID uuid = UUID.fromString(rankChange.get("uuid").getAsString());
        MoneyRank rank = MoneyRank.byName(rankChange.get("rank").getAsString());
        if(AddonUtil.playerStatus.containsKey(uuid)) {
          AddonUtil.playerStatus.get(uuid).rank(rank);
        }
        if(this.addon.labyAPI().getUniqueId().equals(uuid)) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.rank-changed.title", NamedTextColor.GREEN),
              Component.translatable("moneymaker.notification.chat.rank-changed.text", NamedTextColor.YELLOW, Component.text(rank.getOnlineColor() + rank.getName())),
              rank.getIcon()
          );
        }
      }
    }

    // Netty checked
    if(message.has("chatMessage") && message.get("chatMessage").isJsonObject()) {
      MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(message.get("chatMessage").getAsJsonObject());
      Laby.fireEvent(new MoneyChatReceiveEvent(chatMessage));
    }

    if(message.has("chatAction") && message.get("chatAction").isJsonObject()) {
      JsonObject data = message.get("chatAction").getAsJsonObject();
      if(ChatClient.actionByName(data.get("action").getAsString()) != null) {
        switch (ChatClient.actionByName(data.get("action").getAsString())) {

          // Netty checked
          case CLEAR -> this.addon.chatActivity().clearChat(true);

          // Netty checked
          case DELETE_MESSAGE -> {
            if(data.has("data") && data.get("data").isJsonPrimitive()) {
              this.addon.chatActivity().deleteMessage(data.get("data").getAsString());
            }
          }

        }
      }
    }

    if(message.has("chatMute") && message.get("chatMute").isJsonObject()) {
      JsonObject data = message.get("chatMute").getAsJsonObject();
      if(data.has("uuid")) {
        String uuid = data.get("uuid").getAsString();
        if(this.addon.labyAPI().getUniqueId().toString().equals(uuid)) {
          this.addon.chatClient().muted(true);
          this.addon.chatClient().muteReason(data.get("reason").getAsString());
          this.addon.chatActivity().addCustomChatMessage(Component.text("Du wurdest aus dem Chat ausgeschlossen.", NamedTextColor.RED));
          this.addon.chatActivity().reloadScreen();
        }
      }
    }

    if(message.has("chatUnMute") && message.get("chatUnMute").isJsonObject()) {
      JsonObject data = message.get("chatUnMute").getAsJsonObject();
      if(data.has("uuid") && this.addon.chatClient().muted()) {
        String uuid = data.get("uuid").getAsString();
        if(this.addon.labyAPI().getUniqueId().toString().equals(uuid)) {
          this.addon.chatClient().muted(false);
          this.addon.chatClient().muteReason("");
          this.addon.chatActivity().addCustomChatMessage(Component.text("Dein Mute wurde aufgehoben.", NamedTextColor.GREEN));
          this.addon.chatActivity().reloadScreen();
        }
      }
    }

    if (message.has("muteInfo") && message.get("muteInfo").isJsonObject()) {
      JsonObject data = message.get("muteInfo").getAsJsonObject();
      if (data.has("muted") && data.has("reason")) {
        if (data.get("muted").getAsBoolean()) {
          this.addon.chatClient().muted(true);
          this.addon.chatClient().muteReason(data.get("reason").getAsString());
        } else {
          this.addon.chatClient().muted(false);
          this.addon.chatClient().muteReason("");
        }
        this.addon.chatActivity().reloadScreen();
      }
    }

    if(message.has("playerStatus") && message.get("playerStatus").isJsonObject()) {
      JsonObject data = message.get("playerStatus").getAsJsonObject();
      UUID uuid = UUID.fromString(data.get("uuid").getAsString());
      Laby.fireEvent(new MoneyPlayerStatusEvent(
          uuid,
          new MoneyPlayer(uuid,
              data.get("userName").getAsString(),
              data.get("server").getAsString(),
              data.get("addonVersion").getAsString(),
              data.has("minecraftVersion") ? data.get("minecraftVersion").getAsString() : "unknown",
              MoneyRank.byName(data.get("rank").getAsString())
          )
      ));
    }

    if(message.has("retrievedPlayerData")) {
      JsonObject data = message.get("retrievedPlayerData").getAsJsonObject();
      if(data.has("players")) {
        if (data.get("players").isJsonArray()) {
          JsonArray array = data.get("players").getAsJsonArray();
          for (int i = 0; i < array.size(); i++) {
            JsonObject playerData = array.get(i).getAsJsonObject();
            UUID uuid = UUID.fromString(playerData.get("uuid").getAsString());
            AddonUtil.playerStatus.put(uuid, new MoneyPlayer(
                uuid,
                playerData.get("userName").getAsString(),
                playerData.get("server").getAsString(),
                playerData.get("addonVersion").getAsString(),
                playerData.has("minecraftVersion") ? playerData.get("minecraftVersion").getAsString() : "unknown",
                MoneyRank.byName(playerData.get("rank").getAsString())
            ));
          }
        }
      }
    }

    if(message.has("clearChatForHistory")) {
      if(message.get("clearChatForHistory").getAsBoolean()) {
        this.addon.chatActivity().clearChat(false);
      }
    }

  }

}
