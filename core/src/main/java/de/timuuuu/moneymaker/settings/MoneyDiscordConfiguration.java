package de.timuuuu.moneymaker.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

@SpriteTexture("sprite/settings")
public class MoneyDiscordConfiguration extends Config {

  @SpriteSlot()
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot(y = 1, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showLocation = new ConfigProperty<>(true);

  @SpriteSlot(y = 1, x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> showStats = new ConfigProperty<>(true);

  @IntroducedIn(namespace = "moneymaker", value = "1.6.0")
  @SettingRequires(value = "showLocation")
  @SwitchSetting
  private ConfigProperty<Boolean> showCaveLevel = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> enabled() {
    return enabled;
  }

  public ConfigProperty<Boolean> showLocation() {
    return showLocation;
  }

  public ConfigProperty<Boolean> showStats() {
    return showStats;
  }

  public ConfigProperty<Boolean> showCaveLevel() {
    return showCaveLevel;
  }
}
