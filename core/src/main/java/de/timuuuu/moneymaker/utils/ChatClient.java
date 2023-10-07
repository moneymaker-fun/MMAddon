package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.client.component.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

  private static final String SERVER_IP = "78.31.64.201";
  private static final int SERVER_PORT = 12345;

  private static Socket socket;

  public static void connect(MoneyMakerAddon addon) {
    try (
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    ) {
      ChatClient.socket = socket;
      System.out.println("Connected to the chat server.");
      Thread receivingThread = new Thread(() -> {
        try {
          String message;
          while ((message = in.readLine()) != null) {
            addon.logger().info("Server: " + message);
            addon.pushNotification(Component.text("Server Response"), Component.text(message));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      receivingThread.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendMessage(MoneyChatMessage chatMessage) {
    if(socket == null) return;
    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      out.println(chatMessage.toJson());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
