package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class DisconnectListener {

  private MoneyMakerAddon addon;

  public DisconnectListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onDisconnect(ServerDisconnectEvent event){
    AddonSettings.resetValues(true);

    JsonObject data = new JsonObject();
    data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    data.addProperty("userName", this.addon.labyAPI().getName());
    data.addProperty("server", "OFFLINE");
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient.sendMessage("playerStatus", data);

    this.addon.discordAPI().removeCustom();
    this.addon.discordAPI().removeSaved();
    this.addon.discordAPI().cancelUpdater();
  }

  /*@Subscribe
  public void onShutdown(GameShutdownEvent event) {

    JsonObject data = new JsonObject();
    data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    data.addProperty("userName", this.addon.labyAPI().getName());
    data.addProperty("server", "OFFLINE");
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient.sendMessage("playerStatus", data);
    this.addon.chatClient.sendQuitData(this.addon.labyAPI().getUniqueId().toString());

    if(this.addon.configuration().exportOnShutdown().get()) {
      BoosterActivity.writeLinkedListToCSV(true);
    }

    AddonUpdater.executeUpdater();

  }*/

}
