package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatUtil;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;

public class ScoreBoardListener {

  private boolean langWarningSent = false;

  private MoneyMakerAddon addon;

  public ScoreBoardListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onScoreboardScoreUpdate(ScoreboardScoreUpdateEvent event) {
    if(!AddonSettings.playingOn.contains("MoneyMaker")) return;

    if(event.score().getValue() == MoneyScore.LANG_CHECK.score() & AddonSettings.playingOn.contains(MoneyScore.LANG_CHECK.neededServer())) {
      AddonSettings.languageSupported = event.score().getName().contains("Kontostand") || event.score().getName().contains("Balance");
      if(!AddonSettings.languageSupported & !langWarningSent) {
        langWarningSent = true;
        this.addon.displayMessage("§4§lℹ Du benutzt eine Sprache, die vom MoneyMaker-Addon §nNICHT §4§lunterstützt wird! ℹ");
        this.addon.displayMessage("§7Bitte gehe in die Lobby und stelle die Sprach auf 'Deutsch' oder 'Englisch'.");
        this.addon.displayMessage(" ");
        this.addon.displayMessage("§4§lℹ You are using a language that is §nNOT §4§lsupported by the MoneyMaker-Addon ℹ");
        this.addon.displayMessage("§7Please go into the lobby and change your language to 'German' or 'English'.");
      }
    }

    if(event.score().getValue() == MoneyScore.BROKEN_BLOCKS.score() & AddonSettings.playingOn.contains(MoneyScore.BROKEN_BLOCKS.neededServer())) {
      String raw = ChatUtil.stripColor(event.score().getName()).replace(".", "").replace(",", "");
      try {
        int blocks = Integer.parseInt(raw);
        if(AddonSettings.brokenBlocks == 0) {
          AddonSettings.brokenBlocks = blocks;
        } else {
            AddonSettings.sessionBlocks = blocks - AddonSettings.brokenBlocks;
            if(AddonSettings.breakGoalEnabled & AddonSettings.breakGoal != 0) {
              AddonSettings.breakGoalBlocks++;
              if(AddonSettings.breakGoal == (AddonSettings.breakGoalBlocks / 2)) {
                this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
                this.addon.pushNotification(Component.text("§eAbbau Ziel"), Component.text("§aDu hast dein Abbau Ziel erreicht! §7Das Abbau Zeil wurde deaktiviert."));
                AddonSettings.breakGoalEnabled = false;
                AddonSettings.breakGoal = 0;
                AddonSettings.breakGoalBlocks = 0;
              }
            }
        }
      } catch (NumberFormatException ignored) {}
    }

    if(event.score().getValue() == MoneyScore.BALANCE.score() & AddonSettings.playingOn.contains(MoneyScore.BALANCE.neededServer())) {
      AddonSettings.balance = ChatUtil.stripColor(event.score().getName());

      try {
        String[] balSplit = AddonSettings.balance.replace(".", "").split(" ");
        if(balSplit.length == 1) return;
        int balance = Integer.parseInt(balSplit[0]);
        String balEinheit = balSplit[1];

        if(!AddonSettings.nextWorkerCost.equals("X") && !AddonSettings.workerNotifySent) {
          String[] workerSplit = AddonSettings.nextWorkerCost.replace(".", "").split(" ");
          int workerCost = Integer.parseInt(workerSplit[0]);
          String workerEinheit = workerSplit[1];
          if(CurrencyUtil.units.get(balEinheit) >= CurrencyUtil.units.get(workerEinheit) && balance >= workerCost) {
            if(this.addon.configuration().notifyOnMoneyReached().get()) {
              AddonSettings.workerNotifySent = true;
              this.addon.pushNotification(Component.text("§aGeld für Arbeiter erreicht!"),
                  Component.text("§7Du hast das Geld für den nächsten Minenarbeiter erreicht."));
              this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
            }
          }
        }

        if(!AddonSettings.debrisCost.equals("X") && !AddonSettings.debrisNotifySent) {
          String[] debrisSplit = AddonSettings.debrisCost.replace(".", "").split(" ");
          int debrisCost = Integer.parseInt(debrisSplit[0]);
          String debrisEinheit = debrisSplit[1];
          if(CurrencyUtil.units.get(balEinheit) >= CurrencyUtil.units.get(debrisEinheit) && balance >= debrisCost) {
            if(this.addon.configuration().notifyOnMoneyReached().get()) {
              AddonSettings.debrisNotifySent = true;
              this.addon.pushNotification(Component.text("§aGeld für Geröll erreicht!"),
                  Component.text("§7Du hast das Geld zum entfernen des Gerölls erreicht."));
              this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
            }
          }
        }

      } catch (NumberFormatException ignored) {

      }

    }

  }

  public enum MoneyScore {

    BROKEN_BLOCKS(3, "Farming"),
    BALANCE(12, "MoneyMaker"),

    LANG_CHECK(13, "MoneyMaker");

    private final int score;
    private final String neededServer;

    MoneyScore(int score, String neededServer) {
      this.score = score;
      this.neededServer = neededServer;
    }

    public int score() {
      return score;
    }

    public String neededServer() {
      return neededServer;
    }
  }

}
