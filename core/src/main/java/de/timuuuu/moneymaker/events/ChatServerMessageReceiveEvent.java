package de.timuuuu.moneymaker.events;

import com.google.gson.JsonObject;
import net.labymod.api.event.Event;

public class ChatServerMessageReceiveEvent implements Event {

  private JsonObject message;

  public ChatServerMessageReceiveEvent(JsonObject message) {
    this.message = message;
  }

  public JsonObject message() {
    return message;
  }
}
