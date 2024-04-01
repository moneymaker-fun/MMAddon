package de.timuuuu.moneymaker.event.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class FruitsHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  public static int fruits = 0;

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public FruitsHudWidget(MoneyMakerAddon addon) {
    super("mm_event_fruits");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(SpriteCommon.HUD_FRUITS);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_event_fruits.display"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(fruits));
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && fruits > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
