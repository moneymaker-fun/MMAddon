package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.TimerWidget;
import de.timuuuu.moneymaker.utils.Util;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

@AutoActivity
@Links({@Link("start.lss"), @Link("buttons.lss")})
public class StartActivity extends SimpleActivity {

  private MoneyMakerAddon addon;
  private AtomicInteger secretCount = new AtomicInteger(5);

  public StartActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.start.title").addId("title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("start-container");

    ComponentWidget breakGoalTitle = ComponentWidget.i18n("moneymaker.ui.start.break-goal.title").addId("break-goal-title");
    breakGoalTitle.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.description"));
    container.addChild(breakGoalTitle);

    SwitchWidget breakGoalSwitch = SwitchWidget.create(value -> {
      this.addon.addonSettings().breakGoalEnabled(value);
      this.addon.addonUtil().breakGoalBlocks(0);
      if(!value) {
        this.addon.addonSettings().breakGoal(0);
      }
      this.reload();
    }).addId("break-goal-switch");
    breakGoalSwitch.setValue(this.addon.addonSettings().breakGoalEnabled());
    breakGoalSwitch.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.description"));
    container.addChild(breakGoalSwitch);

    if(this.addon.addonSettings().breakGoalEnabled()) {
      ComponentWidget breakGoalInputTitle = ComponentWidget.i18n("moneymaker.ui.start.break-goal.input-title").addId("break-goal-input-title");
      breakGoalInputTitle.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.input-description"));
      container.addChild(breakGoalInputTitle);

      TextFieldWidget breakGoalInput = new TextFieldWidget().addId("break-goal-input");
      breakGoalInput.submitHandler(this::submitInput);
      if(this.addon.addonSettings().breakGoal() > 0) {
        breakGoalInput.setText(String.valueOf(this.addon.addonSettings().breakGoal()));
      }
      container.addChild(breakGoalInput);
    }

    DivWidget commandsContainer = new DivWidget().addId("commands-container");

    commandsContainer.addChild(ComponentWidget.i18n("moneymaker.ui.start.commands.title").addId("commands-title"));

    Component commands = Component.translatable("moneymaker.ui.start.commands.timer.command", NamedTextColor.AQUA)
        .append(Component.translatable("moneymaker.ui.start.commands.timer.description", NamedTextColor.GOLD))
        .append(Component.text("\n"))
        .append(Component.translatable("moneymaker.ui.start.commands.reset.command", NamedTextColor.AQUA))
        .append(Component.translatable("moneymaker.ui.start.commands.reset.description", NamedTextColor.GOLD));
    commandsContainer.addChild(ComponentWidget.component(commands).addId("command-list"));

    DivWidget timerContainer = new DivWidget().addId("timer-container");

    timerContainer.addChild(ComponentWidget.i18n("moneymaker.ui.start.current-timers").addId("timer-title"));

    VerticalListWidget<TimerWidget> timerList = new VerticalListWidget<>().addId("timer-list");
    Util.timers.values().forEach(timer -> timerList.addChild(new TimerWidget(this.addon, timer)));

    timerContainer.addChild(new ScrollWidget(timerList, new ListSession<>()).addId("timer-scroll"));

    this.document.addChild(container);
    this.document.addChild(commandsContainer);
    this.document.addChild(timerContainer);

    //Toggle secret
    ButtonWidget secretButton = ButtonWidget.text("").addId("secret-button");
    secretButton.setActionListener(() -> {
      secretCount.decrementAndGet();
      if (secretCount.get() == 0) {
        this.addon.mainActivity().registerSecret();
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);
      }
    });
    this.document.addChild(secretButton);

    this.document.addChild(Util.feedbackButton());
    this.document.addChild(Util.discordButton());
    this.document.addChild(Util.leaderboardButton());
    Util.addCredits(this.addon, this.document);
  }

  private void submitInput(String input) {
    try {
      int count = Util.parseInteger(input, this.getClass());
      this.addon.addonSettings().breakGoal(count);
      if(this.addon.addonUtil().currentBrokenBlocks() > 0) {
        this.addon.addonUtil().breakGoalBlocks(this.addon.addonUtil().currentBrokenBlocks() + count);
      }
      this.addon.addonUtil().startTimestamp(System.currentTimeMillis());
      this.addon.pushNotification(Component.translatable("moneymaker.notification.break-goal.title", TextColor.color(255, 255, 85)),
          Component.translatable("moneymaker.notification.break-goal.set", TextColor.color(170, 170, 170),
              Component.text(Util.format(count), TextColor.color(255, 255, 85))));
    } catch (NumberFormatException ignored) {
      this.addon.pushNotification(Component.translatable("moneymaker.notification.break-goal.title", TextColor.color(255, 255, 85)),
          Component.translatable("moneymaker.notification.break-goal.no-number", TextColor.color(255, 85, 85)));
    }
  }

  public void reloadScreen() {
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

}
