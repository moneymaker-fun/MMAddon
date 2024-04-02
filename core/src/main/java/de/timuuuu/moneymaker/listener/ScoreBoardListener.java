package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.utils.ChatMessages;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;

import static de.timuuuu.moneymaker.utils.Util.convertToReadableFormat;

public class ScoreBoardListener {

  private MoneyMakerAddon addon;

  public ScoreBoardListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onScoreboardScoreUpdate(ScoreboardScoreUpdateEvent event) {
    if(!this.addon.addonUtil().connectedToMoneyMaker()) return;

    if(event.score().getValue() == MoneyScore.BROKEN_BLOCKS.score() && this.addon.addonUtil().inFarming()) {
      String raw = ChatUtil.stripColor(event.score().getName()).replace(".", "").replace(",", "");
      try {
        int blocks = Integer.parseInt(raw);
        this.addon.addonUtil().currentBrokenBlocks(blocks);
        if(this.addon.addonUtil().brokenBlocks() == 0) {
          this.addon.addonUtil().brokenBlocks(blocks);
        } else {
            this.addon.addonUtil().sessionBlocks(blocks - this.addon.addonUtil().brokenBlocks());
            if(this.addon.addonSettings().breakGoalEnabled() && this.addon.addonSettings().breakGoal() != 0) {

              if(this.addon.addonUtil().breakGoalBlocks() == 0) {
                this.addon.addonUtil().breakGoalBlocks(blocks + this.addon.addonSettings().breakGoal());
              }

              if(blocks == this.addon.addonUtil().breakGoalBlocks()) {
                this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
                String readableTimeDifference = convertToReadableFormat(System.currentTimeMillis() - this.addon.addonUtil().startTimestamp());
                this.addon.pushNotification(Component.translatable("moneymaker.notification.break-goal.title", TextColor.color(255, 255, 85)),
                    Component.translatable("moneymaker.notification.break-goal.text", TextColor.color(170, 170, 170),
                        Component.text(readableTimeDifference, TextColor.color(255, 255, 85))));
                this.addon.addonSettings().breakGoalEnabled(false);
                this.addon.addonSettings().breakGoal(0);
                this.addon.addonUtil().breakGoalBlocks(0);
              }

            }
        }
      } catch (NumberFormatException ignored) {}
    }

    if(event.score().getValue() == MoneyScore.PICKAXE_LEVEL.score() && this.addon.addonUtil().inFarming()) {
      try {
        this.addon.addonUtil().pickaxeLevel(Integer.parseInt(ChatUtil.stripColor(event.score().getName())));
      } catch (NumberFormatException ignored) {}
    }

    if(event.score().getValue() == MoneyScore.PICKAXE_RANKING.score() && this.addon.addonUtil().inFarming()) {
      String scoreName = ChatUtil.stripColor(event.score().getName());
      if(ChatMessages.SB_PLACE_DE.startWith(scoreName) || ChatMessages.SB_PLACE_EN.startWith(scoreName)) {
        this.addon.addonUtil().pickaxeRanking(Integer.parseInt(scoreName
            .replace(ChatMessages.SB_PLACE_DE.message() + " ", "")
            .replace(ChatMessages.SB_PLACE_EN.message() + " ", "")
            .replace(".", "").replace(",", "")
          ));
      }
    }

    if(event.score().getValue() == MoneyScore.RANK.score() && this.addon.addonUtil().connectedToMoneyMaker()) {
      String scoreName = ChatUtil.stripColor(event.score().getName());
      if(ChatMessages.SB_PLACE_DE.startWith(scoreName) || ChatMessages.SB_PLACE_EN.startWith(scoreName)) {
        this.addon.addonUtil().ranking(Integer.parseInt(scoreName.
            replace(ChatMessages.SB_PLACE_DE.message() + " ", "")
            .replace(ChatMessages.SB_PLACE_EN.message() + " ", "")
            .replace(".", "").replace(",", "")
        ));
      }
    }

    if(event.score().getValue() == MoneyScore.BALANCE.score() && this.addon.addonUtil().connectedToMoneyMaker()) {
      this.addon.addonUtil().balance(ChatUtil.stripColor(event.score().getName()));

      try {
        String[] balSplit = this.addon.addonUtil().balance().replace(".", "").split(" ");
        if(balSplit.length == 1) return;
        int balance = Integer.parseInt(balSplit[0]);
        String balEinheit = balSplit[1];

        if(!this.addon.addonUtil().nextWorkerCost().equals("X") && !this.addon.addonUtil().workerNotifySent()) {
          String[] workerSplit = this.addon.addonUtil().nextWorkerCost().replace(".", "").split(" ");
          int workerCost = Integer.parseInt(workerSplit[0]);
          String workerEinheit = workerSplit[1];
          if(CurrencyUtil.get(balEinheit) >= CurrencyUtil.get(workerEinheit) && balance >= workerCost) {
            if(this.addon.configuration().gameplayConfiguration.notifyOnMoneyReached().get()) {
              this.addon.addonUtil().workerNotifySent(true);
              this.addon.pushNotification(Component.translatable("moneymaker.notification.balance-reached.miner.title", TextColor.color(85, 255, 85)),
                  Component.translatable("moneymaker.notification.balance-reached.miner.text", TextColor.color(170, 170, 170)));
              this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
            }
          }
        }

        if(!this.addon.addonUtil().debrisCost().equals("X") && this.addon.addonUtil().nextWorkerCost().equals("X") && !this.addon.addonUtil().debrisNotifySent()) {
          String[] debrisSplit = this.addon.addonUtil().debrisCost().replace(".", "").split(" ");
          int debrisCost = Integer.parseInt(debrisSplit[0]);
          String debrisEinheit = debrisSplit[1];
          if(CurrencyUtil.get(balEinheit) >= CurrencyUtil.get(debrisEinheit) && balance >= debrisCost) {
            if(this.addon.configuration().gameplayConfiguration.notifyOnMoneyReached().get()) {
              this.addon.addonUtil().debrisNotifySent(true);
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

    BROKEN_BLOCKS(3),
    PICKAXE_LEVEL(6),
    PICKAXE_RANKING(0),

    RANK(9),

    BALANCE(12);

    private final int score;

    MoneyScore(int score) {
      this.score = score;
    }

    public int score() {
      return score;
    }

  }

}
