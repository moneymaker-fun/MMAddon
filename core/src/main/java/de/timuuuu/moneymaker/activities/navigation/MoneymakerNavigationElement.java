package de.timuuuu.moneymaker.activities.navigation;

import de.timuuuu.moneymaker.ExampleAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneymakerNavigationElement extends ScreenNavigationElement {

  ExampleAddon addon;

  public MoneymakerNavigationElement(ExampleAddon addon) {
    super(addon.moneymakerMainAcivity);
    this.addon = addon;
  }

  @Override
  public String getWidgetId() {
    return "moneymaker_navigation";
  }

  @Override
  public Component getDisplayName() {
    return Component.text("MoneyMaker");
  }

  @Override
  public Icon getIcon() {
    return Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"));
  }

  @Override
  public boolean isVisible() {
    return AddonSettings.playingOn.contains("MoneyMaker");
  }
}
