package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
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
    Component component = Component.text("[", NamedTextColor.DARK_GRAY)
        .append(Component.text(this.timer.minutes() + "m", NamedTextColor.YELLOW))
        .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
        .append(Component.text(this.timer.remainingTime(), NamedTextColor.GOLD))
        .append(Component.text("]", NamedTextColor.DARK_GRAY))
        .append(Component.text(this.timer.name(), NamedTextColor.YELLOW));
    this.addEntry(ComponentWidget.component(component).addId("timer-name"));
  }

}
