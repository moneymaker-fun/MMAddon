package de.timuuuu.moneymaker.chat;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.chat.ChatClientUtil.MessageType;

import java.util.UUID;

public class MoneyChatMessage {

  private String messageId;
  private UUID uuid;
  private String userName;
  private String message;
  private MoneyRank rank;
  private ChatClientUtil.MessageType messageType;
  private boolean deleted = false;
  private boolean fromServerCache;
  private String timeStamp;

  public MoneyChatMessage(String messageId, UUID uuid, String userName, String message, MoneyRank rank, boolean fromServerCache, String timeStamp) {
    this.messageId = messageId;
    this.uuid = uuid;
    this.userName = userName;
    this.message = message;
    this.rank = rank;
    this.messageType = ChatClientUtil.getMessageType(uuid.toString());
    this.fromServerCache = fromServerCache;
    this.timeStamp = timeStamp;
  }

  public static MoneyChatMessage fromJson(JsonObject object) {
    if(object.has("uuid")) {
      return new MoneyChatMessage(
          object.get("messageId").getAsString(),
          UUID.fromString(object.get("uuid").getAsString()),
          object.get("userName").getAsString(),
          object.get("message").getAsString(),
          object.has("rank") ? MoneyRank.byName(object.get("rank").getAsString()) : MoneyRank.USER,
              object.has("fromCache") && object.get("fromCache").getAsBoolean(),
          object.has("timeStamp") ? object.get("timeStamp").getAsString() : null
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

  public MessageType messageType() {
    return messageType;
  }

  public boolean fromServerCache() {
    return fromServerCache;
  }

  public String timeStamp() {
    return timeStamp;
  }

  public boolean deleted() {
    return deleted;
  }

  public void deleted(boolean deleted) {
    this.deleted = deleted;
  }
}
