package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.PriceOverviewActivity.MineData;
import de.timuuuu.moneymaker.activities.widgets.LeaderboardEntryWidget;
import de.timuuuu.moneymaker.moneychat.util.MoneyChatMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import de.timuuuu.moneymaker.event.EventChatListener;
import de.timuuuu.moneymaker.event.hudwidget.ChristmasEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.EasterEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.FruitsHudWidget;
import de.timuuuu.moneymaker.event.hudwidget.HalloweenEventWidget;
import de.timuuuu.moneymaker.event.hudwidget.ValentineEventWidget;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.AddonUtil.FarmingCave;
import de.timuuuu.moneymaker.utils.AddonUtil.MoneyMakerEvent;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.io.web.request.Request;

public class ApiUtil {

  public static final String BASE_URL = "https://api.moneymakeraddon.de";

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
        .userAgent("MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if(response.getStatusCode() != 200 || response.hasException()) {
            return;
          }

          JsonObject object = response.get();

          if (object.has("motd") && object.get("motd").isJsonObject()) {
            JsonObject motd = object.get("motd").getAsJsonObject();
            if(motd.has("text") && motd.has("priority")) {
              this.addon.addonUtil().motd(motd.get("text").getAsString());
              this.addon.addonUtil().motdPriority(motd.get("priority").getAsBoolean());
            }
          }

