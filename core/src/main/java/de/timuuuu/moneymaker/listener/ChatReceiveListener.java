package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import de.timuuuu.moneymaker.utils.ChatMessages;
import de.timuuuu.moneymaker.utils.Util;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
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
    if (!(AddonSettings.inMine || AddonSettings.inFarming)) return;

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
          String boost = plain.replace(ChatMessages.PREFIX.message(), "");
          this.addon.displayMessage(Component.text(AddonSettings.prefix).append(Component.text("§a" + boost + " ")).append(Component.translatable("moneymaker.text.found", NamedTextColor.GREEN)));
          event.setCancelled(true);
        }
      }

      // EN: [MoneyMaker] +10% Booster (20 minutes)
      // DE: [MoneyMaker] +10 % Booster (20 Minuten)

      if (plain.startsWith("[MoneyMaker] +") && plain.contains("Booster (")) {
        int boost = Integer.parseInt(plain.split(" ")[1].replace("%", "").replace("+", ""));
        Booster.sessionBoost.addAndGet(boost);
        Booster.sessionBoosters.addAndGet(1);
        int time = Integer.parseInt(plain.split(" \\(")[1].split(" ")[0]);
        if (plain.contains("Stunde") || plain.contains("hour"))
          time *= 60;
        Booster.insertBooster(boost, time);
        Booster.insertLatestBooster(boost, time);
      }

      if(ChatMessages.WORKPLACE_UNLOCKED_DE.equals(plain) || ChatMessages.WORKPLACE_UNLOCKED_EN.equals(plain)) {
        AddonSettings.nextWorkerCost = "X";
        AddonSettings.workerNotifySent = false;
      }

      if((ChatMessages.DEBRIS_REMOVE_DE_1.startWith(plain) && ChatMessages.DEBRIS_REMOVE_DE_2.contains(plain)) ||
          (ChatMessages.DEBRIS_REMOVE_EN.startWith(plain))) {
        AddonSettings.debrisCost = "X";
        AddonSettings.debrisNotifySent = false;
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
          TextComponent component = Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.text.effect-select", NamedTextColor.GRAY))
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