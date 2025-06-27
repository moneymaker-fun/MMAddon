package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.BoosterActivity;
import de.timuuuu.moneymaker.moneychat.util.MoneyChatMessage;
import de.timuuuu.moneymaker.enums.MoneyChatMessageType;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.events.CaveLevelChangeEvent;
import de.timuuuu.moneymaker.events.MineSwitchEvent;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.events.ProfileSwitchEvent;
import de.timuuuu.moneymaker.moneychat.MoneyChatClient.Initiator;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketAddonStatistics;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
import de.timuuuu.moneymaker.settings.MoneyChatConfiguration.NotificationType;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.AddonUtil.FarmingCave;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.Constants.Resources;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.labymod.api.util.I18n;
import net.labymod.api.util.concurrent.task.Task;

public class MoneyAddonListener {

  private MoneyMakerAddon addon;

  public MoneyAddonListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onServerLogin(ServerLoginEvent event) {
    if(event.serverData().actualAddress().matches("gommehd.net", 25565, true) ||
        event.serverData().actualAddress().matches("gommehd.fun", 25565, true) ||
        event.serverData().actualAddress().matches("moneymaker.gg", 25565, true)) {

      String uuid = this.addon.labyAPI().getUniqueId().toString();
      //TODO: Migrate?
      //this.addon.chatClient().util().sendRetrievePlayerData(uuid);
      //this.addon.chatClient().util().sendCheckMute(uuid);

      this.addon.apiUtil().loadChatHistory();

    }
  }

