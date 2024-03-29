package de.timuuuu.moneymaker.settings;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.DiscordAPI;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("sprite/settings")
public class MoneyDiscordConfiguration extends Config {

  public MoneyDiscordConfiguration() {

    this.enabled.addChangeListener((type, oldValue, newValue) -> {
      if(MoneyMakerAddon.instance() == null) return;
      if(MoneyMakerAddon.instance().discordAPI() == null) return;
      DiscordAPI discordAPI = MoneyMakerAddon.instance().discordAPI();
      if(newValue) {
        discordAPI.update();
      } else {
        discordAPI.removeCustom();
      }
    });

    this.showLocation.addChangeListener(aBoolean -> {
      if(MoneyMakerAddon.instance() == null) return;
      if(MoneyMakerAddon.instance().discordAPI() == null) return;
      DiscordAPI discordAPI = MoneyMakerAddon.instance().discordAPI();
      discordAPI.update();
    });

    this.showStats.addChangeListener(aBoolean -> {
      if(MoneyMakerAddon.instance() == null) return;
      if(MoneyMakerAddon.instance().discordAPI() == null) return;
      DiscordAPI discordAPI = MoneyMakerAddon.instance().discordAPI();
      discordAPI.update();
    });

  }

  @ParentSwitch
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot(y = 1, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showLocation = new ConfigProperty<>(true);

  @SpriteSlot(y = 1, x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> showStats = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> enabled() {
    return enabled;
  }

  public ConfigProperty<Boolean> showLocation() {
    return showLocation;
  }

  public ConfigProperty<Boolean> showStats() {
    return showStats;
  }

}
