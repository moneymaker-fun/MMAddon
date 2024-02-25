package de.timuuuu.moneymaker.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddonSettings {

  public static HashMap<String, List<Float>> workerCoordinates = new HashMap<>();
  public static HashMap<String, List<Float>> debrisCoordinates = new HashMap<>();

  private int CHECK_TICK = 5*20; // 5*20 (5 seconds)
  private int CHECK_RENDER = 20; // every 20 renders

  private boolean breakGoalEnabled = false;
  private int breakGoal = 0;

  public void setFallbackCoordinates(boolean fill) {
    workerCoordinates.put("x", new ArrayList<>());
    workerCoordinates.put("z", new ArrayList<>());
    debrisCoordinates.put("x", new ArrayList<>());
    debrisCoordinates.put("z", new ArrayList<>());
    if(fill) {
      workerCoordinates.get("x").addAll(Arrays.asList(2.5F, 1001.5F));
      workerCoordinates.get("z").addAll(Arrays.asList(-1.5F, 6.5F));
      debrisCoordinates.get("x").addAll(Arrays.asList(5.5F, 1004.5F));
      debrisCoordinates.get("z").addAll(Arrays.asList(-5.5F, 1.5F));
    }
  }

  public void selectUpdateMode(UpdateMode updateMode) {
    switch (updateMode) {
      case INSTANT -> {
        CHECK_TICK = 1; // check every tick
        CHECK_RENDER = 1; // check every render
      }
      case FAST -> {
        CHECK_TICK = 3*20; // check every 3 seconds
        CHECK_RENDER = 10; // check every 10 renders
      }
      case NORMAL -> {
        CHECK_TICK = 5*20; // check every 5 seconds
        CHECK_RENDER = 20; // check every 20 renders
      }
      case SLOW -> {
        CHECK_TICK = 10*20; // check every 20 seconds
        CHECK_RENDER = 60; // check every 60 renders
      }
    }
  }

  public int CHECK_RENDER() {
    return CHECK_RENDER;
  }

  public int CHECK_TICK() {
    return CHECK_TICK;
  }

  public boolean breakGoalEnabled() {
    return breakGoalEnabled;
  }

  public void breakGoalEnabled(boolean breakGoalEnabled) {
    this.breakGoalEnabled = breakGoalEnabled;
  }

  public int breakGoal() {
    return breakGoal;
  }

  public void breakGoal(int breakGoal) {
    this.breakGoal = breakGoal;
  }

  public enum FarmingReset {
    AUTOMATICALLY, ASK, HIDE
  }

  public enum UpdateMode {
    INSTANT, FAST, NORMAL, SLOW
  }

}