  @Subscribe
  public void onDisconnect(ServerDisconnectEvent event) {
    if(event.serverData().actualAddress().matches("gommehd.net", 25565, true) ||
        event.serverData().actualAddress().matches("gommehd.fun", 25565, true) ||
        event.serverData().actualAddress().matches("moneymaker.gg", 25565, true)) {
      //TODO: Move Leaderboard to Rest API
      //this.addon.chatClient().util().sendLeaderboard(this.addon.labyAPI().getUniqueId().toString(), this.addon.labyAPI().getName());
    }

    this.addon.addonUtil().resetValues(true);

    if(this.addon.moneyChatClient().isAuthenticated()) {
      this.addon.moneyChatClient().sendPacket(new PacketPlayerStatus(
          Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), MoneyRank.USER,
          Util.currentServer(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));
    }

    this.addon.discordAPI().removeCustom();
    this.addon.discordAPI().removeSaved();
    this.addon.discordAPI().cancelUpdater();
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {

    if(this.addon.moneyChatClient().isAuthenticated()) {
      this.addon.moneyChatClient().sendPacket(new PacketAddonStatistics("remove",
          event.previousSession().getUniqueId(), event.previousSession().getUsername(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));
      this.addon.moneyChatClient().sendPacket(new PacketAddonStatistics("add",
          event.newSession().getUniqueId(), event.newSession().getUsername(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));

      this.addon.moneyChatClient().sendPacket(new PacketPlayerStatus(
          event.previousSession().getUniqueId(), event.previousSession().getUsername(), MoneyRank.USER,
          Util.currentServer(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));
      this.addon.moneyChatClient().sendPacket(new PacketPlayerStatus(
          event.newSession().getUniqueId(), event.newSession().getUsername(), MoneyRank.USER,
          Util.currentServer(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));


    }

    //this.addon.chatClient().util().sendStatistics(true, event.previousSession().getUniqueId().toString(), event.previousSession().getUsername());
    //this.addon.chatClient().util().sendStatistics(false, event.newSession().getUniqueId().toString(), event.newSession().getUsername());
    AddonUtil.playerStatus.remove(event.previousSession().getUniqueId());

    //this.addon.chatClient().util().sendPlayerStatus(event.previousSession().getUniqueId().toString(), event.previousSession().getUsername(), true);
    //this.addon.chatClient().util().sendPlayerStatus(event.newSession().getUniqueId().toString(), event.newSession().getUsername(), false);

    //TODO: Migrate?
    //this.addon.chatClient().util().sendCheckMute(event.newSession().getUniqueId().toString());
    //this.addon.chatClient().util().sendRetrievePlayerData(event.newSession().getUniqueId().toString());

    //TODO: Move Leaderboard to Rest API
    //this.addon.chatClient().util().sendLeaderboard(event.previousSession().getUniqueId().toString(), event.previousSession().getUsername());

    this.addon.addonUtil().ranking(0);
    this.addon.addonUtil().breakGoalBlocks(0);
    this.addon.addonUtil().pickaxeRanking(0);
    this.addon.addonUtil().swordRanking(0);

  }

  @Subscribe
  public void onProfileChange(ProfileSwitchEvent event) {

    if(this.addon.configuration().resetOnProfileSwitch().get()) {
      this.addon.addonUtil().resetValues(false);
      this.addon.pushNotification(
          Component.translatable("moneymaker.notification.dataReset.profile.title", NamedTextColor.AQUA),
          Component.translatable("moneymaker.notification.dataReset.profile.text", NamedTextColor.YELLOW)
      );
    }

  }

  @Subscribe
  public void onShutdown(GameShutdownEvent event) {
    if(this.addon.moneyChatClient().isAuthenticated()) {
      this.addon.moneyChatClient().sendPacket(new PacketAddonStatistics("remove", Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), "", "", false));
      this.addon.moneyChatClient().sendPacket(new PacketPlayerStatus(
          Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), MoneyRank.USER,
          "OFFLINE", MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));
    }
    //TODO: Move Leaderboard to Rest API
    //this.addon.chatClient().util().sendLeaderboard(this.addon.labyAPI().getUniqueId().toString(), this.addon.labyAPI().getName());
    this.addon.moneyChatClient().disconnect(Initiator.SERVER, "Game Shutdown");
    if(this.addon.configuration().exportBoosterOnShutdown().get()) {
      BoosterActivity.writeLinkedListToCSV(true);
    }
  }

  private long lastMineUpdate = System.currentTimeMillis();

  @Subscribe
  public void onMineSwitch(MineSwitchEvent event) {
    if(((this.lastMineUpdate + 10*1000 - System.currentTimeMillis())) <= 0) {
      this.lastMineUpdate = System.currentTimeMillis();
      if(this.addon.moneyChatClient().isAuthenticated()) {
        this.addon.moneyChatClient().sendPacket(new PacketPlayerStatus(
            Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), MoneyRank.USER,
            Util.currentServer(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));
      }
      if(event.getNewMine() != null) {
        this.addon.sendServerUpdate("MoneyMaker » " + I18n.translate(event.getNewMine().translation()));
      }
    }
  }

  private long lastLevelUpdate = System.currentTimeMillis();
  private FarmingCave lastCave = FarmingCave.UNKNOWN;

  @Subscribe
  public void onCaveLevelChange(CaveLevelChangeEvent event) {
    if(this.lastCave == event.newCave()) return;
    this.lastCave = event.newCave();
    this.addon.addonUtil().farmingCave(event.newCave());
    if(((this.lastLevelUpdate + 10*1000 - System.currentTimeMillis())) <= 0) {
      this.lastLevelUpdate = System.currentTimeMillis();
      if(this.addon.moneyChatClient().isAuthenticated()) {
        this.addon.moneyChatClient().sendPacket(new PacketPlayerStatus(
            Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), MoneyRank.USER,
            Util.currentServer(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(), Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));
      }
      this.addon.sendServerUpdate("MoneyMaker » " + I18n.translate(event.newCave().translation()));
    }
  }

  @Subscribe
  public void onPlayerStatusUpdate(MoneyPlayerStatusEvent event) {
    UUID uuid = event.uuid();
    MoneyPlayer player = event.player();

    if(uuid.equals(this.addon.labyAPI().getUniqueId())) {
      this.addon.addonUtil().rank(player.rank());
    }

    if(AddonUtil.playerStatus.containsKey(uuid)) {
      String serverBefore = AddonUtil.playerStatus.get(uuid).server();

      // Online
      if(serverBefore.equalsIgnoreCase("Other") && (player.server().equalsIgnoreCase("Mine") || player.server().startsWith("Farming"))) {
        if(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString()) && this.addon.configuration().chatConfiguration.onlineOfflineMessages().get()) {
          if(this.addon.configuration().chatConfiguration.onlineOfflineNotifications().get() == NotificationType.LABYMOD) {
            Task.builder(() -> this.addon.pushNotification(
                Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
                Component.translatable("moneymaker.notification.chat.user.online", TextColor.color(85, 255, 85),
                    Component.text(player.rank().getChatPrefix() + player.userName())),
                Icon.head(uuid)
            )).delay(2, TimeUnit.SECONDS).build().execute();
          } else {
            this.addon.displayMessage(this.addon.prefix.copy().append(
                Component.translatable("moneymaker.notification.chat.user.online", TextColor.color(85, 255, 85),
                    Component.text(player.rank().getChatPrefix() + player.userName()))
            ));
          }
        }
      }

      // Offline
      if((serverBefore.equalsIgnoreCase("Mine") || serverBefore.startsWith("Farming")) && (player.server().equalsIgnoreCase("Other") || player.server().equals("OFFLINE"))) {
        if(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString()) && this.addon.configuration().chatConfiguration.onlineOfflineMessages().get()) {
          if(this.addon.configuration().chatConfiguration.onlineOfflineNotifications().get() == NotificationType.LABYMOD) {
            this.addon.pushNotification(
                Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
                Component.translatable("moneymaker.notification.chat.user.offline", TextColor.color(255, 85, 85),
                    Component.text(player.rank().getChatPrefix() + player.userName())),
                Icon.head(uuid)
            );
          } else {
            this.addon.displayMessage(this.addon.prefix.copy().append(
                Component.translatable("moneymaker.notification.chat.user.offline", TextColor.color(255, 85, 85),
                    Component.text(player.rank().getChatPrefix() + player.userName()))
            ));
          }
        }
      }

    }

    AddonUtil.playerStatus.put(uuid, player);

    if(player.server().equals("OFFLINE")) {
      AddonUtil.playerStatus.remove(uuid);
    }

    /*if(!player.server().equals("OFFLINE")) {
      if((AddonSettings.inMine || AddonSettings.inFarming) && !AddonSettings.playerStatus.containsKey(uuid) && this.addon.configuration().moneyChatConfiguration.onlineOfflineMessages().get() && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString())) {
        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("moneymaker.notification.chat.user.online", TextColor.color(85, 255, 85),
                Component.text(player.userName(), TextColor.color(255, 255, 85))),
            Icon.head(uuid).enableHat()
        );
      }
      AddonSettings.playerStatus.put(uuid, player);
    } else {
      if((AddonSettings.inMine || AddonSettings.inFarming) && AddonSettings.playerStatus.containsKey(uuid) && this.addon.configuration().moneyChatConfiguration.onlineOfflineMessages().get() &&
          !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString())) {
        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("moneymaker.notification.chat.user.offline", TextColor.color(255, 85, 85),
                Component.text(player.userName(), TextColor.color(255, 255, 85))),
            Icon.head(uuid).enableHat()
        );
      }
      AddonSettings.playerStatus.remove(uuid);
    }*/
  }

  @Subscribe
  public void onMoneyChatReceive(MoneyChatReceiveEvent event) {
    MoneyChatMessage chatMessage = event.chatMessage();
    this.addon.chatActivity().addChatMessage(chatMessage);
    if(!this.addon.addonUtil().connectedToMoneyMaker()) return;
    if(chatMessage.fromServerCache()) return;
    if(!chatMessage.uuid().equals(this.addon.labyAPI().getUniqueId())) {
      if(chatMessage.messageType() != MoneyChatMessageType.SERVER) {

        if(this.addon.configuration().chatConfiguration.notification().get()) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.new-message", TextColor.color(85, 255, 85)),
              Component.text(chatMessage.userName(), NamedTextColor.YELLOW).append(Component.text(": ", NamedTextColor.DARK_GRAY)).append(Component.text(chatMessage.message(), NamedTextColor.GRAY)),
              Icon.head(chatMessage.uuid()),
              Component.translatable("moneymaker.notification.chat.reply"),
              () -> this.addon.mainActivity().openAndSwitchToChat()
          );
          if(this.addon.configuration().chatConfiguration.notificationSound().get()) {
            this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);
          }
        }

      } else {

        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.chat.system-message", TextColor.color(255, 85, 85)),
            Component.text(chatMessage.message(), NamedTextColor.RED),
            Icon.sprite16(
                ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 1, 2));
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);

      }
    }
  }

}
