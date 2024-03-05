package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;
import java.util.List;

public class InventoryRenderSlotEvent implements Event {

  private String inventoryName;
  private int slot;
  private String displayName;
  private List<String> loreList;
  private String gameVersion;

  public InventoryRenderSlotEvent(String inventoryName, int slot, String displayName, List<String> loreList,
      String gameVersion) {
    this.inventoryName = inventoryName;
    this.slot = slot;
    this.displayName = displayName;
    this.loreList = loreList;
    this.gameVersion = gameVersion;
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

  public List<String> getLoreList() {
    return loreList;
  }

  public String getGameVersion() {
    return gameVersion;
  }

}
