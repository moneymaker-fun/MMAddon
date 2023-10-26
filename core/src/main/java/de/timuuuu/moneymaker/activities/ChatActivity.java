package de.timuuuu.moneymaker.activities;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.ChatMessageWidget;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatClient;
import de.timuuuu.moneymaker.utils.ChatClient.ChatAction;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import de.timuuuu.moneymaker.utils.MoneyPlayer.Rank;
import de.timuuuu.moneymaker.utils.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.io.web.result.Result;

@AutoActivity
@Link("chat.lss")
public class ChatActivity extends Activity {

  private MoneyMakerAddon addon;

  private TextFieldWidget chatInput;
  private static List<ChatMessageWidget> chatMessages = new ArrayList<>();

  public ChatActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.chat.title").addId("chat-title");
    this.document.addChild(titleWidget);

    ComponentWidget statusWidget = ComponentWidget.i18n("moneymaker.ui.chat.server." + (ChatClient.online ? "online" : "offline")).addId("chat-status");
    this.document.addChild(statusWidget);

    if(/*!ChatClient.online && */this.addon.configuration().chatReconnectButton().get()) {
      ButtonWidget reconnectButton = ButtonWidget.i18n("moneymaker.ui.chat.server.reconnect-button");
      reconnectButton.addId("chat-reconnect-button");
      reconnectButton.setPressable(() -> {
        this.addon.chatClient.closeSocket();
        reconnectButton.setEnabled(false);
          Task.builder(() -> {
            reconnectButton.setEnabled(true);
            this.addon.chatClient.connect(true);
            if(ChatClient.online) {

              JsonObject data = new JsonObject();
              data.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
              data.addProperty("userName", this.addon.labyAPI().getName());
              data.addProperty("server", AddonSettings.playingOn.contains("MoneyMaker") ? AddonSettings.playingOn : "Other");
              data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
              this.addon.chatClient.sendMessage("playerStatus", data);

              JsonObject object = new JsonObject();
              object.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
              this.addon.chatClient.sendMessage("retrievePlayerData", object);
            }

          }).delay(5, TimeUnit.SECONDS).build().execute();
      });
      this.document.addChild(reconnectButton);
    }

    // Chat Container

    DivWidget chatContainer = new DivWidget().addId("chat-container");

    VerticalListWidget<ChatMessageWidget> chatList = new VerticalListWidget<>().addId("chat-messages");

    chatMessages.forEach(chatList::addChild);

    ScrollWidget chatScroll = new ScrollWidget(chatList, new ListSession<>()).addId("chat-scroll");
    Task.builder(chatScroll::scrollToBottom).delay(50, TimeUnit.MILLISECONDS).build().execute();
    chatContainer.addChild(chatScroll);

    // Online Container

    ComponentWidget onlineTextWidget = ComponentWidget.i18n("moneymaker.ui.chat.online").addId("chat-online-text");
    this.document.addChild(onlineTextWidget);

    DivWidget onlineContainer = new DivWidget();
    onlineContainer.addId("online-container");

    VerticalListWidget<ComponentWidget> onlineList = new VerticalListWidget<>().addId("online-list");

    List<MoneyPlayer> players = new ArrayList<>(AddonSettings.playerStatus.values());
    players.sort(Comparator.comparing(o -> o.rank().getId()));

    AddonSettings.playerStatus.keySet().forEach(uuid -> {
      MoneyPlayer moneyPlayer = AddonSettings.playerStatus.get(uuid);
      if(moneyPlayer.server().contains("MoneyMaker")) {
        Component component = Component.icon(Icon.head(uuid, true, false), 10)
            .append(Component.text(" " + moneyPlayer.rank().getOnlineColor() + moneyPlayer.userName() + " §8- §b" + moneyPlayer.server().replace("MoneyMaker", "") + " "));
        ComponentWidget componentWidget = ComponentWidget.component(component);
        componentWidget.addId("online-entry");
        onlineList.addChild(componentWidget);
      }
    });

    ScrollWidget onlineScroll = new ScrollWidget(onlineList, new ListSession<>()).addId("online-scroll");
    onlineContainer.addChild(onlineScroll);

    // Input Container

    DivWidget inputContainer = new DivWidget().addId("input-container");

    if(ChatClient.online) {
      if(ChatClient.muted & !(Util.isStaff(this.labyAPI.getUniqueId()) || Util.isDev(this.labyAPI.getUniqueId().toString()))) {
        ComponentWidget componentWidget = ComponentWidget.i18n("moneymaker.ui.chat.muted.title").addId("chat-muted-title");
        ComponentWidget reasonWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.chat.muted.reason").append(Component.text(ChatClient.muteReason))).addId("chat-muted-reason");
        inputContainer.addChild(componentWidget);
        inputContainer.addChild(reasonWidget);
      } else {
        chatInput = new TextFieldWidget();
        chatInput.addId("chat-input");
        chatInput.submitButton().set(true);
        chatInput.maximalLength(200);
        chatInput.submitHandler(message -> this.submitMessage());

        inputContainer.addChild(chatInput);
      }
    } else {
      ComponentWidget componentWidget = ComponentWidget.i18n("moneymaker.ui.chat.server-offline").addId("chat-error");
      inputContainer.addChild(componentWidget);
    }

