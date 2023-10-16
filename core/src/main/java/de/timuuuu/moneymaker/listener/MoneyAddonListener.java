package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;

public class MoneyAddonListener {

  private MoneyMakerAddon addon;

  public MoneyAddonListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {
    this.addon.chatClient.sendQuitData(event.previousSession().getUniqueId().toString());
    this.addon.chatClient.sendLaunchData(event.newSession().getUniqueId().toString(), event.newSession().getUsername());
    AddonSettings.resetValues(true);
    this.addon.chatActivity.clearChat(false);
    AddonSettings.playerStatus.remove(event.previousSession().getUniqueId());

    JsonObject data = new JsonObject();
    data.addProperty("uuid", event.previousSession().getUniqueId().toString());
    data.addProperty("userName", event.previousSession().getUsername());
    data.addProperty("server", "OFFLINE");
    data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient.sendMessage("playerStatus", data);

    JsonObject data1 = new JsonObject();
    data1.addProperty("uuid", event.newSession().getUniqueId().toString());
    data1.addProperty("userName", event.newSession().getUsername());
    data1.addProperty("server", AddonSettings.playingOn.contains("MoneyMaker") ? AddonSettings.playingOn : "Other");
    data1.addProperty("addonVersion", this.addon.addonInfo().getVersion());
    this.addon.chatClient.sendMessage("playerStatus", data1);

  }

  @Subscribe
  public void onPlayerStatusUpdate(MoneyPlayerStatusEvent event) {
    UUID uuid = event.uuid();
    MoneyPlayer player = event.player();
    if(!player.server().equals("OFFLINE")) {
      AddonSettings.playerStatus.put(uuid, player);
    } else {
      AddonSettings.playerStatus.remove(uuid);
    }
  }

  @Subscribe
  public void onMoneyChatReceive(MoneyChatReceiveEvent event) {
    MoneyChatMessage chatMessage = event.chatMessage();
    this.addon.chatActivity.addChatMessage(chatMessage);
    if(!chatMessage.uuid().equals(this.addon.labyAPI().getUniqueId())) {
      if(this.addon.configuration().chatNotification().get()) {
        this.addon.pushNotification(
            Component.translatable("moneymaker.notification.new-chat-message", TextColor.color(85, 255, 85)),
            Component.text("§e" + chatMessage.userName() + "§8: §7" + chatMessage.message()),
            Icon.head(chatMessage.uuid()));
      }
      if(this.addon.configuration().chatNotificationSound().get()) {
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);
      }
    }
  }

}
