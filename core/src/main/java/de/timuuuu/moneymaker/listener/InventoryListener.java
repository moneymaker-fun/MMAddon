package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.BoosterInventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import net.labymod.api.event.Subscribe;

public class InventoryListener {

  private MoneyMakerAddon addon;

  public InventoryListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onInventoryRenderSlot(BoosterInventoryRenderSlotEvent event) {

    if(!(event.getInventoryName().startsWith("Booster-Übersicht") || event.getInventoryName().startsWith("Booster overview"))) return;
    if(!event.getDisplayName().contains("Booster")) return;

    // 1.8 - 1.12

    if(event.getGameVersion().equals("1.8") || event.getGameVersion().equals("1.12")) {

      return;
    }

    // 1.16+
    // {"italic":false,"extra":[{"color":"aqua","text":"+2.000 % Booster "},{"color":"gray","text":"(4)"}],"text":""}

    this.addon.logger().debug("Slot >> " + event.getSlot() + " | Item Name >> " + event.getDisplayName());

  }

  @Subscribe
  public void onInventoryClose(InventoryCloseEvent event) {

  }

}
