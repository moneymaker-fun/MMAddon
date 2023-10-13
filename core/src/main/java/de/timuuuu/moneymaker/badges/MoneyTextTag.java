package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import org.jetbrains.annotations.Nullable;

public class MoneyTextTag extends NameTag {

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if(!visible(entity)) return null;
    Component component = net.labymod.api.client.component.Component.text("MoneyMaker-Addon", TextColor.color(255, 255, 85))
        .decorate(TextDecoration.BOLD);
    if(Util.isDev(entity.getUniqueId().toString())) {
      component.append(Component.text(" Dev", TextColor.color(170, 0, 0)));
    } else {
      component.append(Component.text(" Staff", TextColor.color(255, 85, 85)));
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
    if(!AddonSettings.playerStatus.containsKey(player.profile().getUniqueId())) return false;
    return AddonSettings.playerStatus.get(player.profile().getUniqueId()).staff();
  }

}
