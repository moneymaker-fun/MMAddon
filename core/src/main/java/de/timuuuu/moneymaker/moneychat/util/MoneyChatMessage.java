package de.timuuuu.moneymaker.moneychat.util;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.icon.Icon;

import java.util.UUID;

public class MoneyChatMessage {

  private String messageId;
  private UUID uuid;
  private String userName;
  private String message;
  private MoneyRank rank;
  private MessageType messageType;
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
    this.messageType = MessageType.getMessageType(uuid.toString());
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

  public String addonVersion() {
    return addonVersion;
  }

  public String minecraftVersion() {
    return minecraftVersion;
  }

  public enum MessageType {

    PLAYER(null, null, null),
    SERVER("00000000-0000-0000-0000-000000000000", Component.text("SYSTEM", NamedTextColor.DARK_RED).decorate(
        TextDecoration.BOLD), SpriteCommon.SERVER),
    DISCORD("00000000-0000-0000-0000-000000000001", Component.text("Discord", TextColor.color(88, 101, 242)).decorate(TextDecoration.BOLD), SpriteCommon.DISCORD);

    private final String uuid;
    private final Component userName;
    private final Icon icon;

    MessageType(String uuid, Component userName, Icon icon) {
      this.uuid = uuid;
      this.userName = userName;
      this.icon = icon;
    }

    public String uuid() {
      return uuid;
    }

    public Component userName() {
      return userName;
    }

    public Icon icon() {
      return icon;
    }

    public static MessageType getMessageType(String uuid) {
      MessageType messageType = MessageType.PLAYER;
      for(MessageType type : MessageType.values()) {
        if(type.uuid() != null) {
          if(uuid.equals(type.uuid())) {
            messageType = type;
          }
        }
      }
      return messageType;
    }

  }

}
