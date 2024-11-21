package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.utils.AddonUtil;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;

public class MoneyIconTag extends IconTag {

  private MoneyMakerAddon addon;

  public MoneyIconTag(MoneyMakerAddon addon) {
    super(9.0F);
    this.addon = addon;
  }

  @Override
  public boolean isVisible() {
    return this.entity.isCrouching() && this.visibleRank(entity) != null;
  }

  @Override
  public Icon getIcon() {
    MoneyRank rank = this.visibleRank(entity);
    return rank != null ? rank.getIcon() : super.getIcon();
  }

  private MoneyRank visibleRank(Entity entity) {
    if(!(entity instanceof Player player)) return null;
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!this.addon.configuration().badgeConfiguration.iconTag().get()) return null;
    if(!AddonUtil.playerStatus.containsKey(player.profile().getUniqueId())) return null;
    return AddonUtil.playerStatus.get(player.profile().getUniqueId()) != null ? AddonUtil.playerStatus.get(player.profile().getUniqueId()).rank() : null;
  }

}
