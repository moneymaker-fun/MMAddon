package de.timuuuu.moneymaker.event.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class ChristmasEventWidget extends TextHudWidget<TextHudWidgetConfig> {

  public static int gifts = 0;

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public ChristmasEventWidget(MoneyMakerAddon addon) {
    super("mm_event_christmas");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(SpriteCommon.HUD_EASTER_EGG);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_event_christmas.display"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(gifts));
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && gifts > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
