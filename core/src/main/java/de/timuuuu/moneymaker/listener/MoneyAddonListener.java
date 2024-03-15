package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.BoosterActivity;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.events.CaveLevelChangeEvent;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.AddonUtil.MiningCave;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.event.client.session.SessionUpdateEvent;
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
      JsonObject retrievePlayerDataObject = new JsonObject();
      retrievePlayerDataObject.addProperty("uuid", uuid);
      this.addon.chatClient().sendMessage("retrievePlayerData", retrievePlayerDataObject);

      JsonObject muteCheckObject = new JsonObject();
      muteCheckObject.addProperty("uuid", uuid);
      this.addon.chatClient().sendMessage("checkMute", muteCheckObject);

      this.addon.apiUtil().loadChatHistory();

    }
  }

  @Subscribe
  public void onDisconnect(ServerDisconnectEvent event) {
    if(event.serverData().actualAddress().matches("gommehd.net", 25565, true) ||
        event.serverData().actualAddress().matches("gommehd.fun", 25565, true) ||
        event.serverData().actualAddress().matches("moneymaker.gg", 25565, true)) {
      JsonObject leaderBoard = new JsonObject();
      leaderBoard.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
      leaderBoard.addProperty("userName", this.addon.labyAPI().getName());
      leaderBoard.addProperty("ranking", this.addon.addonUtil().ranking());
      leaderBoard.addProperty("blocks", this.addon.addonUtil().brokenBlocks());
      leaderBoard.addProperty("pickaxe_ranking", this.addon.addonUtil().pickaxeRanking());
      this.addon.chatClient().sendMessage("leaderboard", leaderBoard);
    }

    this.addon.addonUtil().resetValues(true);

    JsonObject data = new JsonObject();
    data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    data.addProperty("userName", this.addon.labyAPI().getName());
    data.addProperty("server", this.addon.chatClient().currentServer());
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient().sendMessage("playerStatus", data);

    this.addon.discordAPI().removeCustom();
    this.addon.discordAPI().removeSaved();
    this.addon.discordAPI().cancelUpdater();
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {

    if(!event.newSession().isPremium()) {
      this.addon.chatClient().closeConnection();
    } else {
      if(!this.addon.chatClient().isConnected()) {
        this.addon.chatClient().connect(true);
      }
    }

    this.addon.chatClient().sendStatistics(true, event.previousSession().getUniqueId().toString(), event.previousSession().getUsername());
    this.addon.chatClient().sendStatistics(false, event.newSession().getUniqueId().toString(), event.newSession().getUsername());
    AddonUtil.playerStatus.remove(event.previousSession().getUniqueId());

    JsonObject data = new JsonObject();
    data.addProperty("uuid", event.previousSession().getUniqueId().toString());
    data.addProperty("userName", event.previousSession().getUsername());
    data.addProperty("server", "OFFLINE");
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient().sendMessage("playerStatus", data);

    JsonObject data1 = new JsonObject();
    data1.addProperty("uuid", event.newSession().getUniqueId().toString());
    data1.addProperty("userName", event.newSession().getUsername());
    data1.addProperty("server", this.addon.chatClient().currentServer());
    data1.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient().sendMessage("playerStatus", data1);

    JsonObject muteCheckObject = new JsonObject();
    muteCheckObject.addProperty("uuid", event.newSession().getUniqueId().toString());
    this.addon.chatClient().sendMessage("checkMute", muteCheckObject);

  }

  @Subscribe
  public void onShutdown(GameShutdownEvent event) {
    this.addon.chatClient().sendStatistics(true, this.addon.labyAPI().getUniqueId().toString(), this.addon.labyAPI().getName());
    JsonObject data = new JsonObject();
    data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    data.addProperty("userName", this.addon.labyAPI().getName());
    data.addProperty("server", "OFFLINE");
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient().sendMessage("playerStatus", data);

    JsonObject leaderBoard = new JsonObject();
    leaderBoard.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
    leaderBoard.addProperty("userName", this.addon.labyAPI().getName());
    leaderBoard.addProperty("ranking", this.addon.addonUtil().ranking());
    leaderBoard.addProperty("blocks", this.addon.addonUtil().brokenBlocks());
    leaderBoard.addProperty("pickaxe_ranking", this.addon.addonUtil().pickaxeRanking());
    this.addon.chatClient().sendMessage("leaderboard", leaderBoard);

    this.addon.chatClient().closeConnection();
    if(this.addon.configuration().exportBoosterOnShutdown().get()) {
      BoosterActivity.writeLinkedListToCSV(true);
    }
  }

  private long lastLevelUpdate = System.currentTimeMillis();
  private MiningCave lastCave = MiningCave.UNKNOWN;

  @Subscribe
  public void onCaveLevelChange(CaveLevelChangeEvent event) {
    if(this.lastCave == event.newCave()) return;
    this.lastCave = event.newCave();
    this.addon.addonUtil().miningCave(event.newCave());
    if(((this.lastLevelUpdate + 10*1000 - System.currentTimeMillis())) <= 0) {
      this.lastLevelUpdate = System.currentTimeMillis();
      JsonObject data = new JsonObject();
      data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
      data.addProperty("userName", this.addon.labyAPI().getName());
      data.addProperty("server", this.addon.chatClient().currentCave(event.newCave()));
      data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
      this.addon.chatClient().sendMessage("playerStatus", data);
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
      if(serverBefore.equalsIgnoreCase("Other") && player.server().contains("MoneyMaker")) {
        if(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString()) && this.addon.configuration().chatConfiguration.onlineOfflineMessages().get()) {
          Task.builder(() -> {
            this.addon.pushNotification(
                Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
                Component.translatable("moneymaker.notification.chat.user.online", TextColor.color(85, 255, 85),
                    Component.text(player.rank().getChatPrefix() + player.userName())),
                Icon.head(uuid)
            );
          }).delay(2, TimeUnit.SECONDS).build().execute();
        }
      }

      // Offline
      if(serverBefore.contains("MoneyMaker") && (player.server().equalsIgnoreCase("Other") || player.server().equals("OFFLINE"))) {
        if(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString()) && this.addon.configuration().chatConfiguration.onlineOfflineMessages().get()) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
              Component.translatable("moneymaker.notification.chat.user.offline", TextColor.color(255, 85, 85),
                  Component.text(player.rank().getChatPrefix() + player.userName())),
              Icon.head(uuid)
          );
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
      if(!chatMessage.systemMessage()) {

        if(this.addon.configuration().chatConfiguration.notification().get()) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.new-message", TextColor.color(85, 255, 85)),
              Component.text("§e" + chatMessage.userName() + "§8: §7" + chatMessage.message()),
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
            Component.text("§c" + chatMessage.message()),
            Icon.sprite16(
                ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 1, 2));
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);

      }
    }
  }

}
