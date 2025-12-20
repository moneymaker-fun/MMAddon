package de.timuuuu.moneymaker.event;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonUtil.MoneyMakerEvent;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class EventHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  public static int collectibles = 0;

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public EventHudWidget(MoneyMakerAddon addon) {
    super("mm_event");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(SpriteCommon.HUD_FRUITS);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_event.display." + this.addon.addonUtil().currentEvent().internalName()), 0);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(collectibles));
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && this.addon.addonUtil().currentEvent() != MoneyMakerEvent.NONE && collectibles > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
