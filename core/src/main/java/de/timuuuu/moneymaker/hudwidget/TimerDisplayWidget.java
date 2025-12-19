package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.hudwidget.TimerDisplayWidget.TimerHudWidgetConfig;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
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

public class TimerDisplayWidget extends SimpleHudWidget<TimerHudWidgetConfig> {

  List<MoneyTimer> dummyTimers = new ArrayList<>();

  private MoneyMakerAddon addon;

  public TimerDisplayWidget(MoneyMakerAddon addon) {
    super("mm_timer_display", TimerHudWidgetConfig.class);
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.sprite16(
        ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 0, 2));
    this.addon = addon;
    this.dummyTimers.add(new MoneyTimer("Timer #1", 30));
    this.dummyTimers.add(new MoneyTimer("Timer #2", 10));
    this.dummyTimers.add(new MoneyTimer("Timer #3", 40));
  }

  @Override
  public void render(RenderPhase renderPhase, ScreenContext screenContext, boolean isEditorContext, HudSize size) {
    size.setHeight(0.0F);
    size.setWidth(0.0F);

    if(isEditorContext) {
      this.renderTimers(this.dummyTimers, renderPhase, screenContext, size);
      return;
    }

    if(!this.addon.addonUtil().connectedToMoneyMaker()) {
      this.renderComponent(Component.translatable("moneymaker.hudWidget.mm_timer_display.notConnected"), renderPhase, screenContext, size);
      return;
    }

    List<MoneyTimer> timers = new ArrayList<>(Util.timers.values());
    if(timers.isEmpty()) {
      this.renderComponent(Component.translatable("moneymaker.hudWidget.mm_timer_display.noTimers"), renderPhase, screenContext, size);
      return;
    }

    this.renderTimers(timers, renderPhase, screenContext, size);
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

  private void renderTimers(List<MoneyTimer> timers, RenderPhase phase, ScreenContext screenContext, HudSize size) {
    int x = 1;
    int y = 1;
    Component title = Component.translatable("moneymaker.hudWidget.mm_timer_display.currentTimers", TextColor.color(this.config.textColor.get().get()));
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

    int maxTimerIndex = this.config.maxDisplayedTimers.get() -1;
    int timerIndex = 0;
    for(MoneyTimer timer : timers) {
      if(timerIndex > maxTimerIndex) break;
      timerIndex++;
      int timerX = x; //int timerX = x + 2;
      if(phase.canRender()) {
        Icon icon = Icon.sprite16(
            ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 0, 2);
        screenContext.canvas().submitIcon(
            icon, timerX, y, rowHeight, rowHeight
        );
      }

      timerX += rowHeight +4;
      Component component = Component.text(timer.name(), NamedTextColor.YELLOW)
          .append(Component.text(" [", NamedTextColor.DARK_GRAY))
          .append(Component.text(timer.minutes() + "m", NamedTextColor.YELLOW))
          .append(Component.text("] » ", NamedTextColor.DARK_GRAY))
          .append(Component.text(timer.remainingTime(), NamedTextColor.YELLOW));
      RenderableComponent timerName = RenderableComponent.of(component);
      if(phase.canRender()) {
        screenContext.canvas().submitRenderableComponent(
            timerName,
            timerX,
            y,
            -1,
            TextRenderingOptions.SHADOW
        );
      }

      y += (int) (timerName.getHeight() +1);
      timerX += (int) timerName.getWidth();
      size.setWidth(Math.max(size.getActualWidth(), timerX +1));
    }

    size.setHeight((float) y);
  }

  // https://github.com/labymod-addons/teamspeak/blob/master/core/src/main/java/net/labymod/addons/teamspeak/core/hud/TeamSpeakHudWidget.java

  @Override
  public boolean isVisibleInGame() {
    return this.addon.addonUtil().connectedToMoneyMaker() && !Util.timers.isEmpty();
  }

  public static class TimerHudWidgetConfig extends HudWidgetConfig {

    @ColorPickerSetting
    private final ConfigProperty<Color> textColor = new ConfigProperty<>(Color.YELLOW);

    @SliderSetting(min = 2, max = 15)
    private final ConfigProperty<Integer> maxDisplayedTimers = new ConfigProperty<>(5);

  }

}
