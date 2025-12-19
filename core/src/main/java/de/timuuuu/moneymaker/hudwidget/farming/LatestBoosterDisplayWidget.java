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
import net.labymod.api.client.gfx.pipeline.renderer.text.TextRenderingOptions;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.render.font.RenderableComponent;
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
  public void render(RenderPhase renderPhase, ScreenContext screenContext, boolean isEditorContext, HudSize size) {
    size.setHeight(0.0F);
    size.setWidth(0.0F);

    if(isEditorContext) {
      this.renderBoosters(this.dummyBoosters, renderPhase, screenContext, size);
      return;
    }

    if(!(this.addon.addonUtil().inFarming() || this.addon.configuration().showWidgetsAlways().get())) {
      this.renderComponent(Component.translatable("moneymaker.hudWidget.mm_booster_display.notConnected"), renderPhase, screenContext, size);
      return;
    }

    List<Booster> boosters = new ArrayList<>();
    for (int j = Booster.latestFoundBoosters().size() - 1; j >= 0; j--) {
      Booster booster = Booster.latestFoundBoosters().get(j);
      boosters.add(booster);
    }

    if(Booster.latestFoundBoosters().isEmpty()) {
      this.renderComponent(Component.translatable("moneymaker.hudWidget.mm_booster_display.noBoosters"), renderPhase, screenContext, size);
      return;
    }

    this.renderBoosters(boosters, renderPhase, screenContext, size);
  }

  private void renderComponent(Component component, RenderPhase phase, ScreenContext screenContext, HudSize size) {
    RenderableComponent renderableComponent = RenderableComponent.of(component);
    if(phase.canRender()) {
      screenContext.canvas().submitRenderableComponent(
          renderableComponent,
          1,
          1,
          -1,
          TextRenderingOptions.SHADOW
      );
    }
    size.setWidth(renderableComponent.getWidth() +2);
    size.setHeight(renderableComponent.getHeight() +2);
  }

  private void renderBoosters(List<Booster> boosters, RenderPhase phase, ScreenContext screenContext, HudSize size) {
    int x = 1;
    int y = 1;
    Component title = Component.translatable("moneymaker.hudWidget.mm_booster_display.latestBoosters", TextColor.color(this.config.textColor.get().get()));
    RenderableComponent titleComponent = RenderableComponent.of(title);
    if(phase.canRender()) {
      screenContext.canvas().submitRenderableComponent(
          titleComponent,
          x,
          y,
          -1,
          TextRenderingOptions.SHADOW
      );
    }

    size.setWidth(x + titleComponent.getWidth() + 1);
    //x += 1;
    y += (int) (titleComponent.getHeight() +1);
    int rowHeight = (int) screenContext.canvas().getLineHeight();

    int maxBoosterIndex = this.config.maxDisplayedBoosters.get() -1;
    int boosterIndex = 0;
    for(Booster booster : boosters) {
      if(boosterIndex > maxBoosterIndex) break;
      boosterIndex++;
      int boosterX = x; //int boosterX = x + 2;
      if(phase.canRender()) {
        screenContext.canvas().submitIcon(
            BoosterUtil.getIcon(booster),
            boosterX,
            y,
            rowHeight,
            rowHeight
        );
      }

      boosterX += rowHeight +4;
      Component component = Component.text(booster.boost() + "%", NamedTextColor.YELLOW)
          .append(Component.text(" ┃ ", NamedTextColor.DARK_GRAY))
          .append(Component.text(booster.readableTime(), NamedTextColor.GRAY));
      RenderableComponent boosterName = RenderableComponent.of(component);
      if(phase.canRender()) {
        screenContext.canvas().submitRenderableComponent(
            boosterName,
            boosterX,
            y,
            -1,
            TextRenderingOptions.SHADOW
        );
      }

      y += (int) (boosterName.getHeight() +1);
      boosterX += (int) boosterName.getWidth();
      size.setWidth(Math.max(size.getActualWidth(), boosterX +1));
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
