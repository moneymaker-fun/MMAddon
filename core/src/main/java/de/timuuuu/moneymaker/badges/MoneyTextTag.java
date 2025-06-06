package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.utils.AddonUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import org.jetbrains.annotations.Nullable;

public class MoneyTextTag extends NameTag {

  private MoneyMakerAddon addon;

  public MoneyTextTag(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if(!visible(entity)) return null;
    Component component = Component.text("MoneyMaker-Addon", TextColor.color(this.addon.configuration().badgeConfiguration.textColor().get().get()));
    MoneyRank rank = AddonUtil.playerStatus.get(entity.getUniqueId()).rank();
    if(rank.getNameTag() != null) {
      component.append(rank.getNameTag());
    }
    return RenderableComponent.of(component);
  }

  @Override
  public float getScale() {
    return 0.6F;
  }

  @Override
  public boolean isVisible() {
    return !this.entity.isCrouching() && visible(entity);
  }

  private boolean visible(Entity entity) {
    if(!(entity instanceof Player player)) return false;
    if(player.profile().getUniqueId() == null) return false;
    if(!this.addon.configuration().enabled().get()) return false;
    if(!this.addon.configuration().badgeConfiguration.textTag().get()) return false;
    if(!AddonUtil.playerStatus.containsKey(player.profile().getUniqueId())) return false;
    if(AddonUtil.playerStatus.get(player.profile().getUniqueId()) == null) return false;
    return AddonUtil.playerStatus.get(player.profile().getUniqueId()).rank() != MoneyRank.USER;
  }

}
