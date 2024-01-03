package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class BlockSessionWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public BlockSessionWidget(MoneyMakerAddon addon) {
    super("mm_block_session");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/gold_ore.png")));
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_block_session.name"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(Util.format(AddonSettings.sessionBlocks) + " ").append(Component.translatable("moneymaker.hudWidget.mm_block_session." + (AddonSettings.sessionBlocks == 1 ? "block" : "blocks"))));
    this.textLine.setState(AddonSettings.inFarming && AddonSettings.sessionBlocks > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
