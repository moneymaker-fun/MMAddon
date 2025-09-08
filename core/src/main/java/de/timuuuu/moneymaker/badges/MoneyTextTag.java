package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.group.Group;
import de.timuuuu.moneymaker.group.Group.GroupDisplay;
import de.timuuuu.moneymaker.utils.AddonUtil;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.gfx.pipeline.renderer.text.FontFlags;
import net.labymod.api.client.gfx.pipeline.renderer.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.color.format.ColorFormat;
import org.jetbrains.annotations.Nullable;

public class MoneyTextTag extends NameTag {

  private MoneyMakerAddon addon;
  private @Nullable Group group;

  public MoneyTextTag(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void begin(Entity entity) {
    this.group = visibleGroup(entity);
    super.begin(entity);
  }

  @Override
  public void render(Stack stack, Entity entity) {
    if(this.group == null) return;
    int alpha = (int)((float) Laby.labyAPI().minecraft().options().getBackgroundColorWithOpacity(192) * 255.0F);
    TextRenderer renderer = Laby.references().textRendererProvider().getRenderer();
    Component groupDisplayName = Component.text().append(Component.text("MoneyMaker-Addon", TextColor.color(this.addon.configuration().badgeConfiguration.textColor().get().get())))
        .append(Component.space()).append(Component.text(this.group.getTagName(), this.group.getTextColor())).build();
    float width = renderer.getWidth(groupDisplayName);
    renderer.render(stack.getProvider().getPose(), groupDisplayName, -width / 2.0F, 0.0F, -1, 15728880, ColorFormat.ARGB32.pack(0, alpha),
        FontFlags.DISPLAY_MODE_NORMAL);
  }

  @Override
  public float getScale() {
    return 0.5F;
  }

  @Override
  public boolean isVisible() {
    return !this.entity.isCrouching() && visibleGroup(entity) != null;
  }

  private Group visibleGroup(Entity entity) {
    if(!(entity instanceof Player player)) return null;
    if(player.profile().getUniqueId() == null) return null;
    if(!this.addon.configuration().enabled().get()) return null;
    if(!this.addon.configuration().badgeConfiguration.textTag().get()) return null;
    if(!AddonUtil.playerStatus.containsKey(player.profile().getUniqueId())) return null;
    if(AddonUtil.playerStatus.get(player.profile().getUniqueId()) == null) return null;
    Group group = AddonUtil.playerStatus.get(player.profile().getUniqueId()).group();
    return group.getDisplayType() == GroupDisplay.BOTH || group.getDisplayType() == GroupDisplay.ABOVE_HEAD ? group : null;
  }

}
