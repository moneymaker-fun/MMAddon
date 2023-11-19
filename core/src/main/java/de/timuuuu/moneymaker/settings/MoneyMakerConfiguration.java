package de.timuuuu.moneymaker.settings;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
@SpriteTexture("sprite/settings")
public class MoneyMakerConfiguration extends AddonConfig {

  @SettingSection(value = "general", center = true)

  @SpriteSlot()
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot(x = 1)
  public MoneyDiscordConfiguration moneyDiscordConfiguration = new MoneyDiscordConfiguration();

  @SpriteSlot(x = 2)
  public MoneyChatConfiguration moneyChatConfiguration = new MoneyChatConfiguration();

  @IntroducedIn(value = "0.0.5", namespace = "moneymaker")
  @SpriteSlot(y = 7)
  public MoneyBadgeConfiguration moneyBadgeConfiguration = new MoneyBadgeConfiguration();


  @SettingSection(value = "gameplay", center = true)

  @SpriteSlot(y = 2, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> shortBoosterMessage = new ConfigProperty<>(false);

  @IntroducedIn(value = "0.0.5", namespace = "moneymaker")
  @SpriteSlot(y = 2, x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideEmptyMessages = new ConfigProperty<>(false);

  @SpriteSlot(y = 2, x = 5)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideWorkerUpdateMessage = new ConfigProperty<>(false);

  @SpriteSlot(y = 2, x = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideTeleportMessage = new ConfigProperty<>(false);

  @SpriteSlot(y = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> notifyOnMoneyReached = new ConfigProperty<>(false);

  @SpriteSlot(y = 2, x = 6)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideEffectMessage = new ConfigProperty<>(false);

  @SettingRequires(value = "hideEffectMessage", invert = true)
  @SpriteSlot(y = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> showTimersOnEffect = new ConfigProperty<>(false);

  @SpriteSlot(y = 2, x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideFullBoosterInventory = new ConfigProperty<>(false);


  // Settings Getters

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }


  // Gameplay

  public ConfigProperty<Boolean> shortBoosterMessage() {
    return shortBoosterMessage;
  }

  public ConfigProperty<Boolean> hideEmptyMessages() {
    return hideEmptyMessages;
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
