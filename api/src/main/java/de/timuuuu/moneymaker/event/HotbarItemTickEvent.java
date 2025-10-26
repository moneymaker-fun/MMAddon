package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.event.EventUtil.Item;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class HotbarItemTickEvent implements Event {

  private Item item;
  private String displayName;
  private List<String> loreList;
  private TextVersion textVersion;

  public HotbarItemTickEvent(@NotNull Item item, String displayName, List<String> loreList, @NotNull TextVersion textVersion) {
    this.item = item;
    this.displayName = displayName;
    this.loreList = loreList;
    this.textVersion = textVersion;
  }

  public Item item() {
    return item;
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
