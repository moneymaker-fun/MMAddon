package de.timuuuu.moneymaker.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

@SpriteTexture("sprite/settings")
public class MoneyBadgeConfiguration extends Config {

  @SettingSection(value = "general", center = true)

  @SpriteSlot(y = 6)
  @SwitchSetting
  private ConfigProperty<Boolean> textTag = new ConfigProperty<>(true);

  @SpriteSlot(y = 6, x = 1)
  @SwitchSetting
  private ConfigProperty<Boolean> iconTag = new ConfigProperty<>(true);

  @SpriteSlot(y = 6, x = 2)
  @SwitchSetting
  private ConfigProperty<Boolean> tabListIcon = new ConfigProperty<>(true);

  @SettingSection(value = "customization", center = true)

  @ColorPickerSetting(chroma = true)
  private ConfigProperty<Color> textColor = new ConfigProperty<>(Color.ofRGB(255, 255, 85));

  public ConfigProperty<Boolean> iconTag() {
    return iconTag;
  }

  public ConfigProperty<Boolean> textTag() {
    return textTag;
  }

  public ConfigProperty<Boolean> tabListIcon() {
    return tabListIcon;
  }

  public ConfigProperty<Color> textColor() {
    return textColor;
  }
}
