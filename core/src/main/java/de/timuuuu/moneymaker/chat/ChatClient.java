package de.timuuuu.moneymaker.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.events.ChatServerMessageReceiveEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.concurrent.task.Task;

public class ChatClient {

  public static boolean muted = false;
  public static String muteReason = "";

  public static final String SERVER_IP = "chat.moneymaker.fun"; // Default: chat.moneymaker.fun | Backup: moneychat.mistercore.de
  private static final int SERVER_PORT = 12345;

  public static boolean online = false;
  private Socket socket;
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
        addon.chatActivity().reloadScreen();
        addon.pushNotification(Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("moneymaker.notification.chat.connected", TextColor.color(85, 255, 85)));
      }

      new Thread(() -> {
        try {
          BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

          if(!socket.isClosed() || socket != null) {
            String serverMessage;
            while ((serverMessage = serverIn.readLine()) != null) {
                JsonObject object = new Gson().fromJson(serverMessage, JsonObject.class);
                Laby.fireEvent(new ChatServerMessageReceiveEvent(object));
            }
            socket.close();
          }

        } catch (Exception e) {
          if(online) {
            addon.chatActivity().reloadScreen();
          }
          online = false;
          //e.printStackTrace();
        }
      }).start();
    } catch (IOException e) {
        online = false;
        if(reconnect) {
          addon.pushNotification(Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
              Component.translatable("moneymaker.notification.chat.no-connection", TextColor.color(255, 85, 85)));
          addon.chatActivity().reloadScreen();
        }
        //e.printStackTrace();
      // Handle connection error
    }
  }

  public static boolean isPortOpen(String host, int port) {
    try (Socket ignored = new Socket(host, port)) {
      return true;
    } catch (IOException ignored) {
      return false;
    }
  }

  public void heartBeat() {
    Task.builder(() -> {
      boolean status = isPortOpen(SERVER_IP, SERVER_PORT);
      if(!status && online) {
        addon.chatActivity().reloadScreen();
        addon.pushNotification(Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("moneymaker.notification.chat.timed", TextColor.color(255, 85, 85)));
      }
      if(status & !online) {
        connect(true);
      }
      online = status;
    }).repeat(1, TimeUnit.MINUTES).build().execute();
  }

  public boolean sendChatMessage(MoneyChatMessage chatMessage) {
    if(!addon.configuration().enabled().get()) return false;
    if(serverOut == null || socket.isClosed()) {
      online = false;
      addon.chatActivity().reloadScreen();
      return false;
    }
    JsonObject object = new JsonObject();
    object.add("chatMessage", chatMessage.toJson());
    serverOut.println(object);
    return true;
  }

  public boolean sendMessage(String channel, JsonObject object) {
    if(!addon.configuration().enabled().get()) return false;
    if(serverOut == null || socket.isClosed()) return false;
    JsonObject data = new JsonObject();
    data.add(channel, object);
    serverOut.println(data);
    return true;
  }

  public void sendLaunchData(String uuid, String userName) {
    if(serverOut == null || socket.isClosed()) return;
    JsonObject object = new JsonObject();
    object.addProperty("data", "add");
    object.addProperty("userName", userName);
    object.addProperty("uuid", uuid);
    object.addProperty("addonVersion", addon.addonInfo().getVersion());
    object.addProperty("gameVersion", addon.labyAPI().minecraft().getVersion());
    object.addProperty("development", addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment());
    JsonObject data = new JsonObject();
    data.add("addonStatistics", object);
    serverOut.println(data);
  }

  public void sendQuitData(String uuid) {
    if(serverOut == null || socket.isClosed()) return;
    JsonObject object = new JsonObject();
    object.addProperty("data", "remove");
    object.addProperty("uuid", uuid);
    JsonObject data = new JsonObject();
    data.add("addonStatistics", object);
    serverOut.println(data);
  }

  public void closeSocket() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException ignored) {}
  }

  public Socket socket() {
    return socket;
  }


  public enum ChatAction {
    CLEAR("CLEAR"),
    MUTE("MUTE"),
    UNMUTE("UNMUTE");

    private final String name;

    ChatAction(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static ChatAction actionByName(String name) {
    ChatAction action = null;
    for(ChatAction chatActions : ChatAction.values()) {
      if(chatActions.getName().equals(name)) {
        action = chatActions;
      }
    }
    return action;
  }

}
