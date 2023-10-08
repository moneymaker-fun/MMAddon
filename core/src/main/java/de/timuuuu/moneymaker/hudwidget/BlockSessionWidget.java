package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class BlockSessionWidget extends ItemHudWidget<HudWidgetConfig> {

  private MoneyMakerAddon addon;

  public BlockSessionWidget(MoneyMakerAddon addon) {
    super("block_session");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/gold_ore.png")));
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateItemName(Component.text(AddonSettings.sessionBlocks + " Blöcke"), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.playingOn.contains("MoneyMaker") && AddonSettings.sessionBlocks > 0;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/gold_ore.png"));
  }

}
