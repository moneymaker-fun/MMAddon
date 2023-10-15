package de.timuuuu.moneymaker.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.Laby;

public class AddonUpdater {

  private static String currentVersion = "";
  private static String newestVersion = "";
  private static boolean devEnvironment = false;

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

  private Thread thread = null;

  public AddonUpdater() {
    if(devEnvironment) return;
    if(currentVersion.equals(newestVersion)) return;
    this.thread = new Thread(() -> {
      try {
        File folder = Laby.labyAPI().labyModLoader().getGameDirectory().toFile();
        File updaterFile = new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile().getPath(), "MoneyMaker/Updater.jar");
        new ProcessBuilder(new String[]{ "java", "-jar", updaterFile.getAbsolutePath(), "--gameDir="+folder.getAbsolutePath() }).start();
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    });
    this.update();
  }

  private void update() {
    try {
      File folder = new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile(), "MoneyMaker");
      if(!folder.exists()) {
        folder.mkdir();
      }
      FileOutputStream outputStream = new FileOutputStream(new File(folder, "Updater.jar"));
      HttpURLConnection connection = (HttpURLConnection) new URL("https://moneymaker.fun/download/Updater.jar").openConnection();
      connection.setRequestProperty("User-Agent", "LabyMod 4 Addon");
      ReadableByteChannel channel = Channels.newChannel(connection.getInputStream());
      outputStream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
      outputStream.close();
      System.out.println("Downloaded latest MoneyMaker Updater...");
      if(this.thread != null) {
        this.thread.start();
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

}
