package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import net.labymod.api.Laby;

public class ChatClient {

  private static final String SERVER_IP = "chat.moneymaker.fun";
  private static final int SERVER_PORT = 12345;

  public static boolean online = false;
  private static Socket socket;
  private static PrintWriter serverOut;

  public void connect() {
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      serverOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
      online = true;

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
                Laby.fireEvent(new MoneyPlayerStatusEvent(UUID.fromString(data.get("uuid").getAsString()), data.get("userName").getAsString(), data.get("server").getAsString()));
              }

              if(object.has("retrievedPlayerData")) {
                JsonObject data = object.get("retrievedPlayerData").getAsJsonObject();
                System.out.println(data);
              }

            }
          }

          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
          online = false;
          // Handle connection error
        }
      }).start();
    } catch (IOException e) {
      e.printStackTrace();
      online = false;
      // Handle connection error
    }
  }

  public static void sendChatMessage(MoneyChatMessage chatMessage) {
    if(serverOut == null) return;
    JsonObject object = new JsonObject();
    object.add("chatMessage", chatMessage.toJson());
    serverOut.println(object);
  }

  public static void sendMessage(String channel, JsonObject object) {
    if(serverOut == null) return;
    JsonObject data = new JsonObject();
    data.add(channel, object);
    serverOut.println(data);
  }

  // Add a method to close the socket when needed
  /*public static void closeSocket() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/
}
