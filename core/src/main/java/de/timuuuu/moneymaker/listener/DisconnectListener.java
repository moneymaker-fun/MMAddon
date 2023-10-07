package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class DisconnectListener {

  @Subscribe
  public void onDisconnect(ServerDisconnectEvent event){
    AddonSettings.playingOn = "Hauptmenü";
  }

}
