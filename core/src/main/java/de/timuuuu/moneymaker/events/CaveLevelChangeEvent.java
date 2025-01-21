package de.timuuuu.moneymaker.events;

import de.timuuuu.moneymaker.utils.AddonUtil.FarmingCave;
import net.labymod.api.event.Event;

public class CaveLevelChangeEvent implements Event {

  private FarmingCave previousCave;
  private FarmingCave newCave;

  public CaveLevelChangeEvent(FarmingCave previousCave, FarmingCave newCave) {
    this.previousCave = previousCave;
    this.newCave = newCave;
  }

  public FarmingCave previousCave() {
    return previousCave;
  }

  public FarmingCave newCave() {
    return newCave;
  }
}
