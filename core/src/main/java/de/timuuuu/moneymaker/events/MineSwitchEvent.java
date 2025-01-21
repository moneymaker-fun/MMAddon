package de.timuuuu.moneymaker.events;

import de.timuuuu.moneymaker.utils.AddonUtil.MineType;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.Nullable;

public class MineSwitchEvent implements Event {

  private MineType previousMine;
  private MineType newMine;

  public MineSwitchEvent(@Nullable MineType previousMine, @Nullable MineType newMine) {
    this.previousMine = previousMine;
    this.newMine = newMine;
  }

  public MineType getPreviousMine() {
    return previousMine;
  }

  public MineType getNewMine() {
    return newMine;
  }

}
