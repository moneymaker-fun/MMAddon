package de.timuuuu.moneymaker.utils;


import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AddonSettings {
  public static String prefix = "§bMoneyMaker > ";
  public static String playingOn = "Hauptmenü";
  public static AtomicInteger id = new AtomicInteger(5);
  public static boolean showJoins = false;

  public static HashMap<UUID, MoneyChatMessage> playerStatus = new HashMap<>();

  public static boolean gommeConnected = false;

}