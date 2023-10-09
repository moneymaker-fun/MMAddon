package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatUtil;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;

public class ScoreBoardListener {

  private MoneyMakerAddon addon;

  public ScoreBoardListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onScoreboardScoreUpdate(ScoreboardScoreUpdateEvent event) {
    if(!AddonSettings.playingOn.contains("MoneyMaker")) return;
    if(event.score().getValue() == MoneyScore.BROKEN_BLOCKS.score() & AddonSettings.playingOn.contains(MoneyScore.BROKEN_BLOCKS.neededServer())) {
      String raw = ChatUtil.stripColor(event.score().getName()).replace(".", "");
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
    }

  }

  public enum MoneyScore {

    BROKEN_BLOCKS(3, "Farming"),
    BALANCE(12, "MoneyMaker");

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
