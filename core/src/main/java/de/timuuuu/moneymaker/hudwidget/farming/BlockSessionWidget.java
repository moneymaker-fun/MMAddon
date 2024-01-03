package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class BlockSessionWidget extends ItemHudWidget<HudWidgetConfig> {

  private MoneyMakerAddon addon;

  public BlockSessionWidget(MoneyMakerAddon addon) {
    super("mm_block_session");
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
    this.updateItemName(Component.text(Util.format(AddonSettings.sessionBlocks) + " ").append(Component.translatable("moneymaker.hudWidget.mm_block_session." + (AddonSettings.sessionBlocks == 1 ? "block" : "blocks"))), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.inFarming && AddonSettings.sessionBlocks > 0;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/gold_ore.png"));
  }

}
