package de.timuuuu.moneymaker.events;

import de.timuuuu.moneymaker.utils.AddonUtil.MiningCave;
import net.labymod.api.event.Event;

public class CaveLevelChangeEvent implements Event {

  private MiningCave previousCave;
  private MiningCave newCave;

  public CaveLevelChangeEvent(MiningCave previousCave, MiningCave newCave) {
    this.previousCave = previousCave;
    this.newCave = newCave;
  }

  public MiningCave previousCave() {
    return previousCave;
  }

  public MiningCave newCave() {
    return newCave;
  }
}
