package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatReceiveListener {

  private MoneyMakerAddon addon;

  public ChatReceiveListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe(Priority.LATEST)
  public void onChatReceive(ChatReceiveEvent event) {
    String plain = event.chatMessage().getOriginalPlainText();
    if (!AddonSettings.playingOn.contains("MoneyMaker")) return;

    if (plain.startsWith("[MoneyMaker]")) {

      if(plain.startsWith("[MoneyMaker] Du hast den Arbeitsplatz auf Level") & plain.contains("verbessert")) {
        if(this.addon.configuration().hideWorkerUpdateMessage().get()) {
          event.setCancelled(true);
        }
      }

      if((plain.contains("[MoneyMaker] Du wurdest") & plain.contains("teleportiert")) || (plain.contains("[MoneyMaker] You were teleported to"))) {
        if(this.addon.configuration().hideTeleportMessage().get()) {
          event.setCancelled(true);
        }
      }

      if (addon.configuration().shortBoosterMessage().get()) {
        if (plain.contains("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"))
          event.setCancelled(true);
        if (plain.equals("[MoneyMaker]"))
          event.setCancelled(true);
        if (plain.contains("[MoneyMaker] Glückwunsch! Du hast einen Booster gefunden:") || plain.contains("[MoneyMaker] Congratulations! You have found a booster:"))
          event.setCancelled(true);
        if (plain.contains("Booster (") && plain.contains(")")) {
          String boost = plain.replace("[MoneyMaker]", "");
          Component component = Component.text(AddonSettings.prefix + "§a" + boost + " ").append(Component.translatable("moneymaker.text.found", TextColor.color(85, 255, 85)));
          this.addon.displayMessage(component);
          event.setCancelled(true);
        }
      }

      // EN: [MoneyMaker] +10% Booster (20 minutes)
      // DE: [MoneyMaker] +10 % Booster (20 Minuten)

      if (plain.contains("[MoneyMaker] +") && plain.contains("Booster (")) {
        int boost = Integer.parseInt(plain.split(" ")[1].replace("%", "").replace("+", ""));
        Booster.sessionBoost.addAndGet(boost);
        int time = Integer.parseInt(plain.split(" \\(")[1].split(" ")[0]);
        if (plain.contains("Stunde") || plain.contains("hour"))
          time *= 60;
        Booster.insertBooster(boost, time);
        Booster.insertLatestBooster(boost, time);
      }

      if(plain.contains("[MoneyMaker] Der Arbeitsplatz wurde erfolgreich freigeschaltet") || plain.contains("[MoneyMaker] The workplace was successfully unlocked")) {
        AddonSettings.nextWorkerCost = "X";
        AddonSettings.workerNotifySent = false;
      }
      if((plain.startsWith("[MoneyMaker] Das geröll wird in") & plain.contains("entfernt")) || (plain.contains("[MoneyMaker] Debris will be removed in"))) {
        AddonSettings.debrisCost = "X";
        AddonSettings.debrisNotifySent = false;
      }

      if(plain.contains("[MoneyMaker] Du hast den Effekt dieses Arbeiters aktiviert") || plain.contains("[MoneyMaker] You have activated the effect of this worker")) {
        if(this.addon.configuration().showTimersOnEffect().get()) {
          AtomicInteger timers = new AtomicInteger();
          Util.timers.values().forEach(timer -> {
            if(timer.name().contains("Effekt-Timer-")) {
              timers.getAndIncrement();
            }
          });
          TextComponent component = Component.text(AddonSettings.prefix + "§7").append(Component.translatable("moneymaker.text.effect-select"))
              .append(Component.text(" §8[§e5m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 5 Effekt-Timer-" + timers.get()))
                  .append(Component.text(" §8[§e10m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 10 Effekt-Timer-" + timers.get())))
                  .append(Component.text(" §8[§e15m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 15 Effekt-Timer-" + timers.get())))
                  .append(Component.text(" §8[§e20m§8]").clickEvent(ClickEvent.runCommand("/mm-timer 20 Effekt-Timer-" + timers.get())))
              );
          Task.builder(() -> this.addon.displayMessage(component)).delay(50, TimeUnit.MILLISECONDS).build().execute();
        }
      }

      if(plain.contains("[MoneyMaker] Dein Booster-Inventar hat das Limit von") || plain.contains("[MoneyMaker] Your booster inventory has reached the limit of")) {
        if(this.addon.configuration().hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }
      }

      if((plain.contains("[MoneyMaker] Dein ") & plain.contains(" Booster (") & plain.contains(" wurde aktiviert")) ||
          (plain.contains("[MoneyMaker] Your ") & plain.contains(" booster (") & plain.contains(" was activated"))) {
        if(this.addon.configuration().hideFullBoosterInventory().get()) {
          event.setCancelled(true);
        }
      }

    }
  }
}