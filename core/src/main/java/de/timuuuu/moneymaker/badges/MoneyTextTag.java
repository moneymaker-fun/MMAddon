package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
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
    Component component = Component.text("MoneyMaker-Addon", TextColor.color(this.addon.configuration().moneyBadgeConfiguration.textColor().get().get()))
        .decorate(TextDecoration.BOLD);
    MoneyRank rank = AddonSettings.playerStatus.get(entity.getUniqueId()).rank();
    if(rank.getNameTag() != null) {
      component.append(rank.getNameTag());
    }
    return RenderableComponent.of(component);
  }

  @Override
  public float getHeight() {
    return super.getHeight();
  }

  @Override
  public float getScale() {
    return 0.6F;
  }

  @Override
  public boolean isVisible() {
    return super.isVisible();
  }

  private boolean visible(Entity entity) {
    if(!(entity instanceof Player player)) return false;
    if(player.profile().getUniqueId() == null) return false;
    if(!this.addon.configuration().moneyBadgeConfiguration.textTag().get()) return false;
    if(!AddonSettings.playerStatus.containsKey(player.profile().getUniqueId())) return false;
    return AddonSettings.playerStatus.get(player.profile().getUniqueId()).rank() != MoneyRank.USER;
  }

}
