package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.io.web.request.Request;

public class ApiUtil {

  private String BASE_URL = "https://api.moneymaker.fun";

  private MoneyMakerAddon addon;

  public ApiUtil(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  public void loadCoordinates() {
    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "/locations/")
        .async()
        .connectTimeout(5000)
        .readTimeout(5000)
        .addHeader("User-Agent", "MoneyMaker LabyMod 4 Addon")
        .execute(response -> {
          if(response.getStatusCode() != 200 || response.hasException()) {
            AddonSettings.setFallbackCoordinates(true);
            this.addon.logger().debug("Loading Coordinates from internal. API got no response.");
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
            this.addon.logger().debug("Loaded Worker Coordinates from API.");
          }

          if(object.has("debris") && object.get("debris").isJsonArray()) {
            JsonArray array = object.get("debris").getAsJsonArray();
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
            this.addon.logger().debug("Loaded Debris Coordinates from API.");
          }
        });
  }

  public void loadChatHistory() {
    if(!this.addon.configuration().moneyChatConfiguration.loadChatHistory().get()) return;
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

}
