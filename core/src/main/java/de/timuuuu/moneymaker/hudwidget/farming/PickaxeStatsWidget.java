package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
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

@SpriteSlot(x = 1)
public class PickaxeStatsWidget extends TextHudWidget<PickaxeHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine rankLine;

  public PickaxeStatsWidget(MoneyMakerAddon addon) {
    super("mm_pickaxe_stats", PickaxeHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(PickaxeHudWidgetConfig config) {
    super.load(config);
    this.rankLine = createLine(Component.translatable("moneymaker.hudWidget.mm_pickaxe_stats.display.rank"), "0");
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
    this.rankLine.updateAndFlush(Util.format(this.addon.addonUtil().pickaxeRanking()) + additional);
    this.rankLine.setState((this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get()) && this.addon.addonUtil().pickaxeRanking() != 0 ? State.VISIBLE : State.HIDDEN);
  }


  public static class PickaxeHudWidgetConfig extends TextHudWidgetConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> showRankingDifference;

    public PickaxeHudWidgetConfig() {
      this.showRankingDifference = new ConfigProperty<>(true);
    }

    public ConfigProperty<Boolean> showRankingDifference() {
      return showRankingDifference;
    }
  }

}
