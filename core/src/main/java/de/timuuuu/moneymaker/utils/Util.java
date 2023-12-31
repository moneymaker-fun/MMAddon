package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import de.timuuuu.moneymaker.activities.popup.FeedbackActivity;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.theme.Theme;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.activity.Document;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.OperatingSystem;

public class Util {

  public static HashMap<String, MoneyTimer> timers = new HashMap<>();

  public static void addFeedbackButton(Document document) {
    ButtonWidget feedbackButton = ButtonWidget.text("§6Feedback §7/ §cBugreport");
    feedbackButton.setPressable(() -> {
      //OperatingSystem.getPlatform().openUrl("https://moneymaker.fun/?page=feedback&minecraft-name="+Laby.labyAPI().getName()+"&minecraft-version="+Laby.labyAPI().minecraft().getVersion());
      Laby.labyAPI().minecraft().executeNextTick(() -> {
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new FeedbackActivity(MoneyMakerAddon.instance(), Laby.labyAPI().minecraft().minecraftWindow().currentScreen()));
      });
    });
    feedbackButton.addId("feedback-button");
    document.addChild(feedbackButton);
  }

  public static Widget addDiscordButton() {
    ButtonWidget discordButton = ButtonWidget.i18n("moneymaker.ui.start.discord", Icon.texture(
        ResourceLocation.create("moneymaker", "textures/ui/discord.png"))).addId("discord-btn");
    discordButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://discord.gg/XKjAZFgknd");
    });
    return discordButton;
  }

  public static void drawAuthor(LabyAPI labyAPI, Bounds bounds, Stack stack) {
    TextRenderer textRenderer = labyAPI.renderPipeline().textRenderer();
    Theme currentTheme = labyAPI.themeService().currentTheme();

    if(currentTheme.getId().equalsIgnoreCase("fancy")) {
      textRenderer.text(RenderableComponent.of(
          Component.text("Addon-Version", NamedTextColor.GRAY)
              .append(Component.text(": ", NamedTextColor.DARK_GRAY))
              .append(Component.text(MoneyMakerAddon.instance().addonInfo().getVersion(), NamedTextColor.YELLOW))
          ))
          .scale(0.8f)
          .pos(5, bounds.getHeight() -17)
          .render(stack);
      textRenderer.text(RenderableComponent.of(
          Component.text("Developed by ", NamedTextColor.GRAY)
              .append(Component.text("Timuuuu", NamedTextColor.YELLOW))
              .append(Component.text(" & ", NamedTextColor.GRAY))
              .append(Component.text("MisterCore", NamedTextColor.YELLOW))
          ))
          .scale(0.8f)
          .pos(5, bounds.getHeight() -7)
          .render(stack);
    } else {
      textRenderer.text(RenderableComponent.of(
              Component.text("Addon-Version", NamedTextColor.GRAY)
                  .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                  .append(Component.text(MoneyMakerAddon.instance().addonInfo().getVersion(), NamedTextColor.YELLOW))
          ))
          .scale(0.8f)
          .pos(5, bounds.getHeight() -18)
          .render(stack);
      textRenderer.text(RenderableComponent.of(
              Component.text("Developed by ", NamedTextColor.GRAY)
                  .append(Component.text("Timuuuu", NamedTextColor.YELLOW))
                  .append(Component.text(" & ", NamedTextColor.GRAY))
                  .append(Component.text("MisterCore", NamedTextColor.YELLOW))
          ))
          .scale(0.8f)
          .pos(5, bounds.getHeight() -8)
          .render(stack);
    }
  }

  public static String format(int toFormate) {
    return new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN)).format(toFormate);
  }

  public static boolean isDev(String uuid) {
    return uuid.equals("308893af-77af-4706-ac8a-1c4830038108") || uuid.equals("966b5d5e-2577-4ab7-987a-89bfa59da74a");
  }

  public static boolean isStaff(UUID uuid) {
    if(!AddonSettings.playerStatus.containsKey(uuid)) return false;
    return AddonSettings.playerStatus.get(uuid).rank().isStaff();
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
        seconds = Integer.parseInt(split[1]);
        seconds += Integer.parseInt(split[0])*60;
      } else {
        seconds = Integer.parseInt(split[2]);
        seconds += Integer.parseInt(split[1])*60;
        seconds += Integer.parseInt(split[0])*60*60;
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

}
