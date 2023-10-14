package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.hudwidget.TimerDisplayWidget.TimerHudWidgetConfig;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import java.util.ArrayList;
import java.util.List;

public class TimerDisplayWidget extends SimpleHudWidget<TimerHudWidgetConfig> {

  List<MoneyTimer> dummyTimers = new ArrayList<>();

  private MoneyMakerAddon addon;

  public TimerDisplayWidget(MoneyMakerAddon addon) {
    super("timer_display", TimerHudWidgetConfig.class);
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.sprite16(
        ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 0, 2));
    this.addon = addon;
    this.dummyTimers.add(new MoneyTimer("Timer", 30));
    this.dummyTimers.add(new MoneyTimer("Anderer Timer", 10));
    this.dummyTimers.add(new MoneyTimer("Noch ein Timer", 40));
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext, HudSize size) {
    size.setHeight(0F);
    size.setWidth(0F);

    if(isEditorContext) {
      this.renderTimers(this.dummyTimers, stack, size);
      return;
    }

    if(!AddonSettings.playingOn.contains("MoneyMaker")) {
      this.renderComponent(Component.text("Nicht auf MoneyMaker"), stack, size);
      return;
    }

    List<MoneyTimer> timers = new ArrayList<>(Util.timers.values());
    if(timers.isEmpty()) {
      this.renderComponent(Component.text("Keine Timer aktiv"), stack, size);
      return;
    }

    this.renderTimers(timers, stack, size);
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

  private void renderTimers(List<MoneyTimer> timers, Stack stack, HudSize size) {
    ComponentRenderer componentRenderer = this.labyAPI.renderPipeline().componentRenderer();
    int x = 1;
    int y = 1;
    Component title = Component.text("Aktuell laufende Timer: ");
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

    int maxTimerIndex = this.config.maxDisplayedTimers.get() -1;
    int timerIndex = 0;
    for(MoneyTimer timer : timers) {
      if(timerIndex > maxTimerIndex) break;
      timerIndex++;
      int timerX = x; //int timerX = x + 2;
      if(stack != null) {
        Icon icon = Icon.sprite16(
            ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 0, 2);
        icon.render(stack, timerX, y, rowHeight);
      }

      timerX += rowHeight +4;
      RenderableComponent timerName = RenderableComponent.of(Component.text("§e"+timer.name()+" §8[§e"+timer.minutes()+"m§8] » §e"+timer.remainingTime()));
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

  // https://github.com/labymod-addons/teamspeak/blob/master/core/src/main/java/net/labymod/addons/teamspeak/core/hud/TeamSpeakHudWidget.java

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.playingOn.contains("MoneyMaker") && !Util.timers.isEmpty();
  }

  public static class TimerHudWidgetConfig extends HudWidgetConfig {

    @SliderSetting(min = 2, max = 20)
    private final ConfigProperty<Integer> maxDisplayedTimers = new ConfigProperty<>(5);

  }

}
