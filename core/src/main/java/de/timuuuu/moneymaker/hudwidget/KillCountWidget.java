package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class KillCountWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine rankLine;

  public KillCountWidget(MoneyMakerAddon addon) {
    super("kill_counter");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.rankLine = createLine("Kill-Count", 0);
    this.updateLines();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateLines();
  }

  private void updateLines() {
    this.rankLine.updateAndFlush(AddonSettings.sessionKills);
    this.rankLine.setState(AddonSettings.playingOn.contains("Farming") && AddonSettings.sessionKills != 0 ? State.VISIBLE : State.HIDDEN);
  }

}
