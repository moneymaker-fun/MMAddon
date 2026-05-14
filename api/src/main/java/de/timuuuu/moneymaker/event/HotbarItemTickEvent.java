package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.event.EventUtil.Item;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.NotNull;

public class HotbarItemTickEvent implements Event {

  private Item item;
  private String displayName;
  private Object loreList;

  public HotbarItemTickEvent(@NotNull Item item, String displayName, Object loreList) {
    this.item = item;
    this.displayName = displayName;
    this.loreList = loreList;
  }

  public Item item() {
    return item;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Object getLoreList() {
    return loreList;
  }

}
