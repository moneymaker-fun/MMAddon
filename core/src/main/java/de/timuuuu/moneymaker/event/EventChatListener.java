package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.hudwidget.ChristmasEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.EasterEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.FruitsHudWidget;
import de.timuuuu.moneymaker.event.hudwidget.HalloweenEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.ValentineEventWidget;
import de.timuuuu.moneymaker.utils.AddonUtil.MoneyMakerEvent;
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
          EasterEventWidget.eggs = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.valentine"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.valentine"), "");
        try {
          ValentineEventWidget.flowers = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.summer"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.summer"), "");
        try {
          FruitsHudWidget.fruits = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.halloween"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.halloween"), "");
        try {
          HalloweenEventWidget.candies = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

      if(plain.contains(this.addon.chatMessageLoader().message("event.christmas"))) {
        String count = plain.replace(this.addon.chatMessageLoader().message("event.now"), "")
            .replace(this.addon.chatMessageLoader().message("event.christmas"), "");
        try {
          ChristmasEventWidget.gifts = Util.parseInteger(count, this.getClass());
        } catch (NumberFormatException ignored) {}
      }

    }

    if(plain.startsWith(this.addon.chatMessageLoader().message("event.booster"))) {

      if(this.addon.addonUtil().currentEvent() == MoneyMakerEvent.VALENTINE && ValentineEventWidget.flowers >= 5) {
        ValentineEventWidget.flowers -= 5;
      }
      if(this.addon.addonUtil().currentEvent() == MoneyMakerEvent.EASTER && EasterEventWidget.eggs >= 5) {
        EasterEventWidget.eggs -= 5;
      }
      if((this.addon.addonUtil().currentEvent() == MoneyMakerEvent.SUMMER || this.addon.addonUtil().currentEvent() == MoneyMakerEvent.CARIBBEAN) && FruitsHudWidget.fruits >= 5) {
        FruitsHudWidget.fruits -= 5;
      }
      if(this.addon.addonUtil().currentEvent() == MoneyMakerEvent.HALLOWEEN && HalloweenEventWidget.candies >= 5) {
        HalloweenEventWidget.candies -= 5;
      }
      if(this.addon.addonUtil().currentEvent() == MoneyMakerEvent.CHRISTMAS && ChristmasEventWidget.gifts >= 5) {
        ChristmasEventWidget.gifts -= 5;
      }

    }

  }

}
