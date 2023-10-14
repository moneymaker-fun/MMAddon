package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.concurrent.task.Task;
import java.util.concurrent.TimeUnit;

public class MoneyTimer {

  private String name;
  private int minutes;
  private int seconds;
  private Task task;

  public MoneyTimer(String name, int minutes) {
    this.name = name;
    this.minutes = minutes;
    this.seconds = minutes*60;
  }

  public String name() {
    return name;
  }

  public int minutes() {
    return minutes;
  }

  public Task task() {
    return task;
  }

  public MoneyTimer start() {
    this.task = Task.builder(() -> {
      this.seconds--;
      if(this.seconds == 0) {
        this.task.cancel();
        MoneyMakerAddon.instance().labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
        MoneyMakerAddon.instance().pushNotification(Component.text("Timer abgelaufen!"), Component.text("§7Dein Timer mit dem Namen §e" + this.name + " §7ist abgelaufen."));
        MoneyMakerAddon.instance().startActivity.reloadScreen();
      }
    }).repeat(1, TimeUnit.SECONDS).build();
    this.task.execute();
    return this;
  }

  public String remainingTime() {
    long seconds = this.seconds;
    long minutes = 0;
    while (seconds >= 60) {
      seconds -= 60;
      minutes++;
    }
    String secString = String.valueOf(seconds);
    String minString = String.valueOf(minutes);
    if (minString.length() == 1) {
      minString = "0" + minString;
    }
    if (secString.length() == 1) {
      secString = "0" + secString;
    }
    return minString + ":" + secString;
  }

}
