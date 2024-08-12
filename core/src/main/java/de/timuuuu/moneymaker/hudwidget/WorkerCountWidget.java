package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.Common;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class WorkerCountWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public WorkerCountWidget(MoneyMakerAddon addon) {
    super("mm_worker_count");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Common.MINER);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_worker_count.name"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(this.addon.addonUtil().workerCount() + " ").append(Component.translatable("moneymaker.hudWidget.mm_worker_count.miners")));
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && this.addon.addonUtil().workerCount() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
