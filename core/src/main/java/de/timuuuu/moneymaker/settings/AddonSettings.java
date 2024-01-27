package de.timuuuu.moneymaker.settings;


import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.HashMap;
import java.util.UUID;

public class AddonSettings {

  public static String prefix = "§8‖ §6MoneyMaker §8» §7";

  public static boolean inMine = false;
  public static boolean inFarming = false;

  public static int CHECK_TICK = 5*20; // 5*20 (5 seconds)
  public static int CHECK_RENDER = 20; // every 20 renders

  public static HashMap<UUID, MoneyPlayer> playerStatus = new HashMap<>();

  public static void selectUpdateMode(UpdateMode updateMode) {
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

  public static void resetValues(boolean changePlaying) {
    if(changePlaying) {
      inMine = false;
      inFarming = false;
    }
    balance = "X";
    rank = 0;
    pickaxeLevel = 0;
    pickaxeRanking = 0;
    brokenBlocks = 0;
    sessionBlocks = 0;
    breakGoalEnabled = false;
    breakGoal = 0;
    breakGoalBlocks = 0;
    workerCount = 0;
    nextWorkerCost = "X";
    debrisCost = "X";
    debrisNotifySent = false;
    workerNotifySent = false;
  }

  public static String balance = "X";
  public static int rank = 0;

  public static int pickaxeLevel = 0;
  public static int pickaxeRanking = 0;

  public static int brokenBlocks = 0;
  public static int sessionBlocks = 0;
  public static int currentBrokenBlocks = 0;

  public static boolean breakGoalEnabled = false;
  public static int breakGoal = 0;
  public static int breakGoalBlocks = 0;

  public static int workerCount = 0;

  public static String nextWorkerCost = "X";
  public static String debrisCost = "X";

  public static int swordRanking = 0;
  public static int swordMobs = 0;
  public static int mobKills = 0;
  public static int sessionKills = 0;

  public static int debrisTime = 0;

  // Util

  public static boolean workerNotifySent = false;
  public static boolean debrisNotifySent = false;

  // enums

  public enum FarmingReset {
    AUTOMATICALLY, ASK, HIDE
  }

  public enum UpdateMode {
    INSTANT, FAST, NORMAL, SLOW
  }

}