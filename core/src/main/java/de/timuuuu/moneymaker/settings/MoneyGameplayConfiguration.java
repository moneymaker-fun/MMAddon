package de.timuuuu.moneymaker.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

@SpriteTexture("sprite/settings")
public class MoneyGameplayConfiguration extends Config {

  @SpriteSlot(y = 2, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> shortBoosterMessage = new ConfigProperty<>(false);

  @IntroducedIn(value = "0.0.5", namespace = "moneymaker")
  @SpriteSlot(y = 2, x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideEmptyMessages = new ConfigProperty<>(false);

  @SpriteSlot(y = 2, x = 5)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideWorkerUpgradeMessage = new ConfigProperty<>(false);

  @SpriteSlot(y = 2, x = 7)
  @IntroducedIn(value = "0.0.6", namespace = "moneymaker")
  @SwitchSetting
  private final ConfigProperty<Boolean> hideBuySellWorkerMessage = new ConfigProperty<>(false);

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

  public ConfigProperty<Boolean> shortBoosterMessage() {
    return shortBoosterMessage;
  }

  public ConfigProperty<Boolean> hideEmptyMessages() {
    return hideEmptyMessages;
  }

  public ConfigProperty<Boolean> hideWorkerUpgradeMessage() {
    return hideWorkerUpgradeMessage;
  }

  public ConfigProperty<Boolean> hideBuySellWorkerMessage() {
    return hideBuySellWorkerMessage;
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

}
