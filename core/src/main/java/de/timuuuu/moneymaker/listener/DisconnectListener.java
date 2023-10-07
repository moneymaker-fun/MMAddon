package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatClient;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class DisconnectListener {

  private MoneyMakerAddon addon;

  public DisconnectListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onDisconnect(ServerDisconnectEvent event){
    AddonSettings.playingOn = "Hauptmenü";
    JsonObject data = new JsonObject();
    data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    data.addProperty("server", "OFFLINE");
    ChatClient.sendMessage("playerStatus", data);
  }

}
