package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyTabBadge extends BadgeRenderer {

  private MoneyMakerAddon addon;

  public MoneyTabBadge(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(Stack stack, float x, float y, NetworkPlayerInfo player) {
    if(visible(player)) {
      Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png")).render(stack, x, y, 8.0F);
    }
  }

  @Override
  protected boolean isVisible(NetworkPlayerInfo player) {
    return visible(player);
  }

  private boolean visible(NetworkPlayerInfo player) {
    if(player.profile().getUniqueId() == null) return false;
    if(!this.addon.configuration().moneyBadgeConfiguration.tabListIcon().get()) return false;
    return AddonSettings.playerStatus.containsKey(player.profile().getUniqueId());
  }

}
