package de.timuuuu.moneymaker.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.ChatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

  private static final String SERVER_IP = "78.31.64.201";
  private static final int SERVER_PORT = 12345;

  private static MoneyMakerAddon addon;
  private static Socket socket;
  private static PrintWriter serverOut;

  public ChatClient(MoneyMakerAddon addon) {
    ChatClient.addon = addon;
  }

  public void connect() {
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      serverOut = new PrintWriter(socket.getOutputStream(), true);

      new Thread(() -> {
        try {
          BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

          String serverMessage;
          while ((serverMessage = serverIn.readLine()) != null) {
            addon.logger().info(serverMessage);

            JsonElement element = JsonParser.parseString(serverMessage);
            if (element.isJsonObject()) {
              MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(element.getAsJsonObject());
              new ChatActivity().addChatMessage(chatMessage); // Use the ChatActivity instance to add the message
            }
          }

          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
          // Handle connection error
        }
      }).start();
    } catch (IOException e) {
      e.printStackTrace();
      // Handle connection error
    }
  }

  public static void sendMessage(MoneyChatMessage chatMessage) {
    if (serverOut != null) {
      serverOut.println(chatMessage.toJson());
    } else {
      // Handle the case when the socket is not initialized or the connection is lost
    }
  }

  // Add a method to close the socket when needed
  public static void closeSocket() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
