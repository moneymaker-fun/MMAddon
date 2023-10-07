package de.timuuuu.moneymaker.utils;

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

  public ChatClient(MoneyMakerAddon addon) {
    ChatClient.addon = addon;
  }

  public void connect() {
    new Thread(() -> {
      try {
        Socket socket = new Socket("78.31.64.201", 12345); // Replace with your server IP and port
        BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String serverMessage;
        while ((serverMessage = serverIn.readLine()) != null) {
          // Handle the received message (e.g., display it in your chat interface)
          MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(new JsonParser().parse(serverMessage).getAsJsonObject()); // Implement this method
          new ChatActivity().addChatMessage(chatMessage);
        }

        socket.close(); // Close the socket when done
      } catch (IOException e) {
        e.printStackTrace();
        // Handle connection error
      }
    }).start();
  }

  public static void sendMessage(MoneyChatMessage chatMessage) {
    try {
      Socket socket = new Socket(SERVER_IP, SERVER_PORT); // Replace with your server IP and port
      PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
      serverOut.println(chatMessage.toJson());
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
      // Handle connection error
    }
  }

  /*public static void sendMessage(MoneyChatMessage chatMessage) {
    if(socket == null) return;
    if(socket.isClosed()) {
      addon.pushNotification(Component.text("Chat-Client"), Component.text("Socket is closed."));
      return;
    }
    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      out.println(chatMessage.toJson());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/

}
