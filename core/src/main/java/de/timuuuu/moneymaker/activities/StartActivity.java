package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.TimerWidget;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.OperatingSystem;

@AutoActivity
@Link("start.lss")
public class StartActivity extends Activity {

  private MoneyMakerAddon addon;

  public StartActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    Util.addFeedbackButton(this.document);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.start.title").addId("start-title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("start-container");

    ComponentWidget breakGoalTitle = ComponentWidget.i18n("moneymaker.ui.start.break-goal.title").addId("break-goal-title");
    breakGoalTitle.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.description"));
    container.addChild(breakGoalTitle);

    SwitchWidget breakGoalSwitch = SwitchWidget.create(value -> {
      AddonSettings.breakGoalEnabled = value;
      AddonSettings.breakGoalBlocks = 0;
      if(!value) {
        AddonSettings.breakGoal = 0;
      }
      this.reload();
    }).addId("break-goal-switch");
    breakGoalSwitch.setValue(AddonSettings.breakGoalEnabled);
    breakGoalSwitch.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.description"));
    container.addChild(breakGoalSwitch);

    if(AddonSettings.breakGoalEnabled) {
      ComponentWidget breakGoalInputTitle = ComponentWidget.i18n("moneymaker.ui.start.break-goal.input-title").addId("break-goal-input-title");
      breakGoalInputTitle.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.input-description"));
      container.addChild(breakGoalInputTitle);

      TextFieldWidget breakGoalInput = new TextFieldWidget().addId("break-goal-input");
      //breakGoalInput.validator(this::validateInput);
      breakGoalInput.submitHandler(this::submitInput);
      if(AddonSettings.breakGoal > 0) {
        breakGoalInput.setText(String.valueOf(AddonSettings.breakGoal));
      }
      container.addChild(breakGoalInput);
    }

    ComponentWidget timerTitle = ComponentWidget.i18n("moneymaker.ui.start.current-timers").addId("timer-title");

    container.addChild(timerTitle);

    DivWidget timerContainer = new DivWidget().addId("timer-container");

    VerticalListWidget<TimerWidget> timerList = new VerticalListWidget<>().addId("timer-list");
    Util.timers.values().forEach(timer -> {
      timerList.addChild(new TimerWidget(this.addon, timer));
    });

    ScrollWidget scrollWidget = new ScrollWidget(timerList, new ListSession<>());
    timerContainer.addChild(scrollWidget);

    container.addChild(timerContainer);

    ButtonWidget donateButton = ButtonWidget.i18n("moneymaker.ui.start.donate", Icon.texture(
        ResourceLocation.create("moneymaker", "textures/ui/donate.png"))).addId("donate-btn");
    donateButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://www.paypal.com/donate/?hosted_button_id=P5DTFDECSA532");
    });
    container.addChild(donateButton);

    ButtonWidget discordButton = ButtonWidget.i18n("moneymaker.ui.start.discord", Icon.texture(
        ResourceLocation.create("moneymaker", "textures/ui/discord.png"))).addId("discord-btn");
    discordButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://discord.gg/XKjAZFgknd");
    });
    container.addChild(discordButton);

    this.document.addChild(container);

  }

  private void submitInput(String input) {
    try {
      int count = Integer.parseInt(input);
      AddonSettings.breakGoal = count;
      if(AddonSettings.currentBrokenBlocks > 0) {
        AddonSettings.breakGoalBlocks = AddonSettings.currentBrokenBlocks + count;
      }
      this.addon.pushNotification(Component.translatable("moneymaker.notification.break-goal.title", TextColor.color(255, 255, 85)),
          Component.translatable("moneymaker.notification.break-goal.set", TextColor.color(170, 170, 170),
              Component.text(count, TextColor.color(255, 255, 85))));
    } catch (NumberFormatException ignored) {
      this.addon.pushNotification(Component.translatable("moneymaker.notification.break-goal.title", TextColor.color(255, 255, 85)),
          Component.translatable("moneymaker.notification.break-goal.no-number", TextColor.color(255, 85, 85)));
    }
  }

  @SuppressWarnings("unused")
  private boolean validateInput(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException ignored) {}
    return false;
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }

  public void reloadScreen() {
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

}
