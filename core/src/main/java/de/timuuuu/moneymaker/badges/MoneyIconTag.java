package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.group.Group;
import de.timuuuu.moneymaker.group.Group.GroupDisplay;
import de.timuuuu.moneymaker.group.GroupService;
import de.timuuuu.moneymaker.snapshot.MoneyMakerKeys;
import de.timuuuu.moneymaker.snapshot.MoneyPlayerSnapshot;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import org.jetbrains.annotations.Nullable;

public class MoneyIconTag extends IconTag {

  private MoneyMakerAddon addon;
  private @Nullable Group group;

  public MoneyIconTag(MoneyMakerAddon addon) {
    super(9.0F);
    this.addon = addon;
  }

  public void begin(EntitySnapshot snapshot) {
    this.group = this.getVisibleGroup(snapshot);
    super.begin(snapshot);
  }

  public boolean isVisible() {
    return this.group != null && super.isVisible();
  }

  public Icon getIcon() {
    return this.group != null ? this.group.getIcon() : GroupService.DEFAULT_GROUP.getIcon();
  }

  private @Nullable Group getVisibleGroup(EntitySnapshot snapshot) {
    if(!this.visible(snapshot)) {
      this.addon.logger().info("IconTag is not visible");
      return null;
    }
    if(!snapshot.has(MoneyMakerKeys.MONEY_PLAYER)) {
      this.addon.logger().info("EntitySnapshot does not have MoneyPlayerSnapshot");
      return null;
    }
    MoneyPlayerSnapshot moneyPlayerSnapshot = snapshot.get(MoneyMakerKeys.MONEY_PLAYER);
    if(moneyPlayerSnapshot.getMoneyPlayer() == null) {
      this.addon.logger().info("MoneyPlayerSnapshot does not have MoneyPlayer");
      return null;
    }
    Group group = moneyPlayerSnapshot.getMoneyPlayer().group();
    return group.getDisplayType() == GroupDisplay.BOTH || group.getDisplayType() == GroupDisplay.BESIDE_NAME ? group : null;
  }

  private boolean visible(EntitySnapshot snapshot) {
    return this.addon.configuration().enabled().get() && this.addon.configuration().badgeConfiguration.iconTag().get()
        && !snapshot.isDiscrete() && !snapshot.isInvisible();
  }

}
