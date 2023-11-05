package de.timuuuu.moneymaker.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

@SpriteTexture("sprite/settings")
public class MoneyChatConfiguration extends Config {

  @SpriteSlot(y = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> notification = new ConfigProperty<>(true);

  @SettingRequires("notification")
  @SwitchSetting
  private final ConfigProperty<Boolean> notificationSound = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> onlineOfflineMessages = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> notification() {
    return notification;
  }

  public ConfigProperty<Boolean> notificationSound() {
    return notificationSound;
  }

  public ConfigProperty<Boolean> onlineOfflineMessages() {
    return onlineOfflineMessages;
  }
}
