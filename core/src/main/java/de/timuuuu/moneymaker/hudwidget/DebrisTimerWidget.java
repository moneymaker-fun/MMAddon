package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot(x = 2)
public class DebrisTimerWidget extends ItemHudWidget<HudWidgetConfig> {

  private MoneyMakerAddon addon;

  public DebrisTimerWidget(MoneyMakerAddon addon) {
    super("mm_debris_timer");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(AddonSettings.playingOn.contains("MoneyMaker")) {

      if(AddonSettings.debrisTime > 0) {
        itemName = Util.intToTime(AddonSettings.debrisTime);
      }

    }

    this.updateItemName(Component.text(itemName), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.playingOn.contains("MoneyMaker") && AddonSettings.debrisTime > 0;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.sprite16(
        ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 2, 0);
  }

}
