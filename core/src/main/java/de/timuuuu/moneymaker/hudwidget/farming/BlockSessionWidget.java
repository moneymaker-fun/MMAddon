package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class BlockSessionWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public BlockSessionWidget(MoneyMakerAddon addon) {
    super("mm_block_session");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(SpriteCommon.HUD_GOLD_ORE);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_block_session.name"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.textLine.updateAndFlush(Component.text(Util.format(this.addon.addonUtil().sessionBlocks()) + " ").append(Component.translatable("moneymaker.hudWidget.mm_block_session." + (this.addon.addonUtil().sessionBlocks() == 1 ? "block" : "blocks"))));
    this.textLine.setState((this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get()) && this.addon.addonUtil().sessionBlocks() > 0 ? State.VISIBLE : State.HIDDEN);
  }

}
