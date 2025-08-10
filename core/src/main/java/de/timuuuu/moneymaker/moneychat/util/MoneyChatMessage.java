package de.timuuuu.moneymaker.moneychat.util;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.enums.MoneyChatMessageType;
import de.timuuuu.moneymaker.enums.MoneyRank;

import java.util.UUID;

public class MoneyChatMessage {

  private String messageId;
  private UUID uuid;
  private String userName;
  private String message;
  private MoneyRank rank;
  private MoneyChatMessageType messageType;
  private boolean deleted = false;
  private boolean fromServerCache;
  private String timeStamp;
  private String addonVersion;
  private String minecraftVersion;

  public MoneyChatMessage(String messageId, UUID uuid, String userName, String message, MoneyRank rank, boolean fromServerCache, String timeStamp, String addonVersion, String minecraftVersion) {
    this.messageId = messageId;
    this.uuid = uuid;
    this.userName = userName;
    this.message = message;
    this.rank = rank;
    this.messageType = MoneyChatMessageType.getMessageType(uuid.toString());
    this.fromServerCache = fromServerCache;
    this.timeStamp = timeStamp;
    this.addonVersion = addonVersion;
    this.minecraftVersion = minecraftVersion;
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
          object.has("timeStamp") ? object.get("timeStamp").getAsString() : null,
          object.has("addonVersion") ? object.get("addonVersion").getAsString() : "N/A",
          object.has("minecraftVersion") ? object.get("minecraftVersion").getAsString() : "N/A"
          );
    }
    return null;
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

  public MoneyChatMessageType messageType() {
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

  public String addonVersion() {
    return addonVersion;
  }

  public String minecraftVersion() {
    return minecraftVersion;
  }

}
