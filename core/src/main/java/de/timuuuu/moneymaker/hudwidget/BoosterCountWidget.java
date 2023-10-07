package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.ExampleAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class BoosterCountWidget extends ItemHudWidget<HudWidgetConfig> {

  private ExampleAddon addon;

  public BoosterCountWidget(ExampleAddon addon) {
    super("booster_count");
    this.addon = addon;
    this.bindCategory(ExampleAddon.CATEGORY);
    this.setIcon(Icon.texture(ResourceLocation.create("minecraft", "textures/item/experience_bottle.png")));
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    this.updateItemName(Component.text(Booster.sessionBoost.get()+"%"), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.playingOn.contains("MoneyMaker") && Booster.sessionBoost.get() > 0;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.texture(ResourceLocation.create("minecraft", "textures/item/experience_bottle.png"));
  }

}
