package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.group.Group;
import de.timuuuu.moneymaker.group.Group.GroupDisplay;
import de.timuuuu.moneymaker.snapshot.MoneyMakerKeys;
import de.timuuuu.moneymaker.snapshot.MoneyPlayerSnapshot;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class MoneyTextTag extends ComponentNameTag {

  private MoneyMakerAddon addon;
  private @Nullable Group group;

  public MoneyTextTag(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void begin(EntitySnapshot snapshot) {
    this.group = this.getVisibleGroup(snapshot);
    super.begin(snapshot);
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
    if(this.group == null) return super.buildComponents(snapshot);
    Component groupDisplayName = Component.text().append(Component.text("MoneyMaker-Addon", TextColor.color(this.addon.configuration().badgeConfiguration.textColor().get().get())))
        .append(Component.space()).append(Component.text(this.group.getTagName(), this.group.getTextColor())).build();
    return List.of(groupDisplayName);
  }

  @Override
  public float getScale() {
    return 0.5F;
  }

  @Override
  public boolean isVisible() {
    return this.group != null && super.isVisible();
  }

  private @Nullable Group getVisibleGroup(EntitySnapshot snapshot) {
    if(!this.visible(snapshot)) return null;
    if(!snapshot.has(MoneyMakerKeys.MONEY_PLAYER)) return null;
    MoneyPlayerSnapshot moneyPlayerSnapshot = snapshot.get(MoneyMakerKeys.MONEY_PLAYER);
    if(moneyPlayerSnapshot.getMoneyPlayer() == null) return null;
    Group group = moneyPlayerSnapshot.getMoneyPlayer().group();
    return group.getDisplayType() == GroupDisplay.BOTH || group.getDisplayType() == GroupDisplay.ABOVE_HEAD ? group : null;
  }

  private boolean visible(EntitySnapshot snapshot) {
    return this.addon.configuration().enabled().get() && this.addon.configuration().badgeConfiguration.textTag().get()
        && !snapshot.isDiscrete() && !snapshot.isInvisible();
  }

}
