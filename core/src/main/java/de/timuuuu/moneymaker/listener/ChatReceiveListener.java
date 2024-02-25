package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.boosters.BoosterUtil;
import de.timuuuu.moneymaker.utils.ChatMessages;
import de.timuuuu.moneymaker.utils.Util;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
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
    String plain = event.chatMessage().getOriginalPlainText();
    if (!this.addon.addonUtil().connectedToMoneyMaker()) return;

    if (ChatMessages.PREFIX.contains(plain)) {

      if((ChatMessages.WORKPLACE_UPGRADE_DE_1.startWith(plain) && ChatMessages.WORKPLACE_UPGRADE_DE_2.contains(plain)) ||
          (ChatMessages.WORKPLACE_UPGRADE_EN.startWith(plain))) {
        if(this.addon.configuration().hideWorkerUpgradeMessage().get()) {
          event.setCancelled(true);
        }
      }

      if((ChatMessages.BUY_WORKER_DE_1.startWith(plain) && ChatMessages.BUY_WORKER_DE_2.contains(plain)) ||
          (ChatMessages.BUY_WORKER_EN.startWith(plain))) {
        if(this.addon.configuration().hideBuySellWorkerMessage().get()) {
          event.setCancelled(true);
        }
      }

      if((ChatMessages.SELL_WORKER_DE_1.startWith(plain) && ChatMessages.SELL_WORKER_DE_2.contains(plain)) ||
          (ChatMessages.SELL_WORKER_EN.startWith(plain))) {
        if(this.addon.configuration().hideBuySellWorkerMessage().get()) {
          event.setCancelled(true);
        }
      }

      if((ChatMessages.TELEPORT_DE_1.startWith(plain) && ChatMessages.TELEPORT_DE_2.contains(plain)) ||
          (ChatMessages.TELEPORT_EN.startWith(plain))) {
        if(this.addon.configuration().hideTeleportMessage().get()) {
          event.setCancelled(true);
        }
      }

      if(ChatMessages.PARTING_LINE.equals(plain) || ChatMessages.PREFIX.equals(plain)) {
        if(this.addon.configuration().hideEmptyMessages().get() || this.addon.configuration().shortBoosterMessage().get()) {
          event.setCancelled(true);
        }
      }

      if (ChatMessages.BOOSTER_FOUND_DE.equals(plain) || ChatMessages.BOOSTER_FOUND_EN.equals(plain)) {
        if(this.addon.configuration().shortBoosterMessage().get()) {
          event.setCancelled(true);
        }
      }

      if (plain.contains("Booster (") && plain.contains(")") &&
          !(ChatMessages.BOOSTER_ACTIVATED_DE_3.contains(plain) || ChatMessages.BOOSTER_ACTIVATED_EN_3.contains(plain))) {
        if(this.addon.configuration().shortBoosterMessage().get()) {
          // Shorted DE: +70 % Booster (15 Minuten)
          // Shorted EN: +40% Booster (20 minutes)
          String boosterString = plain.replace(ChatMessages.PREFIX.message() + " ", "");
          String boostString = boosterString.split("\\(")[0].replace("%", "").replace("+", "").split(" ")[0];
          String timeString = plain.split(" \\(")[1].split(" ")[0];

          Booster booster = null;

          try {
            int boost = Integer.parseInt(boostString);
            int time = Integer.parseInt(timeString);
            if (plain.contains("Stunde") || plain.contains("hour")) {
              time *= 60;
            }
            booster = new Booster(boost, time);
          } catch (NumberFormatException ignored) {}

          if(booster != null) {
            TextColor color = BoosterUtil.getColor(booster);
            Component message = Component.text(this.addon.prefix);

            Component boosterComponent = Component.text(boosterString + " ", color);
            message.append(boosterComponent);

            message.append(Component.icon(BoosterUtil.getIcon(booster), 10));

            message.append(Component.text(" ")).append(Component.translatable("moneymaker.text.found", color));
            this.addon.displayMessage(message);
          } else {
            this.addon.displayMessage(Component.text(this.addon.prefix).append(Component.text(boosterString + " ", NamedTextColor.GREEN)).append(Component.translatable("moneymaker.text.found", NamedTextColor.GREEN)));
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
          boost = Integer.parseInt(plain.split(" ")[1].replace("%", "").replace("+", ""));

          if((plain.contains("Stunde") && plain.contains("Minuten")) || (plain.contains("hour") && plain.contains("minutes"))) {
            int hours = Integer.parseInt(plain.split(" \\(")[1].split(" ")[0]);
            int minutes = Integer.parseInt(plain.split(" \\(")[1].split(" ")[2]);
            time = minutes + (hours*60);
          } else {
            time = Integer.parseInt(plain.split(" \\(")[1].split(" ")[0]);
            if(plain.contains("Stunde") || plain.contains("hour")) {
              time *= 60;
            }
          }

        } catch (NumberFormatException ignored) {

        }
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

      if(ChatMessages.WORKPLACE_UNLOCKED_DE.equals(plain) || ChatMessages.WORKPLACE_UNLOCKED_EN.equals(plain)) {
        this.addon.addonUtil().nextWorkerCost("X");
        this.addon.addonUtil().workerNotifySent(false);
      }

      if((ChatMessages.DEBRIS_REMOVE_DE_1.startWith(plain) && ChatMessages.DEBRIS_REMOVE_DE_2.contains(plain)) ||
          (ChatMessages.DEBRIS_REMOVE_EN.startWith(plain))) {
        this.addon.addonUtil().debrisCost("X");
        this.addon.addonUtil().debrisNotifySent(false);
      }

      if(ChatMessages.WORKER_EFFECT_DE.equals(plain) || ChatMessages.WORKER_EFFECT_EN.equals(plain)) {
        if(this.addon.configuration().hideEffectMessage().get()) {
          event.setCancelled(true);
        }
        if(this.addon.configuration().showTimersOnEffect().get() && !this.addon.configuration().hideEffectMessage().get()) {
          AtomicInteger timers = new AtomicInteger();
          Util.timers.values().forEach(timer -> {
            if(timer.name().contains("Effekt-Timer-")) {
              timers.getAndIncrement();
            }
          });
          TextComponent component = Component.text(this.addon.prefix).append(Component.translatable("moneymaker.text.effect-select", NamedTextColor.GRAY))
              .append(Component.text(" §8[§e5m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 5 Effekt-Timer-" + timers.get()))
                  .append(Component.text(" §8[§e10m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 10 Effekt-Timer-" + timers.get())))
                  .append(Component.text(" §8[§e15m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 15 Effekt-Timer-" + timers.get())))
                  .append(Component.text(" §8[§e20m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 20 Effekt-Timer-" + timers.get())))
              );
          Task.builder(() -> this.addon.displayMessage(component)).delay(50, TimeUnit.MILLISECONDS).build().execute();
        }
      }

      if(ChatMessages.BOOSTER_INVENTORY_DE.startWith(plain) || ChatMessages.BOOSTER_INVENTORY_EN.startWith(plain)) {
        if(this.addon.configuration().hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }
      }

      // DE: [MoneyMaker] Dein +30 % Booster (15 Minuten) wurde aktiviert
      // EN: [MoneyMaker] Your +30% booster (15 Minutes) was activated

      if(ChatMessages.BOOSTER_ACTIVATED_DE_1.contains(plain) && ChatMessages.BOOSTER_ACTIVATED_DE_2.contains(plain) &&
          ChatMessages.BOOSTER_ACTIVATED_DE_3.contains(plain)) {

        if(this.addon.configuration().hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }

        String message = plain.split(" \\(")[0]
            .replace(ChatMessages.BOOSTER_ACTIVATED_DE_1.message(), "")
            .replace(ChatMessages.BOOSTER_ACTIVATED_DE_2.message(), "");

        try {
          int boost = Integer.parseInt(message);
          Booster.activatedBoost.addAndGet(boost);
        } catch (NumberFormatException ignored) {}

      }

      if(ChatMessages.BOOSTER_ACTIVATED_EN_1.contains(plain) && ChatMessages.BOOSTER_ACTIVATED_EN_2.contains(plain) &&
          ChatMessages.BOOSTER_ACTIVATED_EN_3.contains(plain)) {

        if(this.addon.configuration().hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }

        String message = plain.split(" \\(")[0]
            .replace(ChatMessages.BOOSTER_ACTIVATED_EN_1.message(), "")
            .replace(ChatMessages.BOOSTER_ACTIVATED_DE_2.message(), "");

        try {
          int boost = Integer.parseInt(message);
          Booster.activatedBoost.addAndGet(boost);
        } catch (NumberFormatException ignored) {}

      }

    }
  }
}