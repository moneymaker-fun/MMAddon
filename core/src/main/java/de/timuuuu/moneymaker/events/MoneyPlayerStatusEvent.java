package de.timuuuu.moneymaker.events;

import de.timuuuu.moneymaker.utils.MoneyPlayer;
import net.labymod.api.event.Event;
import java.util.UUID;

public class MoneyPlayerStatusEvent implements Event {

  private UUID uuid;
  private MoneyPlayer player;

  public MoneyPlayerStatusEvent(UUID uuid, MoneyPlayer player) {
    this.uuid = uuid;
    this.player = player;
  }

  public UUID uuid() {
    return uuid;
  }

  public MoneyPlayer player() {
    return player;
  }
}
