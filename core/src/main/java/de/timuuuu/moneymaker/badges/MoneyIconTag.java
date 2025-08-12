package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.group.Group;
import de.timuuuu.moneymaker.group.Group.GroupDisplay;
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
    return !this.entity.isCrouching() && this.visibleGroup(entity) != null;
  }

  @Override
  public Icon getIcon() {
    Group group = this.visibleGroup(entity);
    return group != null ? group.getIcon() : super.getIcon();
  }

  private Group visibleGroup(Entity entity) {
    if(!(entity instanceof Player player)) return null;
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!this.addon.configuration().badgeConfiguration.iconTag().get()) return null;
    if(!AddonUtil.playerStatus.containsKey(player.profile().getUniqueId())) return null;
    if(AddonUtil.playerStatus.get(player.profile().getUniqueId()) == null) return null;
    Group group = AddonUtil.playerStatus.get(player.profile().getUniqueId()).group();
    if(group.getDisplayType() == GroupDisplay.BOTH || group.getDisplayType() == GroupDisplay.BESIDE_NAME) {
      return group;
    }
    return null;
  }

}
