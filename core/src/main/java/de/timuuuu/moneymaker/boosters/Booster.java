package de.timuuuu.moneymaker.boosters;

import net.labymod.api.util.I18n;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Booster {

  private static LinkedList<Booster> boosterList = new LinkedList<>();
  private static List<Booster> latestFoundBoosters = new ArrayList<>();

  public static AtomicInteger sessionBoost = new AtomicInteger(0);
  public static AtomicInteger sessionBoosters = new AtomicInteger();
  public static AtomicInteger activatedBoost = new AtomicInteger(0);

  public static void insertLatestBooster(int boost, int time) {
    if(latestFoundBoosters.size() <= 10) {
      latestFoundBoosters.add(new Booster(boost, time));
    } else {
      int lastIndex = latestFoundBoosters.size() -1;
      latestFoundBoosters.remove(lastIndex);
      latestFoundBoosters.add(new Booster(boost, time));
    }
  }

  public static void insertBooster(int boost, int time) {
    for(Booster booster : boosterList) {
      if(booster.boost() == boost && booster.time() == time) {
        booster.addAmount();
        return;
      }
    }
    String CURRENT_TIME = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    for (int i = 0; i < boosterList.size(); i++) {
      if (boosterList.get(i).boost() < boost) {
        boosterList.add(i, new Booster(boost, time, CURRENT_TIME));
        return;
      }
      if (boosterList.get(i).boost() == boost && boosterList.get(i).time() < time) {
        boosterList.add(i, new Booster(boost, time, CURRENT_TIME));
        return;
      }
    }
    boosterList.add(new Booster(boost, time, CURRENT_TIME));
  }

  public static LinkedList<Booster> boosterList() {
    return boosterList;
  }

  public static List<Booster> latestFoundBoosters() {
    return latestFoundBoosters;
  }

  private int boost;

  private int amount;

  private int time;
  private String farmDate = "";

  public Booster(int boost, int time) {
    this.boost = boost;
    this.amount = 1;
    this.time = time;
  }

  public Booster(int boost, int time, String farmDate) {
    this.boost = boost;
    this.amount = 1;
    this.time = time;
    this.farmDate = farmDate;
  }

  public int boost() {
    return this.boost;
  }

  public int time() {
    return this.time;
  }

  public String farmDate() {
    return farmDate;
  }

  public int amount() {
    return this.amount;
  }

  public void addAmount() {
    this.amount++;
  }

  public String readableTime() {
    String boosterTime;
    int tempTime = this.time;
    if (tempTime > 59) {
      if (tempTime == 90) {
        boosterTime = "90 " + I18n.translate("moneymaker.text.timeUnit.minutes");
      } else {
        int hours = this.time / 60;
        boosterTime = hours + " " + I18n.translate("moneymaker.text.timeUnit." + (hours == 1 ? "hour" : "hours"));
      }
    } else {
      boosterTime = this.time + " " + I18n.translate("moneymaker.text.timeUnit.minutes");
    }
    return boosterTime;
  }

  public String toExport() {
    return this.amount + "x;" + this.boost + ";(" + readableTime() + ")";
  }

}