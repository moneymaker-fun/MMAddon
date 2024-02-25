package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.util.HashMap;
import java.util.UUID;

public class AddonUtil {

  private MoneyMakerAddon addon;

  public AddonUtil(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  public static HashMap<UUID, MoneyPlayer> playerStatus = new HashMap<>();

  private boolean inMine = false;
  private boolean inFarming = false;
  private MiningCave miningCave = MiningCave.UNKNOWN;

  public void resetValues(boolean changePlaying) {
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
    this.addon.addonSettings().breakGoalEnabled(false);
    this.addon.addonSettings().breakGoal(0);
    breakGoalBlocks = 0;
    workerCount = 0;
    nextWorkerCost = "X";
    debrisCost = "X";
    debrisNotifySent = false;
    workerNotifySent = false;
    savedSwordRanking = 0;
    swordRanking = 0;
    swordMobs = 0;
    mobKills = 0;
    sessionKills = 0;
  }

  private String balance = "X";
  private int rank = 0;

  private int pickaxeLevel = 0;
  private int pickaxeRanking = 0;

  private int brokenBlocks = 0;
  private int sessionBlocks = 0;
  private int currentBrokenBlocks = 0;

  private int breakGoalBlocks = 0;

  private int workerCount = 0;

  private String nextWorkerCost = "X";
  private String debrisCost = "X";

  private int savedSwordRanking = 0;
  private int swordRanking = 0;
  private int swordMobs = 0;
  private int mobKills = 0;
  private int sessionKills = 0;

  private int debrisTime = 0;

  // Util

  private boolean workerNotifySent = false;
  private boolean debrisNotifySent = false;

  public MiningCave miningCave() {
    return miningCave;
  }

  public void miningCave(MiningCave miningCave) {
    this.miningCave = miningCave;
  }

  public boolean connectedToMoneyMaker() {
    return inMine || inFarming;
  }

  public boolean inMine() {
    return inMine;
  }

  public void inMine(boolean inMine) {
    this.inMine = inMine;
  }

  public boolean inFarming() {
    return inFarming;
  }

  public void inFarming(boolean inFarming) {
    this.inFarming = inFarming;
  }

  public String balance() {
    return balance;
  }

  public void balance(String balance) {
    this.balance = balance;
  }

  public int rank() {
    return rank;
  }

  public void rank(int rank) {
    this.rank = rank;
  }

  public int pickaxeLevel() {
    return pickaxeLevel;
  }

  public void pickaxeLevel(int pickaxeLevel) {
    this.pickaxeLevel = pickaxeLevel;
  }

  public int pickaxeRanking() {
    return pickaxeRanking;
  }

  public void pickaxeRanking(int pickaxeRanking) {
    this.pickaxeRanking = pickaxeRanking;
  }

  public int brokenBlocks() {
    return brokenBlocks;
  }

  public void brokenBlocks(int brokenBlocks) {
    this.brokenBlocks = brokenBlocks;
  }

  public int sessionBlocks() {
    return sessionBlocks;
  }

  public void sessionBlocks(int sessionBlocks) {
    this.sessionBlocks = sessionBlocks;
  }

  public int currentBrokenBlocks() {
    return currentBrokenBlocks;
  }

  public void currentBrokenBlocks(int currentBrokenBlocks) {
    this.currentBrokenBlocks = currentBrokenBlocks;
  }

  public int breakGoalBlocks() {
    return breakGoalBlocks;
  }

  public void breakGoalBlocks(int breakGoalBlocks) {
    this.breakGoalBlocks = breakGoalBlocks;
  }

  public int workerCount() {
    return workerCount;
  }

  public void workerCount(int workerCount) {
    this.workerCount = workerCount;
  }

  public String nextWorkerCost() {
    return nextWorkerCost;
  }

  public void nextWorkerCost(String nextWorkerCost) {
    this.nextWorkerCost = nextWorkerCost;
  }

  public String debrisCost() {
    return debrisCost;
  }

  public void debrisCost(String debrisCost) {
    this.debrisCost = debrisCost;
  }

  public int savedSwordRanking() {
    return savedSwordRanking;
  }

  public void savedSwordRanking(int savedSwordRanking) {
    this.savedSwordRanking = savedSwordRanking;
  }

  public int swordRanking() {
    return swordRanking;
  }

  public void swordRanking(int swordRanking) {
    this.swordRanking = swordRanking;
  }

  public int swordMobs() {
    return swordMobs;
  }

  public void swordMobs(int swordMobs) {
    this.swordMobs = swordMobs;
  }

  public int mobKills() {
    return mobKills;
  }

  public void mobKills(int mobKills) {
    this.mobKills = mobKills;
  }

  public int sessionKills() {
    return sessionKills;
  }

  public void sessionKills(int sessionKills) {
    this.sessionKills = sessionKills;
  }

  public int debrisTime() {
    return debrisTime;
  }

  public void debrisTime(int debrisTime) {
    this.debrisTime = debrisTime;
  }

  public boolean workerNotifySent() {
    return workerNotifySent;
  }

  public void workerNotifySent(boolean workerNotifySent) {
    this.workerNotifySent = workerNotifySent;
  }

  public boolean debrisNotifySent() {
    return debrisNotifySent;
  }

  public void debrisNotifySent(boolean debrisNotifySent) {
    this.debrisNotifySent = debrisNotifySent;
  }

  public enum MiningCave {
    GOLD("moneymaker.farming-level.gold"),
    COAL("moneymaker.farming-level.coal"),
    IRON("moneymaker.farming-level.iron"),
    UNKNOWN("moneymaker.farming-level.unknown");

    private String translation;

    MiningCave(String translation) {
      this.translation = translation;
    }

    public String translation() {
      return translation;
    }
  }

}
