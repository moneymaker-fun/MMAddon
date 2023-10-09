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
    AddonSettings.breakGoalEnabled = false;
    AddonSettings.sessionBlocks = 0;
    AddonSettings.breakGoal = 0;
    AddonSettings.breakGoalBlocks = 0;
    AddonSettings.balance = "";
    AddonSettings.nextWorkerCost = "";
    AddonSettings.debrisCost = "";

    JsonObject data = new JsonObject();
    data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    data.addProperty("userName", this.addon.labyAPI().getName());
    data.addProperty("server", "OFFLINE");
    data.addProperty("afk", false);
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    ChatClient.sendMessage("playerStatus", data);
  }

}
