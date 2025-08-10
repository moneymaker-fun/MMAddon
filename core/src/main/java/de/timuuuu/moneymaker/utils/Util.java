package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.utils.AddonUtil.FarmingCave;
import de.timuuuu.moneymaker.utils.AddonUtil.MineType;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.models.OperatingSystem;

public class Util {

  public static HashMap<String, MoneyTimer> timers = new HashMap<>();

  public static String currentServer() {
    if(MoneyMakerAddon.instance().addonUtil().inMine()) return currentMine(MoneyMakerAddon.instance().addonUtil().currentMine());
    if(MoneyMakerAddon.instance().addonUtil().inFarming()) return currentCave(MoneyMakerAddon.instance().addonUtil().farmingCave());
    return "Other";
  }

  public static String currentMine(MineType mine) {
    if(mine == null) return "Mine";
    if(!MoneyMakerAddon.instance().configuration().chatConfiguration.showDetailedLocation().get()) {
      return "Mine";
    }
    return "Mine - " + mine.internalName();
  }

  public static String currentCave(FarmingCave cave) {
    if(!MoneyMakerAddon.instance().configuration().chatConfiguration.showDetailedLocation().get()) {
      return "Farming";
    }
    return "Farming - " + cave.internalName();
  }

  public static ButtonWidget feedbackButton() {
    ButtonWidget feedbackButton = ButtonWidget.component(
        Component.text("Feedback", NamedTextColor.GOLD).append(Component.text(" / ", NamedTextColor.GRAY)).append(Component.text("Bugreport", NamedTextColor.RED)),
        SpriteCommon.BUG
    ).addId("feedback-button");
    feedbackButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://moneymakeraddon.de/feedback?mcname=" + Laby.labyAPI().getName() + "&mcversion=" + Laby.labyAPI().minecraft().getVersion());
      //Laby.labyAPI().minecraft().executeNextTick(() -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new FeedbackActivity(MoneyMakerAddon.instance(), Laby.labyAPI().minecraft().minecraftWindow().currentScreen())));
    });
    return feedbackButton;
  }

  public static ButtonWidget discordButton() {
    ButtonWidget discordButton = ButtonWidget.i18n("moneymaker.ui.button.discord", SpriteCommon.DISCORD).addId("discord-button");
    discordButton.setPressable(() -> OperatingSystem.getPlatform().openUrl("https://discord.moneymakeraddon.de/"));
    return discordButton;
  }

  public static ButtonWidget leaderboardButton() {
    ButtonWidget leaderboardButton = ButtonWidget.i18n("moneymaker.ui.button.leaderboard").addId("leaderboard-button");
    leaderboardButton.setPressable(() -> OperatingSystem.getPlatform().openUrl("https://moneymakeraddon.de/leaderboard/"));
    return leaderboardButton;
  }

  public static void addCredits(MoneyMakerAddon addon, Document document) {
    DivWidget container = new DivWidget().addId("mm-credit-container");

    ComponentWidget addonVersionWidget = ComponentWidget.component(Component.text("Addon-Version", NamedTextColor.GRAY)
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text(addon.addonInfo().getVersion(), NamedTextColor.YELLOW))
    ).addId("credits-addon-version");
    container.addChild(addonVersionWidget);

    ComponentWidget developerWidget = ComponentWidget.component(Component.text("Developed by ", NamedTextColor.GRAY)
        .append(Component.text("MisterCore", NamedTextColor.YELLOW))
        .append(Component.text(" & ", NamedTextColor.GRAY))
        .append(Component.text("Seelenverwandter", NamedTextColor.YELLOW))
    ).addId("credits-developer");
    container.addChild(developerWidget);

    document.addChild(container);
  }

  public static String format(int toFormate) {
    return new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN)).format(toFormate);
  }

  public static int parseInteger(String input, Class clazz) throws NumberFormatException {
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException numberFormatException) {
      MoneyMakerAddon.instance().logger().warn("Unable to parse Input to Integer (Input: '" + input + "', Used in '" + clazz.getName() + "')");
      throw numberFormatException;
    }
  }

  public static boolean isAdmin(UUID uuid) {
    if(!AddonUtil.playerStatus.containsKey(uuid)) return false;
    return AddonUtil.playerStatus.get(uuid).rank() == MoneyRank.ADMIN;
  }

  public static boolean isStaff(UUID uuid) {
    if(!AddonUtil.playerStatus.containsKey(uuid)) return false;
    return AddonUtil.playerStatus.get(uuid).rank().isStaff();
  }

  public static int timeToInt(String input, boolean hours) {
    // Incoming format
    // Normal >> 20:00
    // Hours >> 15:50:26
    if(!input.contains(":")) return 0;
    String[] split = input.split(":");
    if(hours) {
      if(split.length != 3) return 0;
    } else {
      if(split.length != 2) return 0;
    }

    int seconds = 0;
    try {
      if(!hours) {
        seconds = parseInteger(split[1], Util.class);
        seconds += parseInteger(split[0], Util.class)*60;
      } else {
        seconds = parseInteger(split[2], Util.class);
        seconds += parseInteger(split[1], Util.class)*60;
        seconds += parseInteger(split[0], Util.class)*60*60;
      }
    } catch (NumberFormatException ignored) {}
    return seconds;
  }

  public static String intToTime(int time) {
    long seconds = time;
    long minutes = 0;
    long hours = 0;
    while (seconds >= 60) {
      seconds-=60;
      minutes++;
    }
    while (minutes >= 60) {
      minutes-=60;
      hours++;
    }
    String secString = String.valueOf(seconds);
    String minString = String.valueOf(minutes);
    String hourString = String.valueOf(hours);
    if(hourString.length() == 1) {
      hourString = "0" + hourString;
    }
    if(minString.length() == 1) {
      minString = "0" + minString;
    }
    if(secString.length() == 1) {
      secString = "0" + secString;
    }
    return hourString + ":" + minString + ":" + secString;
  }

  public static List<String> getTextFromJsonObject(String input) {
    List<String> list = new ArrayList<>();
    try {
      JsonObject object = JsonParser.parseString(input).getAsJsonObject();
      if(object.has("extra") && object.get("extra").isJsonArray()) {
        JsonArray array = object.get("extra").getAsJsonArray();
        for(int i = 0; i != array.size(); i++) {
          if(array.get(i).isJsonObject()) {
            if(array.get(i).getAsJsonObject().has("text")) {
              list.add(array.get(i).getAsJsonObject().get("text").getAsString());
            }
          }
        }
      }
    } catch (JsonSyntaxException ignored) {

    }
    return list;
  }

  public static String convertToReadableFormat(long milliseconds) {
    long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
    long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;

    if (hours > 0) {
      return hours + "h " + minutes + "m";
    } else {
      if (minutes > 0) {
        return minutes + "m " + seconds + "s";
      } else {
        return seconds + "s";
      }
    }
  }
}
