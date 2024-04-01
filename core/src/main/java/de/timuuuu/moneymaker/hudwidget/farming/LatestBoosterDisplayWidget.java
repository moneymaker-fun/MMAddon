package de.timuuuu.moneymaker.hudwidget.farming;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.boosters.BoosterUtil;
import de.timuuuu.moneymaker.hudwidget.farming.LatestBoosterDisplayWidget.BoosterHudWidgetConfig;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.Color;

public class LatestBoosterDisplayWidget extends SimpleHudWidget<BoosterHudWidgetConfig> {

  List<Booster> dummyBoosters = new ArrayList<>();

  private MoneyMakerAddon addon;

  public LatestBoosterDisplayWidget(MoneyMakerAddon addon) {
    super("mm_booster_display", BoosterHudWidgetConfig.class);
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.sprite16(
        ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 0, 0));
    this.addon = addon;
    this.dummyBoosters.add(new Booster(10, 15));
    this.dummyBoosters.add(new Booster(100, 30));
    this.dummyBoosters.add(new Booster(50, 60));
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext, HudSize size) {
    size.setHeight(0F);
    size.setWidth(0F);

    if(isEditorContext) {
      this.renderBoosters(this.dummyBoosters, stack, size);
      return;
    }

    if(!(this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get())) {
      this.renderComponent(Component.translatable("moneymaker.hudWidget.mm_booster_display.notConnected"), stack, size);
      return;
    }

    List<Booster> boosters = new ArrayList<>();
    for (int j = Booster.latestFoundBoosters().size() - 1; j >= 0; j--) {
      Booster booster = Booster.latestFoundBoosters().get(j);
      boosters.add(booster);
    }

    if(Booster.latestFoundBoosters().isEmpty()) {
      this.renderComponent(Component.translatable("moneymaker.hudWidget.mm_booster_display.noBoosters"), stack, size);
      return;
    }

    this.renderBoosters(boosters, stack, size);
  }

  private void renderComponent(Component component, Stack stack, HudSize size) {
    RenderableComponent renderableComponent = RenderableComponent.of(component);
    if(stack != null) {
      this.labyAPI.renderPipeline().componentRenderer().builder()
          .text(renderableComponent)
          .pos(1, 1)
          .render(stack);
    }
    size.setWidth(renderableComponent.getWidth() +2);
    size.setHeight(renderableComponent.getHeight() +2);
  }

  private void renderBoosters(List<Booster> boosters, Stack stack, HudSize size) {
    ComponentRenderer componentRenderer = this.labyAPI.renderPipeline().componentRenderer();
    int x = 1;
    int y = 1;
    Component title = Component.translatable("moneymaker.hudWidget.mm_booster_display.latestBoosters", TextColor.color(this.config.textColor.get().get()));
    RenderableComponent titleComponent = RenderableComponent.of(title);
    if(stack != null) {
      componentRenderer.builder()
          .text(titleComponent)
          .pos(x, y)
          .render(stack);
    }

    size.setWidth(x + titleComponent.getWidth() + 1);
    //x += 1;
    y += (int) (titleComponent.getHeight() +1);
    int rowHeight = (int) componentRenderer.height();

    int maxBoosterIndex = this.config.maxDisplayedBoosters.get() -1;
    int boosterIndex = 0;
    for(Booster booster : boosters) {
      if(boosterIndex > maxBoosterIndex) break;
      boosterIndex++;
      int timerX = x; //int timerX = x + 2;
      if(stack != null) {
        BoosterUtil.getIcon(booster).render(stack, timerX, y, rowHeight);
      }

      timerX += rowHeight +4;
      Component component = Component.text(booster.boost() + "%", NamedTextColor.YELLOW)
          .append(Component.text(" ┃ ", NamedTextColor.DARK_GRAY))
          .append(Component.text(booster.readableTime(), NamedTextColor.GRAY));
      RenderableComponent timerName = RenderableComponent.of(component);
      if(stack != null) {
        componentRenderer.builder()
            .text(timerName)
            .pos(timerX, y)
            .render(stack);
      }

      y += (int) (timerName.getHeight() +1);
      timerX += (int) timerName.getWidth();
      size.setWidth(Math.max(size.getActualWidth(), timerX +1));
    }

    size.setHeight((float) y);
  }

  @Override
  public boolean isVisibleInGame() {
    return (this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get()) && !Booster.latestFoundBoosters().isEmpty();
  }

  public static class BoosterHudWidgetConfig extends HudWidgetConfig {

    @ColorPickerSetting
    private final ConfigProperty<Color> textColor = new ConfigProperty<>(Color.YELLOW);

    @SliderSetting(min = 2, max = 10)
    private final ConfigProperty<Integer> maxDisplayedBoosters = new ConfigProperty<>(5);

  }

}
