package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot(x = 3)
public class KillCountWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine rankLine;

  public KillCountWidget(MoneyMakerAddon addon) {
    super("mm_kill_counter");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.rankLine = createLine(Component.translatable("moneymaker.hudWidget.mm_kill_counter.display"), 0);
    this.updateLines();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateLines();
  }

  private void updateLines() {
    this.rankLine.updateAndFlush(Util.format(this.addon.addonUtil().sessionKills()));
    this.rankLine.setState((this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get()) && this.addon.addonUtil().sessionKills() != 0 ? State.VISIBLE : State.HIDDEN);
  }

}
