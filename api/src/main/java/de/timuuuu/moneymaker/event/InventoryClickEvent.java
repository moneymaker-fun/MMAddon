package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;
import java.util.List;

public class InventoryClickEvent implements Event {

  private String inventoryName;
  private int slot;
  private String itemName;
  private List<String> itemLore;
  private String gameVersion;

  public InventoryClickEvent(String inventoryName, int slot, String itemName, List<String> itemLore,
      String gameVersion) {
    this.inventoryName = inventoryName;
    this.slot = slot;
    this.itemName = itemName;
    this.itemLore = itemLore;
    this.gameVersion = gameVersion;
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

  public List<String> getItemLore() {
    return itemLore;
  }

  public String getGameVersion() {
    return gameVersion;
  }
}
