package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.TimerWidget;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.render.matrix.Stack;

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
      if(!value) {
        AddonSettings.breakGoal = 0;
        AddonSettings.breakGoalBlocks = 0;
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
      container.addChild(breakGoalInput);
    }

    ComponentWidget timerTitle = ComponentWidget.text("Aktuell laufende Timer").addId("timer-title");

    container.addChild(timerTitle);

    DivWidget timerContainer = new DivWidget().addId("timer-container");

    VerticalListWidget<TimerWidget> timerList = new VerticalListWidget<>().addId("timer-list");
    Util.timers.values().forEach(timer -> {
      timerList.addChild(new TimerWidget(this.addon, timer));
    });

    ScrollWidget scrollWidget = new ScrollWidget(timerList, new ListSession<>());
    timerContainer.addChild(scrollWidget);

    container.addChild(timerContainer);

    this.document.addChild(container);

  }

  private void submitInput(String input) {
    try {
      int count = Integer.parseInt(input);
      AddonSettings.breakGoal = count;
      this.addon.pushNotification(Component.text("§eAbbau Ziel"), Component.text("§7Du hast dein Abbau Ziel auf §e" + count + " §7Blöcke gesetzt."));
    } catch (NumberFormatException ignored) {
      this.addon.pushNotification(Component.text("§4Fehler"), Component.text("§cBitte gebe eine Zahl als Input ein."));
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
