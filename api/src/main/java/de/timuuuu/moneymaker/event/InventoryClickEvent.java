package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class InventoryClickEvent implements Event {

  private String inventoryName;
  private int slot;
  private String itemName;
  private List<String> itemLore;
  private TextVersion textVersion;

  public InventoryClickEvent(String inventoryName, int slot, String itemName, List<String> itemLore, @NotNull TextVersion textVersion) {
    this.inventoryName = inventoryName;
    this.slot = slot;
    this.itemName = itemName;
    this.itemLore = itemLore;
    this.textVersion = textVersion;
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

  public TextVersion textVersion() {
    return textVersion;
  }

}
