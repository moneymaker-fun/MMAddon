package de.timuuuu.moneymaker.utils;


import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AddonSettings {
  public static String prefix = "§bMoneyMaker > ";
  public static String playingOn = "Hauptmenü";
  public static AtomicInteger id = new AtomicInteger(5);
  public static boolean showJoins = false;

  public static HashMap<UUID, MoneyPlayer> playerStatus = new HashMap<>();


  public static void resetValues() {
    playingOn = "Hauptmenü";
    gommeConnected = false;
    balance = "X";
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


  public static boolean gommeConnected = false;

  public static String balance = "X";

  public static int brokenBlocks = 0;
  public static int sessionBlocks = 0;

  public static boolean breakGoalEnabled = false;
  public static int breakGoal = 0;
  public static int breakGoalBlocks = 0;

  public static int workerCount = 0;

  public static String nextWorkerCost = "X";
  public static String debrisCost = "X";

  public static String swordRanking = "X";
  public static String swordMobs = "X";
  public static int mobKills = 0;
  public static int sessionKills = 0;

  public static int debrisTime = 0;

  // Util

  public static boolean workerNotifySent = false;
  public static boolean debrisNotifySent = false;

}