package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;
import java.util.List;

public class SwordTickEvent implements Event {

  private String displayName;
  private List<String> loreList;
  private String gameVersion;

  public SwordTickEvent(String displayName, List<String> loreList, String gameVersion) {
    this.displayName = displayName;
    this.loreList = loreList;
    this.gameVersion = gameVersion;
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
