package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot(x = 1)
public class BreakGoalWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public BreakGoalWidget(MoneyMakerAddon addon) {
    super("mm_break_goal");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_break_goal.name"), Component.text("0 ").append(Component.translatable("moneymaker.hudWidget.mm_break_goal.remaining")));
  }

  @Override
  public void onTick(boolean isEditorContext) {
    if(isEditorContext) {
      this.textLine.updateAndFlush(Component.text("0 ").append(Component.translatable("moneymaker.hudWidget.mm_break_goal.remaining")));
    } else {
      int blocks = AddonSettings.breakGoalBlocks - AddonSettings.currentBrokenBlocks;
      this.textLine.updateAndFlush(Component.text(blocks + " ").append(Component.translatable("moneymaker.hudWidget.mm_break_goal.remaining")));
    }
    this.textLine.setState(AddonSettings.inFarming && AddonSettings.breakGoalEnabled && AddonSettings.breakGoal > 0 ? State.VISIBLE : State.HIDDEN);
  }


}
