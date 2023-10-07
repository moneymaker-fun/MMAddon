package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonObject;
import java.util.UUID;

public class MoneyChatMessage {

  private UUID uuid;
  private String userName;
  private String message;

  public MoneyChatMessage(UUID uuid, String userName, String message) {
    this.uuid = uuid;
    this.userName = userName;
    this.message = message;
  }

  public static MoneyChatMessage fromJson(JsonObject object) {
    if(object.has("uuid")) {
      return new MoneyChatMessage(
          UUID.fromString(object.get("uuid").getAsString()),
          object.get("userName").getAsString(),
          object.get("message").getAsString()
          );
    }
    return null;
  }

  public JsonObject toJson() {
    JsonObject object = new JsonObject();
    object.addProperty("uuid", this.uuid.toString());
    object.addProperty("userName", this.userName);
    object.addProperty("message", this.message);
    return object;
  }

  public UUID uuid() {
    return uuid;
  }

  public String userName() {
    return userName;
  }

  public String message() {
    return message;
  }
}
