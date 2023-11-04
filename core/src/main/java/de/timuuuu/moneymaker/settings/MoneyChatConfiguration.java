package de.timuuuu.moneymaker.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

public class MoneyChatConfiguration extends Config {

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
