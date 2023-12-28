package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;
import net.labymod.api.util.concurrent.task.Task;
import java.util.concurrent.TimeUnit;

public class ScoreBoardListener {

  private boolean langWarningSent = false;

  private MoneyMakerAddon addon;

  public ScoreBoardListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onScoreboardScoreUpdate(ScoreboardScoreUpdateEvent event) {
    if(!this.addon.configuration().enabled().get()) return;
    if(!AddonSettings.playingOn.contains("MoneyMaker")) return;

    if(event.score().getValue() == MoneyScore.LANG_CHECK.score() & AddonSettings.playingOn.contains(MoneyScore.LANG_CHECK.neededServer())) {
      AddonSettings.languageSupported = event.score().getName().contains("Kontostand") || event.score().getName().contains("Balance");
      Task.builder(() -> {
        if(!AddonSettings.languageSupported & !langWarningSent) {
          langWarningSent = true;
          //TODO: Make message as translation
          this.addon.displayMessage(" ");
          this.addon.displayMessage(Component.translatable("moneymaker.text.languageNotSupported.line1",
              NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD));
          this.addon.displayMessage(Component.translatable("moneymaker.text.languageNotSupported.line2", NamedTextColor.GRAY));
          this.addon.displayMessage(" ");
          //this.addon.displayMessage("§4§lℹ You are using a language that is §nNOT§4§l supported by the MoneyMaker-Addon ℹ");
          //this.addon.displayMessage("§7Please go into the lobby and change your language to 'German' or 'English'.");
        }
      }).delay(5, TimeUnit.SECONDS).build().execute();
    }

    if(event.score().getValue() == MoneyScore.BROKEN_BLOCKS.score() & AddonSettings.playingOn.contains(MoneyScore.BROKEN_BLOCKS.neededServer())) {
      String raw = ChatUtil.stripColor(event.score().getName()).replace(".", "").replace(",", "");
      try {
        int blocks = Integer.parseInt(raw);
        AddonSettings.currentBrokenBlocks = blocks;
        if(AddonSettings.brokenBlocks == 0) {
          AddonSettings.brokenBlocks = blocks;
        } else {
            AddonSettings.sessionBlocks = blocks - AddonSettings.brokenBlocks;
            if(AddonSettings.breakGoalEnabled && AddonSettings.breakGoal != 0) {

              if(AddonSettings.breakGoalBlocks == 0) {
                AddonSettings.breakGoalBlocks = blocks + AddonSettings.breakGoal;
              }

              if(blocks == AddonSettings.breakGoalBlocks) {
                this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
                this.addon.pushNotification(Component.translatable("moneymaker.notification.break-goal.title", TextColor.color(255, 255, 85)),
                    Component.translatable("moneymaker.notification.break-goal.text", TextColor.color(85, 255, 85)));
                AddonSettings.breakGoalEnabled = false;
                AddonSettings.breakGoal = 0;
                AddonSettings.breakGoalBlocks = 0;
              }

            }
        }
      } catch (NumberFormatException ignored) {}
    }

    if(event.score().getValue() == MoneyScore.PICKAXE_LEVEL.score() & AddonSettings.playingOn.contains(MoneyScore.PICKAXE_LEVEL.neededServer())) {
      AddonSettings.pickaxeLevel = ChatUtil.stripColor(event.score().getName());
    }

    if(event.score().getValue() == MoneyScore.PICKAXE_RANKING.score() & AddonSettings.playingOn.contains(MoneyScore.PICKAXE_RANKING.neededServer())) {
      AddonSettings.pickaxeRanking = ChatUtil.stripColor(event.score().getName()).replace("Platz ", "").replace("Rank ", "");
    }

    if(event.score().getValue() == MoneyScore.RANK.score() & AddonSettings.playingOn.contains(MoneyScore.RANK.neededServer())) {
      String scoreName = ChatUtil.stripColor(event.score().getName());
      if(scoreName.startsWith("Rank") || scoreName.startsWith("Platz")) {
        AddonSettings.rank = scoreName.replace("Rank ", "").replace("Platz ", "");
      }
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
              this.addon.pushNotification(Component.translatable("moneymaker.notification.balance-reached.miner.title", TextColor.color(85, 255, 85)),
                  Component.translatable("moneymaker.notification.balance-reached.miner.text", TextColor.color(170, 170, 170)));
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
              this.addon.pushNotification(Component.translatable("moneymaker.notification.balance-reached.debris.title", TextColor.color(85, 255, 85)),
                  Component.translatable("moneymaker.notification.balance-reached.debris.text", TextColor.color(170, 170, 170)));
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
    PICKAXE_LEVEL(6, "Farming"),
    PICKAXE_RANKING(0, "Farming"),

    RANK(9, "MoneyMaker"),

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
