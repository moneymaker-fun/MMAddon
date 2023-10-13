package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.events.ChatServerMessageReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import java.util.UUID;

public class ChatServerListener {

  private MoneyMakerAddon addon;

  public ChatServerListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatServerMessageReceive(ChatServerMessageReceiveEvent event) {
    JsonObject message = event.message();

    if(message.has("chatMessage") && message.get("chatMessage").isJsonObject()) {
      MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(message.get("chatMessage").getAsJsonObject());
      Laby.fireEvent(new MoneyChatReceiveEvent(chatMessage));
    }

    if(message.has("playerStatus") && message.get("playerStatus").isJsonObject()) {
      JsonObject data = message.get("playerStatus").getAsJsonObject();
      UUID uuid = UUID.fromString(data.get("uuid").getAsString());
      Laby.fireEvent(new MoneyPlayerStatusEvent(
          uuid,
          new MoneyPlayer(uuid, data.get("userName").getAsString(), data.get("server").getAsString(), data.get("addonVersion").getAsString(), data.get("staffMember").getAsBoolean())
      ));
    }

    if(message.has("retrievedPlayerData")) {
      JsonObject data = message.get("retrievedPlayerData").getAsJsonObject();
      if(data.has("uuid") & data.has("players")) {
        if(Laby.labyAPI().getUniqueId().toString().equals(data.get("uuid").getAsString())) {
          if(data.get("players").isJsonArray()) {
            JsonArray array = data.get("players").getAsJsonArray();
            for(int i  = 0; i < array.size(); i++) {
              JsonObject playerData = array.get(i).getAsJsonObject();
              UUID uuid = UUID.fromString(playerData.get("uuid").getAsString());
              AddonSettings.playerStatus.put(uuid, new MoneyPlayer(
                  uuid,
                  playerData.get("userName").getAsString(),
                  playerData.get("server").getAsString(),
                  playerData.get("addonVersion").getAsString(),
                  playerData.get("staffMember").getAsBoolean()
              ));
            }
          }
        }
      }
    }

  }

}
