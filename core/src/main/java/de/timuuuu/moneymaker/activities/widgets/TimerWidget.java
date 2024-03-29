package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;

public class TimerWidget extends HorizontalListWidget {

  private MoneyMakerAddon addon;
  private MoneyTimer timer;

  public TimerWidget(MoneyMakerAddon addon, MoneyTimer timer) {
    this.addon = addon;
    this.timer = timer;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    ButtonWidget cancelButton = ButtonWidget.deleteButton().addId("timer-cancel");
    cancelButton.setPressable(() -> {
      if(this.timer.task().isRunning()) {
        this.timer.task().cancel();
        this.addon.pushNotification(Component.translatable("moneymaker.notification.timer.deleted.title", TextColor.color(255, 85, 85)),
            Component.translatable("moneymaker.notification.timer.deleted.text", TextColor.color(170, 170, 170),
                Component.text(this.timer.name(), TextColor.color(255, 255, 85))));
      }
      Util.timers.remove(this.timer.name());
      this.addon.startActivity().reloadScreen();
    });
    this.addEntry(cancelButton);
    this.addEntry(ComponentWidget.text("§8[§e" + this.timer.minutes() + "m§8- §6" + timer.remainingTime() + "§8] §e" + this.timer.name()).addId("timer-name"));
  }

}
