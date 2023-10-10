package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.concurrent.task.Task;

public class ChatClient {

  public static final String SERVER_IP = "chat.moneymaker.fun"; // Default: chat.moneymaker.fun | Backup: moneychat.mistercore.de
  private static final int SERVER_PORT = 12345;

  public static boolean online = false;
  private static Socket socket;
  private static PrintWriter serverOut;

  private static MoneyMakerAddon addon;

  public ChatClient(MoneyMakerAddon addon) {
    ChatClient.addon = addon;
  }

  public void connect(boolean reconnect) {
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      serverOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
      online = true;
      if(reconnect) {
        addon.chatActivity.reloadScreen();
        addon.pushNotification(Component.text("Chat-Server"), Component.text("§aErfolgreich zum Server verbunden."));
      }

      new Thread(() -> {
        try {
          BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

          String serverMessage;
          while ((serverMessage = serverIn.readLine()) != null) {

            JsonElement element = JsonParser.parseString(serverMessage);
            if (element.isJsonObject()) {
              JsonObject object = element.getAsJsonObject();

              if(object.has("chatMessage") && object.get("chatMessage").isJsonObject()) {
                MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(object.get("chatMessage").getAsJsonObject());
                Laby.fireEvent(new MoneyChatReceiveEvent(chatMessage));
              }

              if(object.has("playerStatus") && object.get("playerStatus").isJsonObject()) {
                JsonObject data = object.get("playerStatus").getAsJsonObject();
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                Laby.fireEvent(new MoneyPlayerStatusEvent(
                    uuid,
                    new MoneyPlayer(uuid, data.get("userName").getAsString(), data.get("server").getAsString(), data.get("addonVersion").getAsString(), data.get("staffMember").getAsBoolean())
                ));
              }

              if(object.has("retrievedPlayerData")) {
                JsonObject data = object.get("retrievedPlayerData").getAsJsonObject();
                if(data.has("uuid") & data.has("players")) {
                  if(Laby.labyAPI().getUniqueId().toString().equals(data.get("uuid").getAsString())) {
                    if(data.get("players").isJsonArray()) {
                      JsonArray array = data.get("players").getAsJsonArray();
                      for(int i  = 0; i < array.size(); i++) {
                        JsonObject playerData = array.get(i).getAsJsonObject();
                        UUID uuid = UUID.fromString(playerData.get("uuid").getAsString());
                        AddonSettings.playerStatus.put(uuid, new MoneyPlayer(
                            uuid,
                            playerData.get("userName").getAsString(),
                            playerData.get("server").getAsString(),
                            playerData.get("addonVersion").getAsString(),
                            playerData.get("staffMember").getAsBoolean()
                        ));
                      }
                    }
                  }
                }
              }

            }
          }

          socket.close();
        } catch (IOException e) {
          if(online) {
            addon.chatActivity.reloadScreen();
          }
          online = false;
          // Handle connection error
        }
      }).start();
    } catch (IOException e) {
        online = false;
        if(reconnect) {
          addon.pushNotification(Component.text("Chat-Server"), Component.text("§cKeine Verbindung zum Chat-Server möglich."));
          addon.chatActivity.reloadScreen();
        }
      // Handle connection error
    }
  }

  public static boolean isPortOpen(String host, int port) {
    try (Socket socket = new Socket(host, port)) {
      return true;
    } catch (IOException ignored) {
      return false;
    }
  }

  public void heartBeat() {
    Task.builder(() -> {
      boolean status = isPortOpen(SERVER_IP, SERVER_PORT);
      if(!status && online) {
        addon.chatActivity.reloadScreen();
        addon.pushNotification(Component.text("Chat-Server"), Component.text("§cVerbindung zum Chat-Server verloren."));
      }
      if(status & !online) {
        connect(true);
      }
      online = status;
    }).repeat(1, TimeUnit.MINUTES).build().execute();
  }

  /*private boolean sendHeartBeat() {
    if(serverOut == null) return false;
    JsonObject object = new JsonObject();
    object.addProperty("uuid", Laby.labyAPI().getUniqueId().toString());
    JsonObject data = new JsonObject();
    data.add("heartBeat", object);
    serverOut.println(data);
    return true;
  }*/

  public static boolean sendChatMessage(MoneyChatMessage chatMessage) {
    if(serverOut == null || socket.isClosed()) {
      online = false;
      addon.chatActivity.reloadScreen();
      return false;
    }
    JsonObject object = new JsonObject();
    object.add("chatMessage", chatMessage.toJson());
    serverOut.println(object);
    return true;
  }

  public static void sendMessage(String channel, JsonObject object) {
    if(serverOut == null || socket.isClosed()) return;
    JsonObject data = new JsonObject();
    data.add(channel, object);
    serverOut.println(data);
  }

  public void closeSocket() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException ignored) {}
  }
}
