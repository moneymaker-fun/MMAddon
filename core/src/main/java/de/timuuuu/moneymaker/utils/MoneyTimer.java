package de.timuuuu.moneymaker.utils;

import net.labymod.api.util.concurrent.task.Task;

public class MoneyTimer {

  private String name;
  private int minutes;
  private Task task;

  public MoneyTimer(String name, int minutes, Task task) {
    this.name = name;
    this.minutes = minutes;
    this.task = task;
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

}
