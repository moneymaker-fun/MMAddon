package de.timuuuu.moneymaker;

import de.timuuuu.moneymaker.managers.DiscordAPI;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
public class MoneyMakerConfiguration extends AddonConfig {

  public MoneyMakerConfiguration() {

    this.discordRichPresence.addChangeListener((type, oldValue, newValue) -> {
      DiscordAPI discordAPI = MoneyMakerAddon.instance().discordAPI();
      if(discordAPI != null) {
        if(newValue) {
          discordAPI.update();
        } else {
          discordAPI.removeCustom();
        }
      }
    });

  }

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection(value = "general", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> shortBoosterMessage = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideWorkerUpdateMessage = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideTeleportMessage = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> notifyOnMoneyReached = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideEffectMessage = new ConfigProperty<>(false);

  @SettingRequires(value = "hideEffectMessage", invert = true)
  @SwitchSetting
  private final ConfigProperty<Boolean> showTimersOnEffect = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideFullBoosterInventory = new ConfigProperty<>(false);

  @SettingSection(value = "chat", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> chatNotification = new ConfigProperty<>(true);

  @SettingRequires("chatNotification")
  @SwitchSetting
  private final ConfigProperty<Boolean> chatNotificationSound = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> chatOnlineOfflineMessages = new ConfigProperty<>(true);

  // Other

  @SettingSection(value = "other", center = true)

  @SwitchSetting
  private final ConfigProperty<Boolean> discordRichPresence = new ConfigProperty<>(true);



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

  public ConfigProperty<Boolean> hideTeleportMessage() {
    return hideTeleportMessage;
  }

  public ConfigProperty<Boolean> notifyOnMoneyReached() {
    return notifyOnMoneyReached;
  }

  public ConfigProperty<Boolean> showTimersOnEffect() {
    return showTimersOnEffect;
  }

  public ConfigProperty<Boolean> hideEffectMessage() {
    return hideEffectMessage;
  }

  public ConfigProperty<Boolean> hideFullBoosterInventory() {
    return hideFullBoosterInventory;
  }

  // Chat Section

  public ConfigProperty<Boolean> chatNotification() {
    return chatNotification;
  }

  public ConfigProperty<Boolean> chatNotificationSound() {
    return chatNotificationSound;
  }

  public ConfigProperty<Boolean> chatOnlineOfflineMessages() {
    return chatOnlineOfflineMessages;
  }

  // Other

  public ConfigProperty<Boolean> discordRichPresence() {
    return discordRichPresence;
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
