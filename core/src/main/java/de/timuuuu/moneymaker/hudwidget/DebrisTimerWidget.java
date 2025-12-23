package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.hudwidget.DebrisTimerWidget.DebrisTimerHudWidgetConfig;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteSlot(x = 2)
public class DebrisTimerWidget extends TextHudWidget<DebrisTimerHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine textLine;
  private TextLine realTimeLine;

  public static String realTime = "N/A";

  public DebrisTimerWidget(MoneyMakerAddon addon) {
    super("mm_debris_timer", DebrisTimerHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(DebrisTimerHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_debris_timer.name"), "0");
    this.realTimeLine = createLine(Component.translatable("moneymaker.hudWidget.mm_debris_timer.realTime"), "00:00");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(this.addon.addonUtil().connectedToMoneyMaker() && this.addon.addonUtil().debrisTime() > 0) {
      itemName = Util.intToTime(this.addon.addonUtil().debrisTime());
    }
    this.textLine.updateAndFlush(itemName);
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && this.addon.addonUtil().debrisTime() > 0 ? State.VISIBLE : State.HIDDEN);
    this.realTimeLine.updateAndFlush(realTime);
    this.realTimeLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && this.getConfig().showRealTimeEnd().get() && this.addon.addonUtil().debrisTime() > 0 ? State.VISIBLE : State.DISABLED);
  }

  public static class DebrisTimerHudWidgetConfig extends TextHudWidgetConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> showRealTimeEnd;

    public DebrisTimerHudWidgetConfig() {
      this.showRealTimeEnd = new ConfigProperty<>(true);
    }

    public ConfigProperty<Boolean> showRealTimeEnd() {
      return showRealTimeEnd;
    }

  }

}
