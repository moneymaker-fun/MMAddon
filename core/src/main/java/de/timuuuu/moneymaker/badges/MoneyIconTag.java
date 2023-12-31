package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
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
    return this.visibleRank(entity) != null;
  }

  @Override
  public int getColor() {
    return super.getColor();
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
    if(!this.addon.configuration().moneyBadgeConfiguration.iconTag().get()) return null;
    return AddonSettings.playerStatus.containsKey(player.profile().getUniqueId()) ? AddonSettings.playerStatus.get(player.profile().getUniqueId()).rank() : null;
  }

}
