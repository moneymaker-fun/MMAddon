package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.hudwidget.ChristmasEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.EasterEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.FruitsHudWidget;
import de.timuuuu.moneymaker.event.hudwidget.HalloweenEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.ValentineEventWidget;
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

    if(EventChatMessages.NOW_DE.startWith(plain) || EventChatMessages.NOW_EN.startWith(plain)) {

      if(EventChatMessages.EASTER_DE.contains(plain) || EventChatMessages.EASTER_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.EASTER_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.EASTER_EN.message(), "");
        try {
          EasterEventWidget.eggs = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(EventChatMessages.VALENTINE_DE.contains(plain) || EventChatMessages.VALENTINE_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.VALENTINE_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.VALENTINE_EN.message(), "");
        try {
          ValentineEventWidget.flowers = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(EventChatMessages.SUMMER_DE.contains(plain) || EventChatMessages.SUMMER_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.SUMMER_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.SUMMER_EN.message(), "");
        try {
          FruitsHudWidget.fruits = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(EventChatMessages.HALLOWEEN_DE.contains(plain) || EventChatMessages.HALLOWEEN_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.HALLOWEEN_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.HALLOWEEN_EN.message(), "");
        try {
          HalloweenEventWidget.candies = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(EventChatMessages.CHRISTMAS_DE.contains(plain) || EventChatMessages.CHRISTMAS_EN.contains(plain)) {
        String count = plain.replace(EventChatMessages.NOW_DE.message(), "")
            .replace(EventChatMessages.CHRISTMAS_DE.message(), "")
            .replace(EventChatMessages.NOW_EN.message(), "")
            .replace(EventChatMessages.CHRISTMAS_EN.message(), "");
        try {
          ChristmasEventWidget.gifts = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

    }

    if(EventChatMessages.BOOSTER_DE.contains(plain) || EventChatMessages.BOOSTER_EN.contains(plain)) {

      if(this.addon.addonUtil().currentEvent().equals(EventChatMessages.EVENT_VALENTINE.message()) && ValentineEventWidget.flowers >= 5) {
        ValentineEventWidget.flowers -= 5;
      }
      if(this.addon.addonUtil().currentEvent().equals(EventChatMessages.EVENT_EASTER.message()) && EasterEventWidget.eggs >= 5) {
        EasterEventWidget.eggs -= 5;
      }
      if((this.addon.addonUtil().currentEvent().equals(EventChatMessages.EVENT_SUMMER.message()) ||
          this.addon.addonUtil().currentEvent().equals(EventChatMessages.EVENT_CARIBBEAN.message())) && FruitsHudWidget.fruits >= 5) {
        FruitsHudWidget.fruits -= 5;
      }
      if(this.addon.addonUtil().currentEvent().equals(EventChatMessages.EVENT_HALLOWEEN.message()) && HalloweenEventWidget.candies >= 5) {
        HalloweenEventWidget.candies -= 5;
      }
      if(this.addon.addonUtil().currentEvent().equals(EventChatMessages.EVENT_CHRISTMAS.message()) && ChristmasEventWidget.gifts >= 5) {
        ChristmasEventWidget.gifts -= 5;
      }

    }

  }

}
