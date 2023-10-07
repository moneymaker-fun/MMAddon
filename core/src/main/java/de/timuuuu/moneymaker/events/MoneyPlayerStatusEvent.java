package de.timuuuu.moneymaker.events;

import net.labymod.api.event.Event;
import java.util.UUID;

public class MoneyPlayerStatusEvent implements Event {

  private UUID uuid;
  private String userName;
  private String server;

  public MoneyPlayerStatusEvent(UUID uuid, String userName, String server) {
    this.uuid = uuid;
    this.userName = userName;
    this.server = server;
  }

  public UUID uuid() {
    return uuid;
  }

  public String userName() {
    return userName;
  }

  public String server() {
    return server;
  }
}
