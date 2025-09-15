package de.timuuuu.moneymaker.settings;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketAddonMessage;
import de.timuuuu.moneymaker.settings.AddonSettings.FarmingReset;
import de.timuuuu.moneymaker.settings.AddonSettings.UpdateMode;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
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

  @SettingSection(value = "account", center = true)

  @IntroducedIn(value = "1.7.0", namespace = "moneymaker")
  @MethodOrder(after = "gameplayConfiguration")
  @ButtonSetting
  public void createWebsiteAccount() {
    MoneyMakerAddon addon = MoneyMakerAddon.instance();
    if(addon.moneyChatClient().isAuthenticated()) {
      JsonObject payload = new JsonObject();
      payload.addProperty("uuid", addon.labyAPI().getUniqueId().toString());
      payload.addProperty("username", addon.labyAPI().getName());
      addon.moneyChatClient().sendPacket(new MoneyPacketAddonMessage("website_register", payload));
      addon.labyAPI().minecraft().minecraftWindow().displayScreen(addon.tokenVerificationActivity());
    } else {
      addon.pushNotification(
          Component.translatable("moneymaker.verification.title", TextColor.color(255, 255, 85)),
          Component.translatable("moneymaker.verification.request.chatNotConnected", TextColor.color(255, 85, 85))
      );
    }
  }

  @IntroducedIn(value = "1.7.0", namespace = "moneymaker")
  @MethodOrder(after = "createWebsiteAccount")
  @ButtonSetting
  public void linkDiscordAccount() {
    MoneyMakerAddon addon = MoneyMakerAddon.instance();
    if(addon.moneyChatClient().isAuthenticated()) {
      JsonObject payload = new JsonObject();
      payload.addProperty("uuid", addon.labyAPI().getUniqueId().toString());
      payload.addProperty("username", addon.labyAPI().getName());
      addon.moneyChatClient().sendPacket(new MoneyPacketAddonMessage("discord_link", payload));
      addon.labyAPI().minecraft().minecraftWindow().displayScreen(addon.tokenVerificationActivity());
    } else {
      addon.pushNotification(
          Component.translatable("moneymaker.verification.title", TextColor.color(255, 255, 85)),
          Component.translatable("moneymaker.verification.request.chatNotConnected", TextColor.color(255, 85, 85))
      );
    }
  }

  @SettingSection(value = "other", center = true)

  @MethodOrder(before = "showCustomGameSwitchNotifications")
  @ButtonSetting
  public void showBlocksInLeaderboard() {
    MoneyMakerAddon addon = MoneyMakerAddon.instance();
    if(addon.addonUtil().leaderboardShowBlocks()) {
      addon.addonUtil().leaderboardShowBlocks(false);
      addon.pushNotification(
          Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
          Component.translatable("moneymaker.notification.leaderboard.blocks.disabled", NamedTextColor.RED)
              .append(Component.text("\n"))
              .append(Component.translatable("moneymaker.notification.leaderboard.blocks.info", NamedTextColor.GRAY))
      );
    } else {
      addon.addonUtil().leaderboardShowBlocks(true);
      addon.pushNotification(
          Component.translatable("moneymaker.notification.leaderboard.title", NamedTextColor.GOLD),
          Component.translatable("moneymaker.notification.leaderboard.blocks.enabled", NamedTextColor.GREEN)
              .append(Component.text("\n"))
              .append(Component.translatable("moneymaker.notification.leaderboard.blocks.info", NamedTextColor.GRAY))
      );
    }
  }

  @IntroducedIn(value = "1.6.5", namespace = "moneymaker")
  @SwitchSetting
  private final ConfigProperty<Boolean> showCustomGameSwitchNotifications = new ConfigProperty<>(true);

  @IntroducedIn(value = "1.1.0", namespace = "moneymaker")
  @SpriteSlot(y = 3, x = 3)
  @SwitchSetting
  private final ConfigProperty<Boolean> exportBoosterOnShutdown = new ConfigProperty<>(false);

  @IntroducedIn(value = "1.2.3", namespace = "moneymaker")
  @SpriteSlot(y = 3, x = 2)
  @DropdownSetting
  @DropdownEntryTranslationPrefix("moneymaker.settings.farmingAutoReset.type")
  private final ConfigProperty<AddonSettings.FarmingReset> farmingAutoReset = new ConfigProperty<>(FarmingReset.ASK_LABY);

  @IntroducedIn(value = "1.5.1", namespace = "moneymaker")
  @SpriteSlot(y = 3, x = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> resetOnProfileSwitch = new ConfigProperty<>(false);

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

  @IntroducedIn(value = "1.6.0", namespace = "moneymaker")
  @SwitchSetting
  private final ConfigProperty<Boolean> showMOTD = new ConfigProperty<>(true);

  // Settings Getters

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> showCustomGameSwitchNotifications() {
    return showCustomGameSwitchNotifications;
  }

  public ConfigProperty<Boolean> exportBoosterOnShutdown() {
    return exportBoosterOnShutdown;
  }

  public ConfigProperty<AddonSettings.FarmingReset> farmingAutoReset() {
    return farmingAutoReset;
  }

  public ConfigProperty<Boolean> resetOnProfileSwitch() {
    return resetOnProfileSwitch;
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

  public ConfigProperty<Boolean> showMOTD() {
    return showMOTD;
  }

  // Internal Settings

  @Exclude
  private final ConfigProperty<Boolean> chatReconnectButton = new ConfigProperty<>(false);

  @Exclude
  private final ConfigProperty<Boolean> chatShowAllPlayers = new ConfigProperty<>(false);

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
