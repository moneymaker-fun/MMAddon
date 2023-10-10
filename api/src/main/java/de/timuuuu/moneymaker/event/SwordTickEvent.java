package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;
import java.util.List;

public class SwordTickEvent implements Event {

  private String displayName;
  private List<String> loreList;

  public SwordTickEvent(String displayName, List<String> loreList) {
    this.displayName = displayName;
    this.loreList = loreList;
  }

  public String getDisplayName() {
    return displayName;
  }

  public List<String> getLoreList() {
    return loreList;
  }
}
