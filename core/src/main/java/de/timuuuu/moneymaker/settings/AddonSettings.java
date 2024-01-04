package de.timuuuu.moneymaker.settings;


import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.HashMap;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;

public class AddonSettings {

  public static TextComponent prefix = Component.text("§8‖ §6MoneyMaker §8» §7");

  public static boolean inMine = false;
  public static boolean inFarming = false;

  public static HashMap<UUID, MoneyPlayer> playerStatus = new HashMap<>();

  public static void resetValues(boolean changePlaying) {
    if(changePlaying) {
      inMine = false;
      inFarming = false;
    }
    balance = "X";
    rank = "X";
    pickaxeLevel = "X";
    pickaxeRanking = "X";
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
  public static String rank = "X";

  public static String pickaxeLevel = "X";
  public static String pickaxeRanking = "X";

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

}