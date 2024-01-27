package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot()
public class BoosterCountWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public BoosterCountWidget(MoneyMakerAddon addon) {
    super("mm_booster_count");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_booster_count.name"), "0 (0%)");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(Util.format(Booster.sessionBoosters.get()) + " (" + Util.format(Booster.sessionBoost.get()) + "%)"));
    this.textLine.setState((AddonSettings.inFarming || this.addon.configuration().showWidgetsAlways().get()) && Booster.sessionBoost.get() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
