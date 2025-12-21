package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.boosters.BoosterUtil;
import de.timuuuu.moneymaker.utils.Util;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;

public class ChatReceiveListener {

  private MoneyMakerAddon addon;

  public ChatReceiveListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe(Priority.LATEST)
  public void onChatReceive(ChatReceiveEvent event) {
    if (!this.addon.addonUtil().connectedToMoneyMaker()) return;
    String plain = event.chatMessage().getOriginalPlainText();

    if (plain.contains(this.addon.chatMessageLoader().message("chat.prefix"))) {

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.workplace.upgrade"))) {
        if(this.addon.configuration().gameplayConfiguration.hideWorkerUpgradeMessage().get()) {
          event.setCancelled(true);
        }
      }

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.buyWorker.start")) && plain.contains(this.addon.chatMessageLoader().message("chat.buyWorker.contains"))) {
        if(this.addon.configuration().gameplayConfiguration.hideBuySellWorkerMessage().get()) {
          event.setCancelled(true);
        }
      }

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.sellWorker.start")) && plain.contains(this.addon.chatMessageLoader().message("chat.sellWorker.contains"))) {
        if(this.addon.configuration().gameplayConfiguration.hideBuySellWorkerMessage().get()) {
          event.setCancelled(true);
        }
      }

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.teleport.start")) && plain.contains(this.addon.chatMessageLoader().message("chat.teleport.contains"))) {
        if(this.addon.configuration().gameplayConfiguration.hideTeleportMessage().get()) {
          event.setCancelled(true);
        }
      }

      if(plain.equals(this.addon.chatMessageLoader().message("chat.spacerLine")) || plain.equals(this.addon.chatMessageLoader().message("chat.prefix"))) {
        if(this.addon.configuration().gameplayConfiguration.hideEmptyMessages().get() || this.addon.configuration().gameplayConfiguration.shortBoosterMessage().get()) {
          event.setCancelled(true);
        }
      }

      if (plain.equals(this.addon.chatMessageLoader().message("chat.booster.found"))) {
        if(this.addon.configuration().gameplayConfiguration.shortBoosterMessage().get()) {
          event.setCancelled(true);
        }
      }

      if (plain.contains("Booster (") && plain.contains(")") && !plain.contains(this.addon.chatMessageLoader().message("chat.booster.activated.3"))) {
        if(this.addon.configuration().gameplayConfiguration.shortBoosterMessage().get()) {
          // Shorted DE: +70 % Booster (15 Minuten)
          // Shorted EN: +40% Booster (20 minutes)
          String boosterString = plain.replace(this.addon.chatMessageLoader().message("chat.prefix") + " ", "");
          String boostString = boosterString.split("\\(")[0].replace("%", "").replace("+", "").split(" ")[0];
          String timeString = plain.split(" \\(")[1].split(" ")[0];

          Booster booster = null;

          try {
            int time;

            if((plain.contains("Stunde") && plain.contains("Minuten")) || (plain.contains("hour") && plain.contains("minutes"))) {
              int hours = Util.parseInteger(plain.split(" \\(")[1].split(" ")[0], this.getClass());
              int minutes = Util.parseInteger(plain.split(" \\(")[1].split(" ")[2], this.getClass());
              time = minutes + (hours*60);
            } else {
              time = Util.parseInteger(timeString, this.getClass());
              if(plain.contains("Stunde") || plain.contains("hour")) {
                time *= 60;
              }
            }
            int boost = Util.parseInteger(boostString, this.getClass());
            /*int time = Integer.parseInt(timeString);
            if (plain.contains("Stunde") || plain.contains("hour")) {
              time *= 60;
            }*/
            booster = new Booster(boost, time);
          } catch (NumberFormatException ignored) {}

          if(booster != null) {
            TextColor color = BoosterUtil.getColor(booster);
            Component message = this.addon.prefix.copy();

            Component boosterComponent = Component.text(boosterString + " ", color);
            message.append(boosterComponent);

            // Icon no longer displayed in Chat
            if(this.addon.configuration().gameplayConfiguration.showBoosterIcon().get()) {
              message.append(Component.icon(BoosterUtil.getIcon(booster), 10)).append(Component.text(" "));
            }

            message.append(Component.translatable("moneymaker.text.found", color));
            this.addon.displayMessage(message);
          } else {
            this.addon.displayMessage(this.addon.prefix.copy().append(Component.text(boosterString + " ", NamedTextColor.GREEN)).append(Component.translatable("moneymaker.text.found", NamedTextColor.GREEN)));
          }

          event.setCancelled(true);
        }
      }

      // EN: [MoneyMaker] +10% Booster (20 minutes)
      // DE: [MoneyMaker] +10 % Booster (20 Minuten)

      // [MoneyMaker] +50% Booster (1 hour 30 minutes)

      if (plain.startsWith("[MoneyMaker] +") && plain.contains("Booster (")) {
        int boost = 0;
        int time = 0;
        try {
          boost = Util.parseInteger(plain.split(" ")[1].replace("%", "").replace("+", ""), this.getClass());

          if((plain.contains("Stunde") && plain.contains("Minuten")) || (plain.contains("hour") && plain.contains("minutes"))) {
            int hours = Util.parseInteger(plain.split(" \\(")[1].split(" ")[0], this.getClass());
            int minutes = Util.parseInteger(plain.split(" \\(")[1].split(" ")[2], this.getClass());
            time = minutes + (hours*60);
          } else {
            time = Util.parseInteger(plain.split(" \\(")[1].split(" ")[0], this.getClass());
            if(plain.contains("Stunde") || plain.contains("hour")) {
              time *= 60;
            }
          }
        } catch (NumberFormatException ignored) {}
        if(boost != 0 && time != 0) {
          Booster.sessionBoost.addAndGet(boost);
          Booster.sessionBoosters.addAndGet(1);
          Booster.insertBooster(boost, time);
          Booster.insertLatestBooster(boost, time);
        }
        //int time = Integer.parseInt(plain.split(" \\(")[1].split(" ")[0]);
        //if (plain.contains("Stunde") || plain.contains("hour"))
          //time *= 60;
      }

      if(plain.equals(this.addon.chatMessageLoader().message("chat.workplace.unlocked"))) {
        this.addon.addonUtil().nextWorkerCost("X");
        this.addon.addonUtil().workerNotifySent(false);
      }

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.debris.remove.start")) && plain.contains(this.addon.chatMessageLoader().message("chat.debris.remove.contains"))) {
        Task.builder(() -> {
          this.addon.addonUtil().debrisCost("X");
          this.addon.addonUtil().debrisNotifySent(false);
        }).delay(3, TimeUnit.SECONDS).build().execute();
      }

      if(plain.equals(this.addon.chatMessageLoader().message("chat.workerEffect"))) {
        if(this.addon.configuration().gameplayConfiguration.hideEffectMessage().get()) {
          event.setCancelled(true);
        }
        if(this.addon.configuration().gameplayConfiguration.showTimersOnEffect().get() && !this.addon.configuration().gameplayConfiguration.hideEffectMessage().get()) {
          AtomicInteger timers = new AtomicInteger();
          Util.timers.values().forEach(timer -> {
            if(timer.name().contains("Effekt-Timer-")) {
              timers.getAndIncrement();
            }
          });
          Component timer5m = Component.text(" [", NamedTextColor.DARK_GRAY).append(Component.text("5m", NamedTextColor.YELLOW).append(Component.text("]", NamedTextColor.DARK_GRAY)));
          Component timer10m = Component.text(" [", NamedTextColor.DARK_GRAY).append(Component.text("10m", NamedTextColor.YELLOW).append(Component.text("]", NamedTextColor.DARK_GRAY)));
          Component timer15m = Component.text(" [", NamedTextColor.DARK_GRAY).append(Component.text("15m", NamedTextColor.YELLOW).append(Component.text("]", NamedTextColor.DARK_GRAY)));
          Component timer20m = Component.text(" [", NamedTextColor.DARK_GRAY).append(Component.text("20m", NamedTextColor.YELLOW).append(Component.text("]", NamedTextColor.DARK_GRAY)));
          Component component = this.addon.prefix.copy().append(Component.translatable("moneymaker.text.effect-select", NamedTextColor.GRAY))
              .append(timer5m.clickEvent(ClickEvent.runCommand("/mm-timer 5 Effekt-Timer-" + timers.get()))
              .append(timer10m.clickEvent(ClickEvent.runCommand("/mm-timer 10 Effekt-Timer-" + timers.get())))
              .append(timer15m.clickEvent(ClickEvent.runCommand("/mm-timer 15 Effekt-Timer-" + timers.get())))
              .append(timer20m.clickEvent(ClickEvent.runCommand("/mm-timer 20 Effekt-Timer-" + timers.get())))
              );
          Task.builder(() -> this.addon.displayMessage(component)).delay(50, TimeUnit.MILLISECONDS).build().execute();
        }
      }

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.booster.fullInventory"))) {
        if(this.addon.configuration().gameplayConfiguration.hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }
      }

      // DE: [MoneyMaker] Dein +30 % Booster (15 Minuten) wurde aktiviert
      // EN: [MoneyMaker] Your +30% booster (15 Minutes) was activated

      if(plain.startsWith(this.addon.chatMessageLoader().message("chat.booster.activated.1")) && plain.contains(this.addon.chatMessageLoader().message("chat.booster.activated.2")) &&
          plain.contains(this.addon.chatMessageLoader().message("chat.booster.activated.3"))) {

        if(this.addon.configuration().gameplayConfiguration.hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }

        String message = plain.split(" \\(")[0]
            .replace(this.addon.chatMessageLoader().message("chat.booster.activated.1"), "")
            .replace(this.addon.chatMessageLoader().message("chat.booster.activated.2"), "");

        try {
          int boost = Util.parseInteger(message, this.getClass());
          Booster.activatedBoost.addAndGet(boost);
        } catch (NumberFormatException ignored) {}

      }

      /*if(ChatMessages.BOOSTER_ACTIVATED_EN_1.contains(plain) && ChatMessages.BOOSTER_ACTIVATED_EN_2.contains(plain) &&
          ChatMessages.BOOSTER_ACTIVATED_EN_3.contains(plain)) {

        if(this.addon.configuration().gameplayConfiguration.hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }

        String message = plain.split(" \\(")[0]
            .replace(ChatMessages.BOOSTER_ACTIVATED_EN_1.message(), "")
            .replace(ChatMessages.BOOSTER_ACTIVATED_DE_2.message(), "");

        try {
          int boost = Util.parseInteger(message, this.getClass());
          Booster.activatedBoost.addAndGet(boost);
        } catch (NumberFormatException ignored) {}

      }*/

    }
  }
}