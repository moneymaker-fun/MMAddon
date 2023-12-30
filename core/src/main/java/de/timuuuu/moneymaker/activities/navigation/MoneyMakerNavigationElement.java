package de.timuuuu.moneymaker.activities.navigation;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyMakerNavigationElement extends ScreenNavigationElement {

  MoneyMakerAddon addon;

  public MoneyMakerNavigationElement(MoneyMakerAddon addon) {
    super(addon.mainActivity());
    this.addon = addon;
  }

  @Override
  public String getWidgetId() {
    return "moneymaker_navigation";
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("moneymaker.navigation.title");
  }

  @Override
  public Icon getIcon() {
    return Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"));
  }

  @Override
  public boolean isVisible() {
    return true;
    //return this.addon.configuration().enabled().get() && (AddonSettings.playingOn.contains("MoneyMaker") || Util.isDev(this.addon.labyAPI().getUniqueId().toString()));
  }

}
