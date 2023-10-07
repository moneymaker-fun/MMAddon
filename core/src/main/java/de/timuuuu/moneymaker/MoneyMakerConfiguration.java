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

  @SettingSection(value = "chat", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> chatNotification = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> chatNotificationSound = new ConfigProperty<>(true);

  private final ConfigProperty<Boolean> exportOnShutdown = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> chatNotification() {
    return chatNotification;
  }

  public ConfigProperty<Boolean> chatNotificationSound() {
    return chatNotificationSound;
  }

  public ConfigProperty<Boolean> getExportOnShutdown() {
    return exportOnShutdown;
  }
}
