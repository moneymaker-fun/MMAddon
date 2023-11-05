package de.timuuuu.moneymaker.settings;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.managers.DiscordAPI;
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


  @SettingSection(value = "general", center = true)

  @SpriteSlot()
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot(x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> discordRichPresence = new ConfigProperty<>(true);

  @SpriteSlot(x = 2)
  public MoneyChatConfiguration moneyChatConfiguration = new MoneyChatConfiguration();

  @IntroducedIn(value = "0.0.5", namespace = "moneymaker")
  @SpriteSlot(y = 6)
  public MoneyBadgeConfiguration moneyBadgeConfiguration = new MoneyBadgeConfiguration();


  @SettingSection(value = "gameplay", center = true)

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


  // Settings Getters

  // General

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> discordRichPresence() {
    return discordRichPresence;
  }


  // Gameplay

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
