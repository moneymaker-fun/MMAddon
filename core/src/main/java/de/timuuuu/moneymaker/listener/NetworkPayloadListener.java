package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.popup.LanguageInfoActivity;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.settings.AddonSettings.FarmingReset;
import de.timuuuu.moneymaker.boosters.Booster;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.serverapi.api.payload.exception.PayloadReaderException;
import net.labymod.serverapi.api.payload.io.PayloadReader;
import java.util.concurrent.TimeUnit;

public class NetworkPayloadListener {

  private final MoneyMakerAddon addon;

  private boolean langInfoOpened = false;
  private boolean motdSent = false;

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

        JsonElement serverMessage = this.addon.gson().fromJson(messageContent, JsonElement.class);

        if(serverMessage.isJsonObject()) {

          JsonObject obj = serverMessage.getAsJsonObject();


          if(messageKey.equals("discord_rpc")) {

            if (obj.has("hasGame")) {
              String gameMode = obj.get("game_mode").getAsString();

              if(this.addon.addonUtil().inFarming() && gameMode.contains("Mine")) {
                AddonSettings.FarmingReset farmingReset = this.addon.configuration().farmingAutoReset().get();
                if(farmingReset == FarmingReset.AUTOMATICALLY) {
                  this.addon.addonUtil().sessionBlocks(0);
                  this.addon.addonUtil().sessionKills(0);
                  Booster.sessionBoost.set(0);
                  Booster.sessionBoosters.set(0);
                  this.addon.pushNotification(Component.translatable("moneymaker.notification.dataReset.farming.title", NamedTextColor.AQUA),
                      Component.translatable("moneymaker.notification.farming.left.done", NamedTextColor.YELLOW));
                }
                if(farmingReset == FarmingReset.ASK) {
                  MoneyMakerAddon.pushNotification(Component.translatable("moneymaker.notification.dataReset.farming.title", NamedTextColor.AQUA),
                      Component.translatable("moneymaker.notification.dataReset.farming.question", NamedTextColor.GRAY),
                      Component.translatable("moneymaker.notification.dataReset.farming.button"), () -> {
                        this.addon.addonUtil().sessionBlocks(0);
                        this.addon.addonUtil().sessionKills(0);
                        Booster.sessionBoost.set(0);
                        Booster.sessionBoosters.set(0);
                        this.addon.pushNotification(Component.translatable("moneymaker.notification.dataReset.farming.title", NamedTextColor.AQUA),
                            Component.translatable("moneymaker.notification.dataReset.farming.done", NamedTextColor.YELLOW));
                  });
                }
              }

              if(!gameMode.contains("Other")) {
                this.addon.discordAPI().setSaved();
              }

              if(gameMode.contains("MoneyMaker")) {
                this.addon.discordAPI().update();
                this.addon.discordAPI().startUpdater();
              } else {
                if(this.addon.addonUtil().connectedToMoneyMaker()) {
                  this.addon.discordAPI().cancelUpdater();
                  this.addon.discordAPI().removeCustom();
                  this.addon.discordAPI().removeSaved();
                }
              }

              this.addon.addonUtil().inMine(gameMode.contains("Mine"));
              this.addon.addonUtil().inFarming(gameMode.contains("Farming"));

              if(!this.addon.addonUtil().inFarming()) {
                this.addon.chatClient().util().sendPlayerStatus(this.addon.labyAPI().getUniqueId().toString(), this.addon.labyAPI().getName(), false);
              }

              if(this.addon.addonUtil().connectedToMoneyMaker()) {
                if(!this.addon.configuration().languageInfoClosed().get()) {
                  if(!langInfoOpened) {
                    langInfoOpened = true;
                    Task.builder(() -> Laby.labyAPI().minecraft().executeNextTick(() -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new LanguageInfoActivity(this.addon, Laby.labyAPI().minecraft().minecraftWindow().currentScreen())))).delay(2, TimeUnit.SECONDS).build().execute();
                  }
                }

                if(!this.addon.addonUtil().motd().isEmpty() && !this.motdSent) {
                  boolean show = this.addon.configuration().showMOTD().get();
                  if(this.addon.addonUtil().motdPriority()) {
                    show = true;
                  }
                  if(show) {
                    this.motdSent = true;
                    this.addon.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.text.motd", NamedTextColor.GREEN)).append(Component.text(":", NamedTextColor.DARK_GRAY)));
                    this.addon.displayMessage(PlainTextComponentSerializer.plainUrl().deserialize(this.addon.addonUtil().motd()));
                  }
                }

              }

            }
          }

        }
      } catch (PayloadReaderException ignored) {

      }
    }
  }

}
