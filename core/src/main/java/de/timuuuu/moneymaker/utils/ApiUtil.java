package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.io.web.request.Request;

public class ApiUtil {

  private String BASE_URL = "https://api.moneymaker.fun";

  private MoneyMakerAddon addon;

  public ApiUtil(MoneyMakerAddon addon) {
    this.addon = addon;
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
                  Laby.fireEvent(new MoneyChatReceiveEvent(MoneyChatMessage.fromJson(chatMessage)));
                }
              }
            }
          });
        });
  }

}
