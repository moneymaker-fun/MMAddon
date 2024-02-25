package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot(x = 2)
public class DebrisTimerWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public DebrisTimerWidget(MoneyMakerAddon addon) {
    super("mm_debris_timer");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_debris_timer.name"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(this.addon.addonUtil().connectedToMoneyMaker()) {

      if(this.addon.addonUtil().debrisTime() > 0) {
        itemName = Util.intToTime(this.addon.addonUtil().debrisTime());
      }

    }
    this.textLine.updateAndFlush(itemName);
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && this.addon.addonUtil().debrisTime() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
