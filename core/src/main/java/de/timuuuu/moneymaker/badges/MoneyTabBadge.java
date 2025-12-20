package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.group.Group;
import de.timuuuu.moneymaker.utils.AddonUtil;
import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.network.NetworkPlayerInfo;

public class MoneyTabBadge extends BadgeRenderer {

  private MoneyMakerAddon addon;

  public MoneyTabBadge(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(ScreenContext context, float x, float y, NetworkPlayerInfo player) {
    Group group = this.visibleRank(player);
    if(group == null) return;
    if(group.getIcon() == null) return;
    context.canvas().submitIcon(group.getIcon(), x +1, y, 8.0F, 8.0F);
  }

  @Override
  protected boolean isVisible(NetworkPlayerInfo player) {
    return this.visibleRank(player) != null;
  }

  private Group visibleRank(NetworkPlayerInfo player) {
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!this.addon.configuration().badgeConfiguration.tabListIcon().get()) return null;
    if(!AddonUtil.playerStatus.containsKey(player.profile().getUniqueId())) return null;
    return AddonUtil.playerStatus.get(player.profile().getUniqueId()) != null ? AddonUtil.playerStatus.get(player.profile().getUniqueId()).group() : null;
  }

}
