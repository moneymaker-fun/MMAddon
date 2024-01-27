package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot()
public class ActivatedBoosterWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public ActivatedBoosterWidget(MoneyMakerAddon addon) {
    super("mm_activated_boosters");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_activated_boosters.name"), "0%");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Booster.activatedBoost.get() + "%");
    this.textLine.setState((AddonSettings.inFarming || this.addon.configuration().showWidgetsAlways().get()) && Booster.activatedBoost.get() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
