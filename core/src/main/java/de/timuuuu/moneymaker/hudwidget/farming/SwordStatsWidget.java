package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.hudwidget.farming.SwordStatsWidget.SwordHudWidgetConfig;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteSlot(x = 3)
public class SwordStatsWidget extends TextHudWidget<SwordHudWidgetConfig> {

  private MoneyMakerAddon addon;

  private TextLine rankLine;
  private TextLine mobsLine;

  public SwordStatsWidget(MoneyMakerAddon addon) {
    super("mm_sword_stats", SwordHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(SwordHudWidgetConfig config) {
    super.load(config);
    boolean showIcon = this.getConfig().showIcons().get();
    this.rankLine = createLine(
        showIcon ? Component.icon(Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/gold_sword.png")), 10).append(Component.translatable("moneymaker.hudWidget.mm_sword_stats.with-icon.rank"))
            : Component.translatable("moneymaker.hudWidget.mm_sword_stats.without-icon.rank"),
        "0"
    );
    this.mobsLine = createLine(
        showIcon ? Component.icon(Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/gold_sword.png")), 10).append(Component.translatable("moneymaker.hudWidget.mm_sword_stats.with-icon.kills"))
            : Component.translatable("moneymaker.hudWidget.mm_sword_stats.without-icon.kills"),
        "0"
    );
    this.updateLines();
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateLines();
  }

  private void updateLines() {
    this.rankLine.updateAndFlush(AddonSettings.swordRanking);
    this.rankLine.setState(AddonSettings.inFarming && !AddonSettings.swordRanking.equals("X") ? State.VISIBLE : State.HIDDEN);

    this.mobsLine.updateAndFlush(AddonSettings.swordMobs);
    this.mobsLine.setState(AddonSettings.inFarming && !AddonSettings.swordMobs.equals("X") ? State.VISIBLE : State.HIDDEN);
  }


  public static class SwordHudWidgetConfig extends TextHudWidgetConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> showIcons;

    public SwordHudWidgetConfig() {
      this.showIcons = new ConfigProperty<>(true);
    }

    public ConfigProperty<Boolean> showIcons() {
      return showIcons;
    }
  }

}
