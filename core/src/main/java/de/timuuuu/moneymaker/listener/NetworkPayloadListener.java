package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
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
  public void onSwitch(SubServerSwitchEvent event) {
    switched = true;
  }

  @Subscribe
  public void onNetworkPayload(NetworkPayloadEvent event) {
    if(event.identifier().getNamespace().equals("labymod3") & event.identifier().getPath().equals("main")) {
      try {
        PayloadReader reader = new PayloadReader(event.getPayload());
        String messageKey = reader.readString();
        String messageContent = reader.readString();

        JsonElement serverMessage = WebResolver.GSON.fromJson(messageContent, JsonElement.class);

        if(serverMessage.isJsonObject()) {

          JsonObject obj = serverMessage.getAsJsonObject();


          if(messageKey.equals("discord_rpc")) {

            if (obj.has("hasGame")) {
              AddonSettings.playingOn = obj.get("game_mode").getAsString();
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
