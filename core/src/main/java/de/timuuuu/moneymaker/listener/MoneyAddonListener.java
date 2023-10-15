package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
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
    AddonSettings.resetValues();
    this.addon.chatActivity.clearChat(false);
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
            Component.text("§aNeue Chat Nachricht"), Component.text("§e" + chatMessage.userName() + "§8: §7" + chatMessage.message()),
            Icon.head(chatMessage.uuid()));
      }
      if(this.addon.configuration().chatNotificationSound().get()) {
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);
      }
    }
  }

}