    this.document.addChild(chatContainer);
    this.document.addChild(onlineContainer);
    this.document.addChild(inputContainer);
  }

  private void submitMessage() {
    String message = this.chatInput.getText();
    message = message.trim();
    if (!message.isEmpty()) {
      if(message.startsWith("/") & (Util.isStaff(this.labyAPI.getUniqueId()) || Util.isDev(this.labyAPI.getUniqueId().toString()))) {
        this.handleCommands(message);
        this.chatInput.setText("");
        return;
      }
      if(this.sendToServer(message)) {
        this.chatInput.setEditable(false);
        this.chatInput.addId("blocked");
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);
        this.chatInput.setText("");
        this.addon.labyAPI().minecraft().executeNextTick(() -> this.chatInput.setFocused(true));
        Task.builder(() -> {
          this.chatInput.setEditable(true);
          this.chatInput.removeId("blocked");
          this.reload();
        }).delay(3, TimeUnit.SECONDS).build().execute();
      }
    }
  }

  private void handleCommands(String input) {
    if(!(Util.isStaff(this.labyAPI.getUniqueId()) || Util.isDev(this.labyAPI.getUniqueId().toString()))) return;

    boolean successful = false;

    if(input.equalsIgnoreCase("/clear")) {
      successful = this.sendChatAction(this.labyAPI.getUniqueId(), ChatAction.CLEAR, null);
    }

    // /mute <Spieler> <Grund>
    if(input.startsWith("/mute")) {
      String[] args = input.split(" ");
      if(args.length >= 3) {
        String playerName = args[1];
        StringBuilder builder = new StringBuilder();
        for(int i = 2; i != args.length; i++) {
          builder.append(args[i]).append(" ");
        }
        String reason = builder.toString().trim();

        Result<UUID> requestUuid = this.labyAPI.labyNetController().loadUniqueIdByNameSync(playerName);
        if(requestUuid.hasException()) {
          this.addCustomChatMessage("§4Failed to get uuid from " + playerName + ":");
          this.addCustomChatMessage(requestUuid.exception().getMessage());
          return;
        }
        UUID uuid = requestUuid.get();

        if(Util.isDev(uuid.toString()) || Util.isStaff(uuid)) {
          this.addCustomChatMessage("§cDu kannst keine Teammitglieder muten.");
          return;
        }

        JsonObject object = new JsonObject();
        object.addProperty("uuid", uuid.toString());
        object.addProperty("playerName", playerName);
        object.addProperty("reason", reason);

        successful = this.sendChatAction(this.labyAPI.getUniqueId(), ChatAction.MUTE, object);
        if(successful) {
          this.addCustomChatMessage("§7Du hast §e" + playerName + " §7erfolgreich gemutet.");
          this.addCustomChatMessage("§7Grund: §e" + reason);
        }
      } else {
        this.addCustomChatMessage("§cBitte nutze /mute <Spieler> <Grund>");
        return;
      }
    }

    if(input.startsWith("/unmute")) {
      String[] args = input.split(" ");
      if(args.length == 2) {
        String playerName = args[1];

        Result<UUID> requestUuid = this.labyAPI.labyNetController().loadUniqueIdByNameSync(playerName);
        if(requestUuid.hasException()) {
          this.addCustomChatMessage("§4Failed to get uuid from " + playerName + ":");
          this.addCustomChatMessage(requestUuid.exception().getMessage());
          return;
        }

        JsonObject object = new JsonObject();
        object.addProperty("uuid", requestUuid.get().toString());
        object.addProperty("playerName", playerName);

        successful = this.sendChatAction(this.labyAPI.getUniqueId(), ChatAction.UNMUTE, object);
        if(successful) {
          this.addCustomChatMessage("§7Du hast §e" + playerName + " §7erfolgreich entmutet.");
        }
      } else {
        this.addCustomChatMessage("§cBitte nutze /unmute <Spieler>");
        return;
      }
    }

    if(!successful) {
      this.addCustomChatMessage("§cBefehl konnte nicht ausgeführt werden. §7(Nur für dich sichtbar)");
    }
  }

  public void addChatMessage(MoneyChatMessage chatMessage) {
    if (chatMessage == null) return;
    String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {
      chatMessages.add(new ChatMessageWidget(time, chatMessage).addId("chat-message"));
      this.reload();
    });
  }

  public void clearChat(boolean message) {
    chatMessages.clear();
    if(message) {
      String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
      this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {
        chatMessages.add(new ChatMessageWidget(time, "§4Der Chat wurde geleert.").addId("chat-message"));
        this.reload();
      });
    }
  }

  public void addCustomChatMessage(String chatMessage) {
    if (chatMessage == null) return;
    String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {
      chatMessages.add(new ChatMessageWidget(time, chatMessage));
      this.reload();
    });
  }

  public void reloadScreen() {
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

  private boolean sendToServer(String message) {
    MoneyChatMessage chatMessage = new MoneyChatMessage(
        this.addon.labyAPI().getUniqueId(),
        this.addon.labyAPI().getName(),
        message,
        Rank.USER);
    return this.addon.chatClient.sendChatMessage(chatMessage);
  }

  private boolean sendChatAction(UUID executor, ChatAction action, JsonObject data) {
    JsonObject object = new JsonObject();
    object.addProperty("action", action.getName());
    object.addProperty("executor", executor.toString());
    if(data != null) {
      object.add("data", data);
    }
    return this.addon.chatClient.sendMessage("chatAction", object);
  }

}
