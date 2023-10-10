package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

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

      if(this.addon.configuration().hideWorkerUpdateMessage().get()) {
        if(plain.startsWith("[MoneyMaker] Du hast den Arbeitsplatz auf Level") & plain.contains("verbessert")) {
          event.setCancelled(true);
        }
      }

      if (addon.configuration().shortBoosterMessage().get()) {
        if (plain.contains("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"))
          event.setCancelled(true);
        if (plain.equals("[MoneyMaker]"))
          event.setCancelled(true);
        if (plain.contains("[MoneyMaker] Glückwunsch! Du hast einen Booster gefunden:"))
          event.setCancelled(true);
        if (plain.contains("Booster (") && plain.contains(".")) {
          String boost = plain.replace("[MoneyMaker]", "");
          this.addon.displayMessage(boost + " gefunden.");
          event.setCancelled(true);
        }
      }

      if (plain.contains("[MoneyMaker] +") && plain.contains("Booster (")) {
        int boost = Integer.parseInt(plain.split(" ")[1].substring(1));
        Booster.sessionBoost.addAndGet(boost);
        int time = Integer.parseInt(plain.split(" ")[4].substring(1));
        if (plain.contains("Stunde"))
          time *= 60;
        Booster.insertBooster(boost, time);
      }

      if(plain.contains("[MoneyMaker] Der Arbeitsplatz wurde erfolgreich freigeschaltet")) {
        AddonSettings.nextWorkerCost = "X";
      }
      if (plain.startsWith("[MoneyMaker] Das geröll wird in") && plain.contains("entfernt")) {
        AddonSettings.debrisCost = "X";
      }

    }
  }
}