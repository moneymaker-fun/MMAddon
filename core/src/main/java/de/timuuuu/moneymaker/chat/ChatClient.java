package de.timuuuu.moneymaker.chat;

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
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import de.timuuuu.moneymaker.utils.AddonUtil.FarmingCave;
import de.timuuuu.moneymaker.utils.AddonUtil.MineType;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.concurrent.task.Task;

public class ChatClient {

  private boolean muted = false;
  private String muteReason = "";

  private final String SERVER_IP = "chat.moneymakeraddon.de";
  private final int SERVER_PORT = 12345;

  private boolean online = false;
  private Socket socket;
  private PrintWriter serverOut;

  private static MoneyMakerAddon addon;
  private ChatClientUtil util;

  public ChatClient(MoneyMakerAddon addon) {
    ChatClient.addon = addon;
    this.util = new ChatClientUtil(addon, this);
  }

  public void connect(boolean reconnect) {
    if(!addon.labyAPI().minecraft().sessionAccessor().isPremium() && !addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      addon.logger().info("[MoneyMaker - Chat] Not connecting to Chat-Server. Account is cracked account!");
      return;
    }
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
          if(socket != null && !socket.isClosed()) {
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            String serverMessage;
            while ((serverMessage = serverIn.readLine()) != null) {
                JsonObject object = addon.gson().fromJson(serverMessage, JsonObject.class);
                Laby.fireEvent(new ChatServerMessageReceiveEvent(object));
            }
            socket.close();
          }

        } catch (Exception e) {
          if(online) {
            addon.chatActivity().reloadScreen();
          }
          online = false;
          e.printStackTrace();
        }
      }).start();
    } catch (IOException e) {
        online = false;
        if(reconnect) {
          addon.pushNotification(Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
              Component.translatable("moneymaker.notification.chat.no-connection", TextColor.color(255, 85, 85)));
          addon.chatActivity().reloadScreen();
        }
        e.printStackTrace();
      // Handle connection error
    }
  }

  public void connectStartUp() {
    this.connect(false);
    this.checkStatus();
    this.util.sendStatistics(false, addon.labyAPI().getUniqueId().toString(), addon.labyAPI().getName());
    Task.builder(() -> {
      if(!isConnected()) {
        this.connect(false);
      }
      this.sendHeartbeat();
      this.util.sendRetrievePlayerData(addon.labyAPI().getUniqueId().toString());
    }).delay(5, TimeUnit.SECONDS).build().execute();
  }

  public static boolean isPortOpen(String host, int port) {
    try (Socket ignored = new Socket(host, port)) {
      return true;
    } catch (IOException ignored) {
      return false;
    }
  }

  public void sendHeartbeat() {
    Task.builder(() -> {
      JsonObject object = new JsonObject();
      object.addProperty("uuid", addon.labyAPI().getUniqueId().toString());
      object.addProperty("userName", addon.labyAPI().getName());
      object.addProperty("server", this.currentServer());
      this.sendMessage("heartbeat", object);
    }).repeat(5, TimeUnit.MINUTES).build().execute();
  }

  public void checkStatus() {
    Task.builder(() -> {
      boolean status = isPortOpen(SERVER_IP, SERVER_PORT);
      if(!status && online) {
        addon.chatActivity().reloadScreen();
        addon.pushNotification(Component.translatable("moneymaker.notification.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("moneymaker.notification.chat.timed", TextColor.color(255, 85, 85)));
        if(this.isConnected()) {
            try {
                socket.close();
                serverOut = null;
            } catch(IOException ignored) {}
        }
      }
      if(status & !online) {
        connect(true);
      }
      online = status;
    }).repeat(1, TimeUnit.MINUTES).build().execute();
  }

  public boolean isConnected() {
    return this.socket != null && !this.socket.isClosed();
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
    if(serverOut == null || socket.isClosed()) return false;
    JsonObject data = new JsonObject();
    data.add(channel, object);
    serverOut.println(data);
    return true;
  }

  public boolean sendChatAction(UUID executorUUID, String executorName, ChatAction action, JsonObject data) {
    JsonObject object = new JsonObject();
    object.addProperty("action", action.getName());
    object.addProperty("executorUUID", executorUUID.toString());
    object.addProperty("executorName", executorName);
    if(data != null) {
      object.add("data", data);
    }
    return this.sendMessage("chatAction", object);
  }

  public void closeConnection() {
    if(this.serverOut != null) {
      this.serverOut.close();
      this.serverOut = null;
    }

    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
        socket = null;
      }
    } catch (IOException ignored) {}

  }

  public ChatClientUtil util() {
    return util;
  }

  public boolean connected() {
    return serverOut == null || socket.isClosed();
  }

  public boolean muted() {
    return muted;
  }

  public void muted(boolean muted) {
    this.muted = muted;
  }

  public String muteReason() {
    return muteReason;
  }

  public void muteReason(String muteReason) {
    this.muteReason = muteReason;
  }

  public boolean online() {
    return online;
  }

  public String currentServer() {
    if(addon.addonUtil().inMine()) return currentMine(addon.addonUtil().currentMine());
    if(addon.addonUtil().inFarming()) return currentCave(addon.addonUtil().farmingCave());
    return "Other";
  }

  public String currentMine(MineType mine) {
    if(mine == null) return "Mine";
    if(!addon.configuration().chatConfiguration.showDetailedLocation().get()) {
      return "Mine";
    }
    return "Mine - " + mine.internalName();
  }

  public String currentCave(FarmingCave cave) {
    if(!addon.configuration().chatConfiguration.showDetailedLocation().get()) {
      return "Farming";
    }
    return "Farming - " + cave.internalName();
  }

  public enum ChatAction {
    CLEAR("CLEAR"),
    MUTE("MUTE"),
    UNMUTE("UNMUTE"),
    REPORT("REPORT"),
    DELETE_MESSAGE("DELETE_MESSAGE");

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
