package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import java.util.function.Predicate;

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

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.start.title");
    titleWidget.addId("start-title");
    this.document.addChild(titleWidget);

    ComponentWidget breakGoalTitle = ComponentWidget.i18n("moneymaker.ui.start.break-goal.title");
    breakGoalTitle.addId("break-goal-title");
    breakGoalTitle.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.description"));
    this.document.addChild(breakGoalTitle);

    SwitchWidget breakGoalSwitch = SwitchWidget.create(value -> {
      AddonSettings.breakGoalEnabled = value;
      if(!value) {
        AddonSettings.breakGoal = 0;
      }
      this.reload();
    }).addId("break-goal-switch");
    breakGoalSwitch.setValue(AddonSettings.breakGoalEnabled);
    breakGoalSwitch.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.description"));
    this.document.addChild(breakGoalSwitch);

    if(AddonSettings.breakGoalEnabled) {
      ComponentWidget breakGoalInputTitle = ComponentWidget.i18n("moneymaker.ui.start.break-goal.input-title");
      breakGoalInputTitle.addId("break-goal-input-title");
      breakGoalInputTitle.setHoverComponent(Component.translatable("moneymaker.ui.start.break-goal.input-description"));
      this.document.addChild(breakGoalInputTitle);

      TextFieldWidget breakGoalInput = new TextFieldWidget();
      breakGoalInput.addId("break-goal-input");
      //breakGoalInput.validator(this::validateInput);
      breakGoalInput.submitHandler(this::submitInput);
      this.document.addChild(breakGoalInput);
    }

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

}
