package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.hudwidget.SwordStatsWidget.SwordHudWidgetConfig;
import de.timuuuu.moneymaker.utils.AddonSettings;
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
    super("sword_stats", SwordHudWidgetConfig.class);
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(SwordHudWidgetConfig config) {
    super.load(config);
    boolean showIcon = this.getConfig().showIcons().get();
    this.rankLine = createLine(
        showIcon ? Component.icon(Icon.sprite16(ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 3, 0), 10).append(Component.text(" Rang"))
            : Component.text("Rang"),
        "0"
    );
    this.mobsLine = createLine(
        showIcon ? Component.icon(Icon.sprite16(ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 3, 0), 10).append(Component.text(" Kills"))
            : Component.text("Kills"),
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
    this.rankLine.setState(AddonSettings.playingOn.contains("MoneyMaker") && !AddonSettings.swordRanking.equals("X") ? State.VISIBLE : State.HIDDEN);

    this.mobsLine.updateAndFlush(AddonSettings.swordMobs);
    this.mobsLine.setState(AddonSettings.playingOn.contains("MoneyMaker") && !AddonSettings.swordMobs.equals("X") ? State.VISIBLE : State.HIDDEN);
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
