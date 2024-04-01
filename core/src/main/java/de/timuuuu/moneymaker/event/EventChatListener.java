package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.hudwidget.EasterEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.ValentineEventWidget;
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

    if(EventChatMessages.NOW_DE.startWith(plain) || EventChatMessages.NOW_EN.startWith(plain)) {

      if(EventChatMessages.EASTER_DE.contains(plain) || EventChatMessages.EASTER_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.EASTER_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.EASTER_EN.message(), "");
        try {
          EasterEventWidget.eggs = Integer.parseInt(count);
        } catch (NumberFormatException ignored) {}
      }

      if(EventChatMessages.VALENTINE_DE.contains(plain) || EventChatMessages.VALENTINE_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.VALENTINE_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.VALENTINE_EN.message(), "");
        try {
          ValentineEventWidget.flowers = Integer.parseInt(count);
        } catch (NumberFormatException ignored) {}
      }

    }

    if(EventChatMessages.BOOSTER_DE.contains(plain) || EventChatMessages.BOOSTER_EN.contains(plain)) {
      if(this.addon.addonUtil().currentEvent().equals("EASTER")) {
        if(EasterEventWidget.eggs >= 5) {
          EasterEventWidget.eggs -=5;
        }
      }
    }

  }

}