          if (object.has("availableLanguages") && object.get("availableLanguages").isJsonArray()) {
            object.get("availableLanguages").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonObject()) {
                JsonObject languageObject = jsonElement.getAsJsonObject();
                if(languageObject.has("name") && languageObject.has("language")) {
                  this.addon.chatMessageLoader().availableLanguages.put(languageObject.get("name").getAsString(), languageObject.get("language").getAsString());
                }
              }
            });
          }

          if(object.has("ignoredRankingValues") && object.get("ignoredRankingValues").isJsonArray()) {
            object.get("ignoredRankingValues").getAsJsonArray().forEach(jsonElement -> {
              if(jsonElement.isJsonPrimitive()) {
                this.addon.addonUtil().ignoredRankingValues().add(jsonElement.getAsString());
              }
            });
          }

          if (object.has("settings") && object.get("settings").isJsonObject()) {
            JsonObject settingsObject = object.get("settings").getAsJsonObject();

            if (settingsObject.has("event")) {
              MoneyMakerEvent event = this.addon.addonUtil().eventByName(settingsObject.get("event").getAsString());
              this.addon.addonUtil().currentEvent(event);

              if(event != MoneyMakerEvent.NONE) {
                this.addon.logger().info("[MoneyMaker - Event] Loaded Event Type '" + event + "' as current Event");
                this.addon.labyAPI().eventBus().registerListener(new EventChatListener(this.addon));

                if(event == MoneyMakerEvent.VALENTINE) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new ValentineEventWidget(this.addon)));
                  this.addon.logger().info("Registered Valentine Event Widget...");
                }
                if(event == MoneyMakerEvent.EASTER) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new EasterEventWidget(this.addon)));
                  this.addon.logger().info("Registered Easter Event Widget...");
                }
                if(event == MoneyMakerEvent.SUMMER || event == MoneyMakerEvent.CARIBBEAN) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new FruitsHudWidget(this.addon)));
                  this.addon.logger().info("Registered Summer/Caribbean Event Widget...");
                }
                if(event == MoneyMakerEvent.HALLOWEEN) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new HalloweenEventWidget(this.addon)));
                  this.addon.logger().info("Registered Halloween Event Widget...");
                }
                if(event == MoneyMakerEvent.CHRISTMAS) {
                  this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.labyAPI().hudWidgetRegistry().register(new ChristmasEventWidget(this.addon)));
                  this.addon.logger().info("Registered Christmas Event Widget...");
                }
                if(event == MoneyMakerEvent.ALL) {
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

          if(object.has("prices") && object.get("prices").isJsonObject()) {
            JsonObject pricesObject = object.get("prices").getAsJsonObject();

            if(pricesObject.has("Goldmine") && pricesObject.get("Goldmine").isJsonObject()) {
              JsonObject goldmineObject = pricesObject.get("Goldmine").getAsJsonObject();
              if(goldmineObject.has("cost") && goldmineObject.has("workers")) {

                String cost = goldmineObject.get("cost").getAsString();
                List<String> workers = new ArrayList<>();
                if(goldmineObject.get("workers").isJsonArray()) {
                  JsonArray array = goldmineObject.get("workers").getAsJsonArray();
                  array.forEach(jsonElement -> {
                    if(jsonElement.isJsonObject()) {
                      JsonObject workerObject = jsonElement.getAsJsonObject();
                      if(workerObject.has("name") && workerObject.has("costs")) {
                        workers.add(workerObject.get("name").getAsString() + ";" + workerObject.get("costs").getAsString());
                      }
                    }
                  });
                }
                this.addon.priceOverviewActivity().priceData().put("Goldmine", new MineData(cost, workers));

              }
            }

            if(pricesObject.has("Kohlemine") && pricesObject.get("Kohlemine").isJsonObject()) {
              JsonObject coalMineObject = pricesObject.get("Kohlemine").getAsJsonObject();
              if(coalMineObject.has("cost") && coalMineObject.has("workers")) {

                String cost = coalMineObject.get("cost").getAsString();
                List<String> workers = new ArrayList<>();
                if(coalMineObject.get("workers").isJsonArray()) {
                  JsonArray array = coalMineObject.get("workers").getAsJsonArray();
                  array.forEach(jsonElement -> {
                    if(jsonElement.isJsonObject()) {
                      JsonObject workerObject = jsonElement.getAsJsonObject();
                      if(workerObject.has("name") && workerObject.has("costs")) {
                        workers.add(workerObject.get("name").getAsString() + ";" + workerObject.get("costs").getAsString());
                      }
                    }
                  });
                }
                this.addon.priceOverviewActivity().priceData().put("Kohlemine", new MineData(cost, workers));

              }
            }

            if(pricesObject.has("Eisenmine") && pricesObject.get("Eisenmine").isJsonObject()) {
              JsonObject ironMineObject = pricesObject.get("Eisenmine").getAsJsonObject();
              if(ironMineObject.has("cost") && ironMineObject.has("workers")) {

                String cost = ironMineObject.get("cost").getAsString();
                List<String> workers = new ArrayList<>();
                if(ironMineObject.get("workers").isJsonArray()) {
                  JsonArray array = ironMineObject.get("workers").getAsJsonArray();
                  array.forEach(jsonElement -> {
                    if(jsonElement.isJsonObject()) {
                      JsonObject workerObject = jsonElement.getAsJsonObject();
                      if(workerObject.has("name") && workerObject.has("costs")) {
                        workers.add(workerObject.get("name").getAsString() + ";" + workerObject.get("costs").getAsString());
                      }
                    }
                  });
                }
                this.addon.priceOverviewActivity().priceData().put("Eisenmine", new MineData(cost, workers));

              }
            }

            if(pricesObject.has("Lapismine") && pricesObject.get("Lapismine").isJsonObject()) {
              JsonObject lapisMineObject = pricesObject.get("Lapismine").getAsJsonObject();
              if(lapisMineObject.has("cost") && lapisMineObject.has("workers")) {

                String cost = lapisMineObject.get("cost").getAsString();
                List<String> workers = new ArrayList<>();
                if(lapisMineObject.get("workers").isJsonArray()) {
                  JsonArray array = lapisMineObject.get("workers").getAsJsonArray();
                  array.forEach(jsonElement -> {
                    if(jsonElement.isJsonObject()) {
                      JsonObject workerObject = jsonElement.getAsJsonObject();
                      if(workerObject.has("name") && workerObject.has("costs")) {
                        workers.add(workerObject.get("name").getAsString() + ";" + workerObject.get("costs").getAsString());
                      }
                    }
                  });
                }
                this.addon.priceOverviewActivity().priceData().put("Lapismine", new MineData(cost, workers));

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
        .userAgent("MoneyMaker LabyMod 4 Addon")
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
                  AddonSettings.workerCoordinates.get("x").add(workerObject.get("x").getAsDouble());
                }
                if(workerObject.has("z")) {
                  AddonSettings.workerCoordinates.get("z").add(workerObject.get("z").getAsDouble());
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
                  AddonSettings.debrisCoordinates.get("x").add(workerObject.get("x").getAsDouble());
                }
                if(workerObject.has("z")) {
                  AddonSettings.debrisCoordinates.get("z").add(workerObject.get("z").getAsDouble());
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
                  FarmingCave cave = this.addon.addonUtil().caveByName(levelObject.get("name").getAsString());
                  if(cave != FarmingCave.UNKNOWN) {
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
        .userAgent("MoneyMaker LabyMod 4 Addon")
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
                  && object.has("Rank") && object.has("MessageID") && object.has("formatted_timestamp")) {
                if(!object.get("MessageID").getAsString().isEmpty()) {
                  JsonObject chatMessage = new JsonObject();
                  chatMessage.addProperty("messageId", object.get("MessageID").getAsString());
                  chatMessage.addProperty("uuid", object.get("UUID").getAsString());
                  chatMessage.addProperty("userName", object.get("UserName").getAsString());
                  chatMessage.addProperty("message", object.get("Message").getAsString());
                  chatMessage.addProperty("rank", object.get("Rank").getAsString());
                  chatMessage.addProperty("fromCache", true);
                  chatMessage.addProperty("timeStamp", object.get("formatted_timestamp").getAsString());
                  chatMessage.addProperty("addonVersion", object.has("AddonVersion") ? object.get("AddonVersion").getAsString() : "N/A");
                  chatMessage.addProperty("minecraftVersion", object.has("MinecraftVersion") ? object.get("MinecraftVersion").getAsString() : "N/A");
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
        .userAgent("MoneyMaker LabyMod 4 Addon")
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
                this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.leaderboardActivity().entries().add(new LeaderboardEntryWidget(
                    this.addon,
                    UUID.fromString(object.get("UUID").getAsString()),
                    object.get("UserName").getAsString(),
                    object.get("Ranking").getAsInt(),
                    object.get("Blocks").getAsInt(),
                    object.get("Pickaxe_Ranking").getAsInt(),
                    object.get("Sword_Ranking").getAsInt()
                )));
              }
            }
          });
          if(update) {
            this.addon.labyAPI().minecraft().executeOnRenderThread(() -> this.addon.leaderboardActivity().reload());
          }
        });
  }

}
