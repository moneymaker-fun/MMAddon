package de.timuuuu.moneymaker.event;

import net.labymod.api.event.Event;
import java.util.List;

public class SwordTickEvent implements Event {

  private String displayName;
  private List<String> loreList;
  private int textVersion;

  public SwordTickEvent(String displayName, List<String> loreList, int textVersion) {
    this.displayName = displayName;
    this.loreList = loreList;
    this.textVersion = textVersion;
  }

  public String getDisplayName() {
    return displayName;
  }

  public List<String> getLoreList() {
    return loreList;
  }

  public int getTextVersion() {
    return textVersion;
  }

  public static class TextVersions {
    public static final int RAW = 1;
    public static final int JSON = 2;
  }

}
