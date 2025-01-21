package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class InventoryRenderSlotEvent implements Event {

  private String inventoryName;
  private int slot;
  private String displayName;
  private List<String> loreList;
  private TextVersion textVersion;

  public InventoryRenderSlotEvent(String inventoryName, int slot, String displayName, List<String> loreList, @NotNull TextVersion textVersion) {
    this.inventoryName = inventoryName;
    this.slot = slot;
    this.displayName = displayName;
    this.loreList = loreList;
    this.textVersion = textVersion;
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

  public TextVersion textVersion() {
    return textVersion;
  }

}
