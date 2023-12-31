package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.util.io.web.request.WebResolver;
import net.labymod.serverapi.protocol.payload.exception.PayloadReaderException;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;

public class NetworkPayloadListener {

  private final MoneyMakerAddon addon;

  public NetworkPayloadListener(MoneyMakerAddon addon) {
    this.addon = addon;
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
              String gameMode = obj.get("game_mode").getAsString();

              if(AddonSettings.inFarming && gameMode.contains("Mine")) {
                if(AddonSettings.sessionBlocks > 0) {
                  MoneyMakerAddon.pushNotification(Component.translatable("moneymaker.notification.farming.left.title", TextColor.color(85, 255, 255)),
                      Component.translatable("moneymaker.notification.farming.left.reset-question", TextColor.color(170, 170, 170)),
                      Component.translatable("moneymaker.notification.farming.left.reset-button"), () -> {
                        AddonSettings.sessionBlocks = 0;
                        Booster.sessionBoost.set(0);
                        Booster.sessionBoosters.set(0);
                        AddonSettings.sessionKills = 0;
                        this.addon.pushNotification(Component.translatable("moneymaker.notification.farming.left.title", TextColor.color(85, 255, 255)),
                            Component.translatable("moneymaker.notification.farming.left.reset", TextColor.color(255, 255, 85)));
                      });
                }
              }

              JsonObject data = new JsonObject();
              data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
              data.addProperty("userName", this.addon.labyAPI().getName());
              data.addProperty("server", gameMode.contains("MoneyMaker") ? gameMode : "Other");
              data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
              this.addon.chatClient().sendMessage("playerStatus", data);

              if(!gameMode.contains("Other")) {
                this.addon.discordAPI().setSaved();
              }

              if(gameMode.contains("MoneyMaker")) {
                this.addon.discordAPI().update();
                this.addon.discordAPI().startUpdater();
                /*if(AddonUpdater.updateAvailable() & !AddonUpdater.notified) {
                  AddonUpdater.notified = true;
                  this.addon.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.text.new-update")));
                }*/
              } else {
                if(AddonSettings.inMine || AddonSettings.inFarming) {
                  this.addon.discordAPI().cancelUpdater();
                  this.addon.discordAPI().removeCustom();
                  this.addon.discordAPI().removeSaved();
                }
              }

              AddonSettings.inMine = gameMode.contains("Mine");
              AddonSettings.inFarming = gameMode.contains("Farming");

            }
          }

        }
      } catch (PayloadReaderException ignored) {

      }
    }
  }

}
