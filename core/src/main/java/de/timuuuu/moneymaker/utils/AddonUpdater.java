package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import net.labymod.api.Laby;

public class AddonUpdater {

  public static boolean notified = false;

  private static String currentVersion = "";
  private static String newestVersion = "";
  private static boolean devEnvironment = false;

  private static Path gameDirectory = Laby.labyAPI().labyModLoader().getGameDirectory();
  private static Path updatePath = gameDirectory.resolve("MoneyMaker/Updater.jar");

  public static void checkVersion() {
    currentVersion = MoneyMakerAddon.instance().addonInfo().getVersion();
    devEnvironment = Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment();
    if(devEnvironment) return;
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL("https://moneymaker.fun/download/version.txt").openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "LabyMod 4 Addon");
      connection.setRequestProperty("Content-type", "application/json");
      if(connection.getResponseCode() != 200) {
        MoneyMakerAddon.instance().logger().error("Unable to contact MoneyMaker Update Server [Response: " + connection.getResponseCode() + "]");
        return;
      }
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      while ((inputLine = bufferedReader.readLine()) != null) {
        newestVersion = inputLine;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    if(!newestVersion.isEmpty() && !currentVersion.equals(newestVersion)) {
      MoneyMakerAddon.instance().logger().info("[MoneyMaker-Updater] Found new Version online. Schedule updater on exit.");
    }
  }

  public static void executeUpdater() {
    if(devEnvironment) return;
    if(currentVersion.equals(newestVersion)) return;
    try {
      new ProcessBuilder("java", "-jar", updatePath.toFile().getAbsolutePath(),
              "--gameDir=" + gameDirectory.toFile().getAbsolutePath()).start();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public static void downloadUpdater() {
    try {
      File folder = new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile(), "MoneyMaker");
      if(!folder.exists()) {
        folder.mkdir();
      }
      FileOutputStream outputStream = new FileOutputStream(new File(folder, "Updater.jar"));
      HttpURLConnection connection = (HttpURLConnection) new URL("https://moneymaker.fun/download/Updater.jar").openConnection();
      connection.setRequestProperty("User-Agent", "LabyMod 4 Addon");
      if(connection.getResponseCode() != 200) return;
      ReadableByteChannel channel = Channels.newChannel(connection.getInputStream());
      outputStream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
      outputStream.close();
      MoneyMakerAddon.instance().logger().info("Downloaded latest MoneyMaker Updater...");
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public static boolean updateAvailable() {
    if(devEnvironment) return false;
    return !currentVersion.equals(newestVersion);
  }

}
