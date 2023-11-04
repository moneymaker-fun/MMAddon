package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyIconTag extends IconTag {

  public MoneyIconTag() {
    super(9.0F);
  }

  @Override
  public boolean isVisible() {
    return visible(entity);
  }

  @Override
  public int getColor() {
    return super.getColor();
  }

  @Override
  public Icon getIcon() {
    return Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"));
  }

  private boolean visible(Entity entity) {
    if(!(entity instanceof Player player)) return false;
    if(player.profile().getUniqueId() == null) return false;
    return AddonSettings.playerStatus.containsKey(player.profile().getUniqueId());
  }

}
