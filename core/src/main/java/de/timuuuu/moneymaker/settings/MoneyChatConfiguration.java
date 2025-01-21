package de.timuuuu.moneymaker.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SpriteTexture("sprite/settings")
public class MoneyChatConfiguration extends Config {

  @SettingSection(value = "general", center = true)

  @IntroducedIn(namespace = "moneymaker", value = "1.4.0")
  @SpriteSlot(x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> loadChatHistory = new ConfigProperty<>(true);

  @SettingSection(value = "privacy", center = true)

  @IntroducedIn(namespace = "moneymaker", value = "1.7.0")
  @SwitchSetting
  private final ConfigProperty<Boolean> showDetailedLocation = new ConfigProperty<>(true);

  @SettingSection(value = "notifications", center = true)

  @SpriteSlot(y = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> notification = new ConfigProperty<>(true);

  @SettingRequires("notification")
  @SpriteSlot(y = 4, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> notificationSound = new ConfigProperty<>(true);

  @SpriteSlot(y = 4, x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> onlineOfflineMessages = new ConfigProperty<>(true);

  @IntroducedIn(namespace = "moneymaker", value = "1.6.0")
  @SettingRequires("onlineOfflineMessages")
  @DropdownSetting
  private final ConfigProperty<NotificationType> onlineOfflineNotifications = new ConfigProperty<>(NotificationType.LABYMOD);


  public ConfigProperty<Boolean> loadChatHistory() {
    return loadChatHistory;
  }

  public ConfigProperty<Boolean> showDetailedLocation() {
    return showDetailedLocation;
  }

  public ConfigProperty<Boolean> notification() {
    return notification;
  }

  public ConfigProperty<Boolean> notificationSound() {
    return notificationSound;
  }

  public ConfigProperty<Boolean> onlineOfflineMessages() {
    return onlineOfflineMessages;
  }

  public ConfigProperty<NotificationType> onlineOfflineNotifications() {
    return onlineOfflineNotifications;
  }

  public enum NotificationType {
    LABYMOD, CHAT
  }

}
