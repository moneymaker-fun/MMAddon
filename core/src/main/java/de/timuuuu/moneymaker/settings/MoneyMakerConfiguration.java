package de.timuuuu.moneymaker.settings;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings.FarmingReset;
import de.timuuuu.moneymaker.settings.AddonSettings.UpdateMode;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownEntryTranslationPrefix;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;

@ConfigName("settings")
@SpriteTexture("sprite/settings")
public class MoneyMakerConfiguration extends AddonConfig {

  @SettingSection(value = "general", center = true)

  @SpriteSlot()
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot(y = 1)
  public MoneyDiscordConfiguration discordConfiguration = new MoneyDiscordConfiguration();

  @SpriteSlot(x = 1)
  public MoneyChatConfiguration chatConfiguration = new MoneyChatConfiguration();

  @IntroducedIn(value = "0.0.5", namespace = "moneymaker")
  @SpriteSlot(y = 7)
  public MoneyBadgeConfiguration badgeConfiguration = new MoneyBadgeConfiguration();

  @IntroducedIn(value = "1.4.2", namespace = "moneymaker")
  public MoneyGameplayConfiguration gameplayConfiguration = new MoneyGameplayConfiguration();


  @SettingSection(value = "other", center = true)

  @MethodOrder(before = "exportBoosterOnShutdown")
  @ButtonSetting
  public void showBlocksInLeaderboard() {
    MoneyMakerAddon addon = MoneyMakerAddon.instance();
    if(addon.addonUtil().leaderboardShowBlocks()) {
      addon.addonUtil().leaderboardShowBlocks(false);
      addon.pushNotification(
          Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
          Component.translatable("moneymaker.notification.leaderboard.blocks.disabled", NamedTextColor.RED)
      );
    } else {
      addon.addonUtil().leaderboardShowBlocks(true);
      addon.pushNotification(
          Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
          Component.translatable("moneymaker.notification.leaderboard.blocks.enabled", NamedTextColor.GREEN)
      );
    }
  }

  @IntroducedIn(value = "1.1.0", namespace = "moneymaker")
  @SpriteSlot(y = 3, x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> exportBoosterOnShutdown = new ConfigProperty<>(false);

  @IntroducedIn(value = "1.2.3", namespace = "moneymaker")
  @SpriteSlot(y = 3, x = 2)
  @DropdownSetting
  @DropdownEntryTranslationPrefix("moneymaker.settings.farmingAutoReset.type")
  private final ConfigProperty<AddonSettings.FarmingReset> farmingAutoReset = new ConfigProperty<>(FarmingReset.ASK);

  @IntroducedIn(value = "1.3.0", namespace = "moneymaker")
  @SpriteSlot(y = 3, x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showWidgetsAlways = new ConfigProperty<>(false);

  @IntroducedIn(value = "1.3.0", namespace = "moneymaker")
  @SpriteSlot(y = 3)
  @DropdownSetting
  @DropdownEntryTranslationPrefix("moneymaker.settings.updateMode.type")
  private final ConfigProperty<AddonSettings.UpdateMode> updateMode = new ConfigProperty<>(UpdateMode.NORMAL);

  @IntroducedIn(value = "1.4.2", namespace = "moneymaker")
  @SwitchSetting
  private final ConfigProperty<Boolean> showTotalBoostMessage = new ConfigProperty<>(true);

  // Settings Getters

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> exportBoosterOnShutdown() {
    return exportBoosterOnShutdown;
  }

  public ConfigProperty<AddonSettings.FarmingReset> farmingAutoReset() {
    return farmingAutoReset;
  }

  public ConfigProperty<Boolean> showWidgetsAlways() {
    return showWidgetsAlways;
  }

  public ConfigProperty<AddonSettings.UpdateMode> updateMode() {
    return updateMode;
  }

  public ConfigProperty<Boolean> showTotalBoostMessage() {
    return showTotalBoostMessage;
  }

  // Internal Settings

  @Exclude
  private final ConfigProperty<Boolean> chatReconnectButton = new ConfigProperty<>(false);

  @Exclude
  private final ConfigProperty<Boolean> chatShowAllPlayers = new ConfigProperty<>(true);

  @Exclude
  private final ConfigProperty<Boolean> languageInfoClosed = new ConfigProperty<>(false);

  @Exclude
  private final ConfigProperty<Integer> chatRulesVersion = new ConfigProperty<>(0);

  public ConfigProperty<Boolean> chatReconnectButton() {
    return chatReconnectButton;
  }

  public ConfigProperty<Boolean> chatShowAllPlayers() {
    return chatShowAllPlayers;
  }

  public ConfigProperty<Boolean> languageInfoClosed() {
    return languageInfoClosed;
  }

  public ConfigProperty<Integer> chatRulesVersion() {
    return chatRulesVersion;
  }
}
