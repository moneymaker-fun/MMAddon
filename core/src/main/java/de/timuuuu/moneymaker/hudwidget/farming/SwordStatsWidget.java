package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.hudwidget.farming.SwordStatsWidget.SwordHudWidgetConfig;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteSlot(x = 3)
public class SwordStatsWidget extends TextHudWidget<SwordHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine rankLine;
  private TextLine mobsLine;

  public SwordStatsWidget(MoneyMakerAddon addon) {
    super("mm_sword_stats", SwordHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(SwordHudWidgetConfig config) {
    super.load(config);
    this.rankLine = createLine(Component.translatable("moneymaker.hudWidget.mm_sword_stats.display.rank"), "0");
    this.mobsLine = createLine(Component.translatable("moneymaker.hudWidget.mm_sword_stats.display.kills"), "0");
    this.updateLines();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateLines();
  }

  private void updateLines() {
    String additional = "";
    if(this.getConfig().showRankingDifference().get() && AddonSettings.savedSwordRanking != 0) {
      int diff = AddonSettings.savedSwordRanking - AddonSettings.swordRanking;
      additional = " (" + (diff == 0 ? "∓ " : diff > 0 ? "↑ " : "↓ ") + diff + ")";
    }
    this.rankLine.updateAndFlush(Util.format(AddonSettings.swordRanking) + additional);
    this.rankLine.setState((AddonSettings.inFarming || this.addon.configuration().showWidgetsAlways().get()) && AddonSettings.swordRanking != 0 ? State.VISIBLE : State.HIDDEN);

    this.mobsLine.updateAndFlush(Util.format(AddonSettings.swordMobs));
    this.mobsLine.setState((AddonSettings.inFarming || this.addon.configuration().showWidgetsAlways().get()) && AddonSettings.swordMobs != 0 ? State.VISIBLE : State.HIDDEN);
  }


  public static class SwordHudWidgetConfig extends TextHudWidgetConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> showRankingDifference;

    public SwordHudWidgetConfig() {
      this.showRankingDifference = new ConfigProperty<>(false);
    }

    public ConfigProperty<Boolean> showRankingDifference() {
      return showRankingDifference;
    }
  }

}
