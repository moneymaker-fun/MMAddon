package de.timuuuu.moneymaker.utils;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Booster {

  private static LinkedList<Booster> boosterguilist = new LinkedList<>();

  public static AtomicInteger sessionBoost = new AtomicInteger(0);

  public static void insertBooster(int boost, int time) {
    int i;
    for (i = 0; i < boosterguilist.size(); i++) {
      if (boosterguilist.get(i).getBoost() == boost && boosterguilist.get(i).getTime() == time) {
        boosterguilist.get(i).addAmnt();
        return;
      }
    }
    for (i = 0; i < boosterguilist.size(); i++) {
      if (boosterguilist.get(i).getBoost() < boost) {
        boosterguilist.add(i, new Booster(boost, time));
        return;
      }
      if (boosterguilist.get(i).getBoost() == boost && boosterguilist.get(i).getTime() < time) {
        boosterguilist.add(i, new Booster(boost, time));
        return;
      }
    }
    boosterguilist.add(new Booster(boost, time));
  }

  public static LinkedList<Booster> getBoosterguilist() {
    return boosterguilist;
  }

  private int boost;

  private int amnt;

  private int time;

  public Booster(int boost, int time) {
    this.boost = boost;
    this.amnt = 1;
    this.time = time;
  }

  public int getBoost() {
    return this.boost;
  }

  public int getTime() {
    return this.time;
  }

  public int getAmnt() {
    return this.amnt;
  }

  public void addAmnt() {
    this.amnt++;
  }

  public String toExport() {
    String boosterTime;
    int tempTime = this.time;
    if (tempTime == 60 || tempTime == 90 || tempTime == 120 || tempTime == 180
        || tempTime == 360 || tempTime == 480 || tempTime == 720 || tempTime == 1440) {
      if (tempTime == 60) {
        boosterTime = "1 Stunde";
      } else if (tempTime == 90) {
        boosterTime = "90 Minuten";
      } else {
        boosterTime = (this.time / 60) + " Stunden";
      }
    } else {
      boosterTime = this.time + " Minuten";
    }
    return this.amnt + "x;" + this.boost + ";(" + boosterTime + ")";
  }

}