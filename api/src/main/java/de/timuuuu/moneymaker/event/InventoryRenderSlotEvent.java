package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;

public class InventoryRenderSlotEvent implements Event {

  private String inventoryName;
  private int slot;
  private String displayName;

  public InventoryRenderSlotEvent(String inventoryName, int slot, String displayName) {
    this.inventoryName = inventoryName;
    this.slot = slot;
    this.displayName = displayName;
  }

  public String getInventoryName() {
    return inventoryName;
  }

  public int getSlot() {
    return slot;
  }

  public String getDisplayName() {
    return displayName;
  }

}
