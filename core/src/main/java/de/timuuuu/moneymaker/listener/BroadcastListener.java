package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectBroadcastEvent;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectBroadcastEvent.Action;

public class BroadcastListener {

  private MoneyMakerAddon addon;

  public BroadcastListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onBroadcastReceive(LabyConnectBroadcastEvent event) {
    if(event.action() != Action.RECEIVE) return;
    if(!event.getKey().equals("moneymaker_addon_chat")) return;
    JsonElement payload = event.getPayload();
    if(!payload.isJsonObject()) return;
    JsonObject object = payload.getAsJsonObject();

    this.addon.logger().info(object.toString());
    this.addon.displayMessage(object.toString());

    if(object.has("chatMessage") && object.get("chatMessage").isJsonObject()) {
      MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(object.get("chatMessage").getAsJsonObject());
      if(chatMessage == null) return;
      this.addon.displayMessage(chatMessage.toString());
    }

  }

}
