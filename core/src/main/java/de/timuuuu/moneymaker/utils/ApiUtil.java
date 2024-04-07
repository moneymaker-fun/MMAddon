package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.LeaderboardEntryWidget;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import de.timuuuu.moneymaker.event.EventChatListener;
import de.timuuuu.moneymaker.event.EventChatMessages;
import de.timuuuu.moneymaker.event.hudwidget.ChristmasEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.EasterEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.FruitsHudWidget;
import de.timuuuu.moneymaker.event.hudwidget.HalloweenEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.ValentineEventWidget;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.AddonUtil.MiningCave;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.io.web.request.Request;

public class ApiUtil {

  private String BASE_URL = "https://api.moneymaker.fun";

  private MoneyMakerAddon addon;

  public ApiUtil(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  public void loadSettings() {
    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "/settings/")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if(response.getStatusCode() != 200 || response.hasException()) {
            return;
          }

          JsonObject object = response.get();

          if (object.has("motd") && object.get("motd").isJsonPrimitive()) {
            this.addon.addonUtil().motd(object.get("motd").getAsString());
          }

          if (object.has("settings") && object.get("settings").isJsonObject()) {
            JsonObject settingsObject = object.get("settings").getAsJsonObject();

            if (settingsObject.has("event")) {
              String event = settingsObject.get("event").getAsString();
              this.addon.addonUtil().currentEvent(event);

              if(!event.equals(EventChatMessages.EVENT_NONE.message())) {
                this.addon.logger().info("[MoneyMaker - Event] Loaded Event Type '" + event + "' as current Event");
                this.addon.labyAPI().eventBus().registerListener(new EventChatListener(this.addon));

                if(event.equals(EventChatMessages.EVENT_VALENTINE.message())) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new ValentineEventWidget(this.addon)));
                  this.addon.logger().info("Registered Valentine Event Widget...");
                }
                if(event.equals(EventChatMessages.EVENT_EASTER.message())) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new EasterEventWidget(this.addon)));
                  this.addon.logger().info("Registered Easter Event Widget...");
                }
                if(event.equals(EventChatMessages.EVENT_SUMMER.message()) || event.equals(EventChatMessages.EVENT_CARIBBEAN.message())) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new FruitsHudWidget(this.addon)));
                  this.addon.logger().info("Registered Summer/Caribbean Event Widget...");
                }
                if(event.equals(EventChatMessages.EVENT_HALLOWEEN.message())) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new HalloweenEventWidget(this.addon)));
                  this.addon.logger().info("Registered Halloween Event Widget...");
                }
                if(event.equals(EventChatMessages.EVENT_CHRISTMAS.message())) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new ChristmasEventWidget(this.addon)));
                  this.addon.logger().info("Registered Christmas Event Widget...");
                }
                if(event.equals(EventChatMessages.EVENT_ALL.message())) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {
                    this.addon.labyAPI().hudWidgetRegistry().register(new ValentineEventWidget(this.addon));
                    this.addon.labyAPI().hudWidgetRegistry().register(new EasterEventWidget(this.addon));
                    this.addon.labyAPI().hudWidgetRegistry().register(new FruitsHudWidget(this.addon));
                    this.addon.labyAPI().hudWidgetRegistry().register(new HalloweenEventWidget(this.addon));
                    this.addon.labyAPI().hudWidgetRegistry().register(new ChristmasEventWidget(this.addon));
                  });
                }

              }

            }

          }
        });
  }

  public void loadCoordinates() {
    AtomicBoolean failed = new AtomicBoolean(false);
    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "/locations/")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if (response.getStatusCode() != 200 || response.hasException()) {
            this.addon.addonSettings().setFallbackCoordinates(true);
            return;
          }

          JsonObject object = response.get();

          if(object.has("workers") && object.get("workers").isJsonArray()) {
            JsonArray array = object.get("workers").getAsJsonArray();
            array.forEach(jsonElement -> {
              if(jsonElement.isJsonObject()) {
                JsonObject workerObject = jsonElement.getAsJsonObject();
                if(workerObject.has("x")) {
                  AddonSettings.workerCoordinates.get("x").add(workerObject.get("x").getAsFloat());
                }
                if(workerObject.has("z")) {
                  AddonSettings.workerCoordinates.get("z").add(workerObject.get("z").getAsFloat());
                }
              }
            });
            this.addon.logger().debug("[MoneyMaker] Loaded Worker Coordinates from API.");
          } else {
            failed.set(true);
          }

          if(object.has("debris") && object.get("debris").isJsonArray()) {
            JsonArray array = object.get("debris").getAsJsonArray();
            array.forEach(jsonElement -> {
              if(jsonElement.isJsonObject()) {
                JsonObject workerObject = jsonElement.getAsJsonObject();
                if(workerObject.has("x")) {
                  AddonSettings.debrisCoordinates.get("x").add(workerObject.get("x").getAsFloat());
                }
                if(workerObject.has("z")) {
                  AddonSettings.debrisCoordinates.get("z").add(workerObject.get("z").getAsFloat());
                }
              }
            });
            this.addon.logger().debug("[MoneyMaker] Loaded Debris Coordinates from API.");

          } else {
            failed.set(true);
          }

          if(object.has("cave_levels") && object.get("cave_levels").isJsonArray()) {
            JsonArray array = object.get("cave_levels").getAsJsonArray();
            array.forEach(jsonElement -> {
              if(jsonElement.isJsonObject()) {
                JsonObject levelObject = jsonElement.getAsJsonObject();
                if (levelObject.has("name") && levelObject.has("min") && levelObject.has("max")) {
                  MiningCave cave = this.addon.addonUtil().caveByName(levelObject.get("name").getAsString());
                  if(cave != MiningCave.UNKNOWN) {
                    cave.minY(levelObject.get("min").getAsFloat());
                    cave.maxY(levelObject.get("max").getAsFloat());
                  }
                }
              }
            });
            this.addon.logger().debug("[MoneyMaker] Loaded Cave Levels from API.");
          }
        });

    if(failed.get()) {
      this.addon.addonSettings().setFallbackCoordinates(true);
    }

  }

  public void loadChatHistory() {
    if(!this.addon.configuration().chatConfiguration.loadChatHistory().get()) return;
    Request.ofGson(JsonArray.class)
        .url(BASE_URL + "/chat/history/")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if(response.getStatusCode() != 200 || response.hasException()) {
            this.addon.pushNotification(Component.text("Chat History", NamedTextColor.DARK_RED), Component.text("Failed to load Chat History from Server", NamedTextColor.RED));
            this.addon.logger().error("Chat Server Message History Error: ", response.exception());
            return;
          }
          JsonArray array = response.get();
          List<MoneyChatMessage> messages = new ArrayList<>();
          array.forEach(jsonElement -> {
            if(jsonElement.isJsonObject()) {
              JsonObject object = jsonElement.getAsJsonObject();
              if(object.has("UUID") && object.has("UserName") && object.has("Message")
                  && object.has("Rang") && object.has("MessageID") && object.has("formatted_timestamp")) {
                if(!object.get("MessageID").getAsString().isEmpty()) {
                  JsonObject chatMessage = new JsonObject();
                  chatMessage.addProperty("messageId", object.get("MessageID").getAsString());
                  chatMessage.addProperty("uuid", object.get("UUID").getAsString());
                  chatMessage.addProperty("userName", object.get("UserName").getAsString());
                  chatMessage.addProperty("message", object.get("Message").getAsString());
                  chatMessage.addProperty("rank", object.get("Rang").getAsString());
                  chatMessage.addProperty("fromCache", true);
                  chatMessage.addProperty("timeStamp", object.get("formatted_timestamp").getAsString());
                  messages.add(MoneyChatMessage.fromJson(chatMessage));
                }
              }
            }
          });
          if(!messages.isEmpty()) {
            Collections.reverse(messages);
            messages.forEach(message -> this.addon.chatActivity().addChatMessage(message));
          }
        });
  }

  public void loadLeaderboard(boolean update) {
    this.addon.leaderboardActivity().entries().clear();
    Request.ofGson(JsonArray.class)
        .url(BASE_URL + "/leaderboard/")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if(response.getStatusCode() != 200 || response.hasException()) {
            this.addon.pushNotification(Component.text("Leaderboard", NamedTextColor.DARK_RED), Component.text("Failed to load Leaderboard from Server", NamedTextColor.RED));
            this.addon.logger().error("Leaderboard Error: ", response.exception());
            return;
          }
          JsonArray array = response.get();
          array.forEach(jsonElement -> {
            if(jsonElement.isJsonObject()) {
              JsonObject object = jsonElement.getAsJsonObject();
              if(object.has("UUID") && object.has("UserName") && object.has("Ranking")
                  && object.has("Blocks") && object.has("Pickaxe_Ranking") && object.has("Sword_Ranking")) {
                this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {
                  this.addon.leaderboardActivity().entries().add(new LeaderboardEntryWidget(
                      this.addon,
                      UUID.fromString(object.get("UUID").getAsString()),
                      object.get("UserName").getAsString(),
                      object.get("Ranking").getAsInt(),
                      object.get("Blocks").getAsInt(),
                      object.get("Pickaxe_Ranking").getAsInt(),
                      object.get("Sword_Ranking").getAsInt()
                  ));
                });
              }
            }
          });
          if(update) {
            this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.leaderboardActivity().reload());
          }
        });
  }

}
