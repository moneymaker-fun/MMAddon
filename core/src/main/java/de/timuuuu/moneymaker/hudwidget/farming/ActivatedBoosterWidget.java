package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class ActivatedBoosterWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine textLine;

  public ActivatedBoosterWidget(MoneyMakerAddon addon) {
    super("mm_activated_boosters");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine("Aktivierte Booster", "0%");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    updateLine();
  }

  private void updateLine() {
    this.textLine.updateAndFlush(Booster.activatedBoost.get() + "%");
    this.textLine.setState(AddonSettings.playingOn.contains("Farming") && Booster.activatedBoost.get() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
