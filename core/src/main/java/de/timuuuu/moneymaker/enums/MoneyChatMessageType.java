package de.timuuuu.moneymaker.enums;

import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.icon.Icon;

public enum MoneyChatMessageType {

    PLAYER(null, null, null),
    SERVER("00000000-0000-0000-0000-000000000000", Component.text("SYSTEM", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD), SpriteCommon.SERVER),
    DISCORD("00000000-0000-0000-0000-000000000001", Component.text("Discord", TextColor.color(88, 101, 242)).decorate(TextDecoration.BOLD), SpriteCommon.DISCORD);

    private final String uuid;
    private final Component userName;
    private final Icon icon;

    MoneyChatMessageType(String uuid, Component userName, Icon icon) {
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

  public static MoneyChatMessageType getMessageType(String uuid) {
    MoneyChatMessageType messageType = MoneyChatMessageType.PLAYER;
    for(MoneyChatMessageType type : MoneyChatMessageType.values()) {
      if(type.uuid() != null) {
        if(uuid.equals(type.uuid())) {
          messageType = type;
        }
      }
    }
    return messageType;
  }

}
