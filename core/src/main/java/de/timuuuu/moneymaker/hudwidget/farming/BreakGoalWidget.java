package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot(x = 1)
public class BreakGoalWidget extends ItemHudWidget<HudWidgetConfig> {

  private MoneyMakerAddon addon;

  public BreakGoalWidget(MoneyMakerAddon addon) {
    super("break_goal");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateItemName(Component.text((AddonSettings.breakGoalBlocks / 2) + " / " + AddonSettings.breakGoal), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.playingOn.contains("Farming") && AddonSettings.breakGoalEnabled && AddonSettings.breakGoal > 0;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.sprite16(
        ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 1, 0);
  }

}
