package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

public class EventChatListener {

  private MoneyMakerAddon addon;

  public EventChatListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe(Priority.FIRST)
  public void onChatReceive(ChatReceiveEvent event) {
    if (!this.addon.addonUtil().connectedToMoneyMaker()) return;
    String plain = event.chatMessage().getOriginalPlainText();

    if(plain.startsWith(this.addon.chatMessageLoader().message("event.now"))) {

      if(plain.contains(this.addon.chatMessageLoader().message("event.easter"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.easter"), "");
        try {
          EventHudWidget.collectibles = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.valentine"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.valentine"), "");
        try {
          EventHudWidget.collectibles = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.summer"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.summer"), "");
        try {
          EventHudWidget.collectibles = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.halloween"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.halloween"), "");
        try {
          EventHudWidget.collectibles = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.christmas"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.christmas"), "");
        try {
          EventHudWidget.collectibles = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

    }

    if(plain.startsWith(this.addon.chatMessageLoader().message("event.booster")) && EventHudWidget.collectibles >= 5) {
        EventHudWidget.collectibles -= 5;
    }

  }

}
