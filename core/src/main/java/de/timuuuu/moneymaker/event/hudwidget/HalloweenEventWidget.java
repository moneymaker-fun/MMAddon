package de.timuuuu.moneymaker.event.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class HalloweenEventWidget extends TextHudWidget<TextHudWidgetConfig> {

  public static int candies = 0;

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public HalloweenEventWidget(MoneyMakerAddon addon) {
    super("mm_event_halloween");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(SpriteCommon.HUD_CANDIES);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_event_halloween.display"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(candies));
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && candies > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
