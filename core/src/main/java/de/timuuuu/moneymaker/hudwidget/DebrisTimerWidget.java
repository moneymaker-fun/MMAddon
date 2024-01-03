package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
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
    if(AddonSettings.inMine || AddonSettings.inFarming) {

      if(AddonSettings.debrisTime > 0) {
        itemName = Util.intToTime(AddonSettings.debrisTime);
      }

    }
    this.textLine.updateAndFlush(itemName);
    this.textLine.setState((AddonSettings.inMine || AddonSettings.inFarming) && AddonSettings.debrisTime > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
