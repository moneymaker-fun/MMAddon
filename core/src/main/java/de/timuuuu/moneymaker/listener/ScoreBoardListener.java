package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;

public class ScoreBoardListener {

  private MoneyMakerAddon addon;

  public ScoreBoardListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void on(ScoreboardScoreUpdateEvent event) {
    if(!AddonSettings.playingOn.contains("Farming")) return;
    if(event.score().getValue() == MoneyScore.BROKEN_BLOCKS.getValue()) {
      String raw = event.score().getName().substring(2).replace(".", "");
      try {
        int blocks = Integer.parseInt(raw);
        if(AddonSettings.brokenBlocks == 0) {
          AddonSettings.brokenBlocks = blocks;
        } else {
            AddonSettings.sessionBlocks = blocks - AddonSettings.brokenBlocks;
        }
      } catch (NumberFormatException ignored) {}
    }
  }

  public enum MoneyScore {

    BROKEN_BLOCKS(3);

    private final int value;

    MoneyScore(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

}
