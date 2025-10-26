package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.hudwidget.farming.PickaxeStatsWidget.PickaxeHudWidgetConfig;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@SpriteSlot(x = 1)
public class PickaxeStatsWidget extends TextHudWidget<PickaxeHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine rankLine;
  private TextLine chanceLine;

  DecimalFormat decimalFormat = new DecimalFormat("#.####");

  public PickaxeStatsWidget(MoneyMakerAddon addon) {
    super("mm_pickaxe_stats", PickaxeHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    decimalFormat.setRoundingMode(RoundingMode.CEILING);
  }

  @Override
  public void load(PickaxeHudWidgetConfig config) {
    super.load(config);
    this.rankLine = createLine(Component.translatable("moneymaker.hudWidget.mm_pickaxe_stats.display.rank"), "0");
    this.chanceLine = createLine(Component.translatable("moneymaker.hudWidget.mm_pickaxe_stats.display.chance"), "0");
    this.updateLines();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateLines();
  }

  private void updateLines() {
    String additional = "";
    if(this.getConfig().showRankingDifference().get() && this.addon.addonUtil().savedPickaxeRanking() != 0) {
      int diff = this.addon.addonUtil().savedPickaxeRanking() - this.addon.addonUtil().pickaxeRanking();
      additional = " (" + (diff == 0 ? "" : diff > 0 ? "↑ " : "↓ ") + diff + ")";
    }
    String pickaxeRanking = this.addon.addonUtil().savedPickaxeRanking() != 0 ? Util.format(this.addon.addonUtil().pickaxeRanking()) : this.addon.addonUtil().savedPickaxeRankingString();
    this.rankLine.updateAndFlush(pickaxeRanking + additional);
    this.rankLine.setState((this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get()) && (this.addon.addonUtil().pickaxeRanking() != 0 || !this.addon.addonUtil().savedPickaxeRankingString().isEmpty()) ? State.VISIBLE : State.HIDDEN);

    String averageChance = "";
    if(this.getConfig().showAverageChance().get() && Booster.sessionBoosters.get() > 0 && this.addon.addonUtil().sessionBlocks() > 0) {
      float change = ((float) Booster.sessionBoosters.get() / this.addon.addonUtil().sessionBlocks()) * 100;
      averageChance = " (" + decimalFormat.format(change) + " %)";
    }
    this.chanceLine.updateAndFlush(this.addon.addonUtil().pickaxeBoosterChance() + averageChance);
    this.chanceLine.setState((this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get()) && !this.addon.addonUtil().pickaxeBoosterChance().isEmpty() ? State.VISIBLE : State.HIDDEN);
  }


  public static class PickaxeHudWidgetConfig extends TextHudWidgetConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> showRankingDifference;

    @SwitchSetting
    private final ConfigProperty<Boolean> showAverageChance;

    public PickaxeHudWidgetConfig() {
      this.showRankingDifference = new ConfigProperty<>(true);
      this.showAverageChance = new ConfigProperty<>(true);
    }

    public ConfigProperty<Boolean> showRankingDifference() {
      return showRankingDifference;
    }

    public ConfigProperty<Boolean> showAverageChance() {
      return showAverageChance;
    }

  }

}
