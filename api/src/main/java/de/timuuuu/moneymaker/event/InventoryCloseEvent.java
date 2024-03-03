package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;

public class InventoryCloseEvent implements Event {

  private String inventoryName;

  public InventoryCloseEvent(String inventoryName) {
    this.inventoryName = inventoryName;
  }

  public String getInventoryName() {
    return inventoryName;
  }
}
