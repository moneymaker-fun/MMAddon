package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import java.util.UUID;
import de.timuuuu.moneymaker.utils.Booster;
import de.timuuuu.moneymaker.utils.ChatClient;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.util.io.web.request.WebResolver;
import net.labymod.serverapi.protocol.payload.exception.PayloadReaderException;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;

public class NetworkPayloadListener {

  private final MoneyMakerAddon addon;

  public NetworkPayloadListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private boolean switched = false;

  @Subscribe
  public void onServerLogin(ServerLoginEvent event) {
    if(event.serverData().actualAddress().matches("gommehd.net", 25565, true) ||
        event.serverData().actualAddress().matches("moneymaker.gg", 25565, true)) {
      AddonSettings.gommeConnected = true;

      JsonObject object = new JsonObject();
      object.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
      ChatClient.sendMessage("retrievePlayerData", object);

    }
  }

  @Subscribe
  public void onSwitch(SubServerSwitchEvent event) {
    switched = true;
  }

  @Subscribe
  public void onNetworkPayload(NetworkPayloadEvent event) {
    if(event.identifier().getNamespace().equals("labymod3") & event.identifier().getPath().equals("main")) {
      try {
        if(!AddonSettings.gommeConnected) return;
        PayloadReader reader = new PayloadReader(event.getPayload());
        String messageKey = reader.readString();
        String messageContent = reader.readString();

        JsonElement serverMessage = WebResolver.GSON.fromJson(messageContent, JsonElement.class);

        if(serverMessage.isJsonObject()) {

          JsonObject obj = serverMessage.getAsJsonObject();


          if(messageKey.equals("discord_rpc")) {

            if (obj.has("hasGame")) {
              String gameMode = obj.get("game_mode").getAsString();

              if(AddonSettings.playingOn.contains("Farming") && gameMode.contains("Mine")) {
                if(AddonSettings.sessionBlocks > 0) {
                  MoneyMakerAddon.pushNotification(Component.text("§bFarminghöhle verlassen"), Component.text("§7Möchtest du den Block- und Boosterzähler zurücksetzen?"),
                      Component.text("Zurücksetzen"), () -> {
                        AddonSettings.sessionBlocks = 0;
                        Booster.sessionBoost.set(0);
                        this.addon.pushNotification(Component.text("§bFarminghöhle verlassen"), Component.text("§eDein Block- und Boosterzähler wurde zurückgesetzt."));
                      });
                }
              }

              AddonSettings.playingOn = gameMode;

              JsonObject data = new JsonObject();
              data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
              data.addProperty("userName", this.addon.labyAPI().getName());
              data.addProperty("server", gameMode.contains("MoneyMaker") ? gameMode : "Other");
              data.addProperty("afk", false);
              data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
              ChatClient.sendMessage("playerStatus", data);

            }
          }

        }

        if(serverMessage.isJsonArray()) {
          JsonArray array = serverMessage.getAsJsonArray();

          if(messageKey.equals("account_subtitle")) {
            if(AddonSettings.showJoins) {
              if(switched) {
                this.addon.displayMessage("§8----------------------------------");
                switched = false;
              }
              array.forEach(data -> {
                JsonObject object = data.getAsJsonObject();
                Laby.labyAPI().labyNetController().loadNameByUniqueId(UUID.fromString(object.get("uuid").getAsString()), result -> {
                  if (!result.hasException()) {
                    this.addon.displayMessage(AddonSettings.prefix + result.get() + " hat die Farming-Höhle betreten.");
                  } else {
                    this.addon.displayMessage(AddonSettings.prefix + "Konnte Name für UUID "+object.get("uuid") + " nicht abrufen.");
                  }
                });

              });
            }

          }

        }
      } catch (PayloadReaderException ignored) {

      }
    }
  }

}
