package de.timuuuu.moneymaker.chat;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.badges.MoneyRank;
import de.timuuuu.moneymaker.utils.MoneyPlayer;

import java.util.UUID;

public class MoneyChatMessage {

  private String messageId;
  private UUID uuid;
  private String userName;
  private String message;
  private MoneyRank rank;
  private boolean systemMessage;
  private boolean deleted = false;

  public MoneyChatMessage(String messageId, UUID uuid, String userName, String message, MoneyRank rank) {
    this.messageId = messageId;
    this.uuid = uuid;
    this.userName = userName;
    this.message = message;
    this.rank = rank;
    this.systemMessage = uuid.toString().equals("00000000-0000-0000-0000-000000000000");
  }

  public static MoneyChatMessage fromJson(JsonObject object) {
    if(object.has("uuid")) {
      return new MoneyChatMessage(
          object.get("messageId").getAsString(),
          UUID.fromString(object.get("uuid").getAsString()),
          object.get("userName").getAsString(),
          object.get("message").getAsString(),
          object.has("rank") ? MoneyPlayer.rankByName(object.get("rank").getAsString()) : MoneyRank.USER
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

  public String messageId() {
    return messageId;
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

  public void message(String message) {
    this.message = message;
  }

  public MoneyRank rank() {
    return rank;
  }

  public boolean systemMessage() {
    return systemMessage;
  }

  public boolean deleted() {
    return deleted;
  }

  public void deleted(boolean deleted) {
    this.deleted = deleted;
  }
}
