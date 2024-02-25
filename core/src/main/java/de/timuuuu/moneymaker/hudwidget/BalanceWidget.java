package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class BalanceWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public BalanceWidget(MoneyMakerAddon addon) {
    super("mm_balance");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(SpriteCommon.HUD_COIN);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_balance.name"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(this.addon.addonUtil().balance()));
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.addonUtil().balance().equals("X") ? State.VISIBLE : State.HIDDEN);
  }

}
