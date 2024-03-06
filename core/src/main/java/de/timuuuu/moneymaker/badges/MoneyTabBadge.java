package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonUtil;
import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.matrix.Stack;

public class MoneyTabBadge extends BadgeRenderer {

  private MoneyMakerAddon addon;

  public MoneyTabBadge(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(Stack stack, float x, float y, NetworkPlayerInfo player) {
    MoneyRank rank = this.visibleRank(player);
    if(rank != null) {
      rank.getIcon().render(stack, x, y, 8.0F);
    }
  }

  @Override
  protected boolean isVisible(NetworkPlayerInfo player) {
    return this.visibleRank(player) != null;
  }

  private MoneyRank visibleRank(NetworkPlayerInfo player) {
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!this.addon.configuration().badgeConfiguration.tabListIcon().get()) return null;
    return AddonUtil.playerStatus.containsKey(player.profile().getUniqueId()) ? AddonUtil.playerStatus.get(player.profile().getUniqueId()).rank() : null;
  }

}
