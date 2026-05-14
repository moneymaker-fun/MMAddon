package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;

public class InventoryClickEvent implements Event {

  private String inventoryName;
  private int slot;
  private String itemName;

  public InventoryClickEvent(String inventoryName, int slot, String itemName) {
    this.inventoryName = inventoryName;
    this.slot = slot;
    this.itemName = itemName;
  }

  public String getInventoryName() {
    return inventoryName;
  }

  public int getSlot() {
    return slot;
  }

  public String getItemName() {
    return itemName;
  }
}
