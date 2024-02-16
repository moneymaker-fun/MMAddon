package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.event.client.session.SessionUpdateEvent;

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
    AddonSettings.resetValues(true);

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
    this.addon.chatClient().sendStatistics(true, event.previousSession().getUniqueId().toString(), event.previousSession().getUsername());
    this.addon.chatClient().sendStatistics(false, event.newSession().getUniqueId().toString(), event.newSession().getUsername());
    AddonSettings.playerStatus.remove(event.previousSession().getUniqueId());

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
  public void onPlayerStatusUpdate(MoneyPlayerStatusEvent event) {
    UUID uuid = event.uuid();
    MoneyPlayer player = event.player();

    if(AddonSettings.playerStatus.containsKey(uuid)) {
      String serverBefore = AddonSettings.playerStatus.get(uuid).server();

      // Online
      if(serverBefore.equalsIgnoreCase("Other") && player.server().contains("MoneyMaker")) {
        if((AddonSettings.inMine || AddonSettings.inFarming) && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString()) && this.addon.configuration().moneyChatConfiguration.onlineOfflineMessages().get()) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
              Component.translatable("moneymaker.notification.chat.user.online", TextColor.color(85, 255, 85),
                  Component.text(player.userName(), TextColor.color(255, 255, 85))),
              Icon.head(uuid)
          );
        }
      }

      // Offline
      if(serverBefore.contains("MoneyMaker") && (player.server().equalsIgnoreCase("Other") || player.server().equals("OFFLINE"))) {
        if((AddonSettings.inMine || AddonSettings.inFarming) && !this.addon.labyAPI().getUniqueId().toString().equals(uuid.toString()) && this.addon.configuration().moneyChatConfiguration.onlineOfflineMessages().get()) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
              Component.translatable("moneymaker.notification.chat.user.offline", TextColor.color(255, 85, 85),
                  Component.text(player.userName(), TextColor.color(255, 255, 85))),
              Icon.head(uuid)
          );
        }
      }

    }

    AddonSettings.playerStatus.put(uuid, player);

    if(player.server().equals("OFFLINE")) {
      AddonSettings.playerStatus.remove(uuid);
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
    if(!(AddonSettings.inMine || AddonSettings.inFarming)) return;
    if(chatMessage.fromServerCache()) return;
    if(!chatMessage.uuid().equals(this.addon.labyAPI().getUniqueId())) {
      if(!chatMessage.systemMessage()) {

        if(this.addon.configuration().moneyChatConfiguration.notification().get()) {
          this.addon.pushNotification(
              Component.translatable("moneymaker.notification.chat.new-message", TextColor.color(85, 255, 85)),
              Component.text("§e" + chatMessage.userName() + "§8: §7" + chatMessage.message()),
              Icon.head(chatMessage.uuid()),
              Component.translatable("moneymaker.notification.chat.reply"),
              () -> {
                this.addon.labyAPI().minecraft().minecraftWindow().displayScreen(this.addon.mainActivity());
              }
          );
          if(this.addon.configuration().moneyChatConfiguration.notificationSound().get()) {
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
