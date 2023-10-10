package de.timuuuu.moneymaker;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
public class MoneyMakerConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection(value = "general", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> shortBoosterMessage = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideWorkerUpdateMessage = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> notifyOnMoneyReached = new ConfigProperty<>(false);

  @SettingSection(value = "chat", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> chatNotification = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> chatNotificationSound = new ConfigProperty<>(true);




  // Settings Getters

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> shortBoosterMessage() {
    return shortBoosterMessage;
  }

  public ConfigProperty<Boolean> hideWorkerUpdateMessage() {
    return hideWorkerUpdateMessage;
  }

  public ConfigProperty<Boolean> notifyOnMoneyReached() {
    return notifyOnMoneyReached;
  }

  public ConfigProperty<Boolean> chatNotification() {
    return chatNotification;
  }

  public ConfigProperty<Boolean> chatNotificationSound() {
    return chatNotificationSound;
  }


  // Internal Settings

  private final ConfigProperty<Boolean> exportOnShutdown = new ConfigProperty<>(false);

  private final ConfigProperty<Boolean> chatReconnectButton = new ConfigProperty<>(false);

  public ConfigProperty<Boolean> exportOnShutdown() {
    return exportOnShutdown;
  }

  public ConfigProperty<Boolean> chatReconnectButton() {
    return chatReconnectButton;
  }
}
