package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonObject;
import java.util.UUID;

public class MoneyChatMessage {

  private String time;
  private UUID uuid;
  private String userName;
  private String message;

  public MoneyChatMessage(String time, UUID uuid, String userName, String message) {
    this.time = time;
    this.uuid = uuid;
    this.userName = userName;
    this.message = message;
  }

  public static MoneyChatMessage fromJson(JsonObject object) {
    if(object.has("uuid")) {
      return new MoneyChatMessage(
          object.get("time").getAsString(),
          UUID.fromString(object.get("uuid").getAsString()),
          object.get("userName").getAsString(),
          object.get("message").getAsString()
          );
    }
    return null;
  }

  public JsonObject toJson() {
    JsonObject object = new JsonObject();
    object.addProperty("time", this.time);
    object.addProperty("uuid", this.uuid.toString());
    object.addProperty("userName", this.userName);
    object.addProperty("message", this.message);
    return object;
  }

  public String time() {
    return time;
  }

  public void time(String time) {
    this.time = time;
  }

  public UUID uuid() {
    return uuid;
  }

  public void uuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String userName() {
    return userName;
  }

  public void userName(String userName) {
    this.userName = userName;
  }

  public String message() {
    return message;
  }

  public void message(String message) {
    this.message = message;
  }
}
