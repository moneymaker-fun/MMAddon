package de.timuuuu.moneymaker.activities;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.popup.ChatRulesActivity;
import de.timuuuu.moneymaker.activities.widgets.ChatMessageWidget;
import de.timuuuu.moneymaker.activities.widgets.OnlineEntryWidget;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.chat.ChatClient.ChatAction;
import de.timuuuu.moneymaker.chat.ChatClientUtil.MessageType;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import de.timuuuu.moneymaker.utils.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.util.I18n;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.io.web.result.Result;

@AutoActivity
@Link("chat.lss")
public class ChatActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  private int MESSAGE_LIMIT = 50;

  private TextFieldWidget chatInput;
  private static List<ChatMessageWidget> chatMessages = new ArrayList<>();
  private ListSession<Widget> listSession = new ListSession<>();

  public ChatActivity(MoneyMakerAddon addon) {
    this.addon = addon;
    chatInput = new TextFieldWidget();
    chatInput.addId("chat-input");
    chatInput.maximalLength(200);
    chatInput.submitHandler(message -> this.submitMessage());
    listSession.scrollToBottom();
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.chat.title").addId("chat-title");
    this.document.addChild(titleWidget);

    ComponentWidget statusWidget;
    if(this.addon.chatClient().online()) {
      statusWidget = ComponentWidget.i18n("moneymaker.ui.chat.server.online", NamedTextColor.GREEN);
    } else {
      statusWidget = ComponentWidget.i18n("moneymaker.ui.chat.server.offline", NamedTextColor.RED);
    }

    statusWidget.addId("chat-status");
    this.document.addChild(statusWidget);

    ButtonWidget rulesButton = ButtonWidget.i18n("moneymaker.chat-rules.button").addId("rules-button");
    rulesButton.setPressable(() -> {
      try {
        ChatRulesActivity.create(this.addon, this.addon.labyAPI().minecraft().minecraftWindow().currentScreen(), false, chatRulesActivity -> {
          if(chatRulesActivity == null) return;
          this.addon.labyAPI().minecraft().minecraftWindow().displayScreen(chatRulesActivity);
        });
      } catch (Throwable ignored) {

      }
    });

    this.document.addChild(rulesButton);

    if(this.addon.configuration().chatReconnectButton().get()) {
      ButtonWidget reconnectButton = ButtonWidget.i18n("moneymaker.ui.chat.server.reconnect-button");
      reconnectButton.addId("chat-reconnect-button");
      reconnectButton.setPressable(() -> {
        this.addon.chatClient().closeConnection();
        reconnectButton.setEnabled(false);
          Task.builder(() -> {
            reconnectButton.setEnabled(true);
            this.addon.chatClient().connect(true);
            if(this.addon.chatClient().online()) {
              this.addon.chatClient().util().sendPlayerStatus(this.addon.labyAPI().getUniqueId().toString(), this.addon.labyAPI().getName(), false);

              this.addon.chatClient().util().sendRetrievePlayerData(this.addon.labyAPI().getUniqueId().toString());
            }

          }).delay(5, TimeUnit.SECONDS).build().execute();
      });
      this.document.addChild(reconnectButton);
    }

    // Online Container

    AtomicInteger onlineCount = new AtomicInteger(0);
    AddonUtil.playerStatus.values().forEach(moneyPlayer -> {
      String server = moneyPlayer.server();
      if(server.startsWith("Mine") || server.startsWith("Farming")) {
        onlineCount.getAndIncrement();
      }
    });


    ComponentWidget onlineTextWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.chat.online", NamedTextColor.AQUA, Component.text(onlineCount, NamedTextColor.YELLOW))).addId("chat-online-text");
    this.document.addChild(onlineTextWidget);

    DivWidget onlineContainer = new DivWidget();
    onlineContainer.addId("online-container");

    VerticalListWidget<OnlineEntryWidget> onlineList = new VerticalListWidget<>().addId("online-list");

    if(this.addon.chatClient().online()) {
      List<MoneyPlayer> players = new ArrayList<>(AddonUtil.playerStatus.values());
      players.sort(Comparator.comparing(moneyPlayer -> {
        if(moneyPlayer.rank() != null) {
          return moneyPlayer.rank().getId();
        }
        return MoneyRank.USER.getId();
      }));

      if(Util.isAdmin(this.addon.labyAPI().getUniqueId().toString()) && this.addon.configuration().chatShowAllPlayers().get()) {
        onlineList.addChild(new OnlineEntryWidget(this.addon, Component.text("→ ", NamedTextColor.DARK_GRAY).append(Component.text("Online auf MoneyMaker", NamedTextColor.GRAY))));
      }
      players.forEach(moneyPlayer -> {
        String server = moneyPlayer.server();
        if(server.startsWith("Mine") || server.startsWith("Farming")) {
          onlineList.addChild(new OnlineEntryWidget(this.addon, moneyPlayer, false));
        }
      });

      if(Util.isAdmin(this.addon.labyAPI().getUniqueId().toString()) && this.addon.configuration().chatShowAllPlayers().get()) {
        onlineList.addChild(new OnlineEntryWidget(this.addon, Component.text("→ ", NamedTextColor.DARK_GRAY).append(Component.text("Online andere Server", NamedTextColor.GRAY))));
        players.forEach(moneyPlayer -> {
          if(moneyPlayer.server().equalsIgnoreCase("Other")) {
            onlineList.addChild(new OnlineEntryWidget(this.addon, moneyPlayer, true));
          }
        });
      }

    }

    ScrollWidget onlineScroll = new ScrollWidget(onlineList, new ListSession<>()).addId("online-scroll");
    onlineContainer.addChild(onlineScroll);

    // Chat Container

    DivWidget chatContainer = new DivWidget().addId("chat-container");

    VerticalListWidget<ChatMessageWidget> chatList = new VerticalListWidget<>().addId("chat-messages");

    if(chatMessages.size() <= MESSAGE_LIMIT) {
      chatMessages.forEach(chatList::addChild);
    } else {
      int size = chatMessages.size();
      for(int i = size-MESSAGE_LIMIT; i != chatMessages.size(); i++) {
        chatList.addChild(chatMessages.get(i));
      }
    }

    ScrollWidget chatScroll = new ScrollWidget(chatList, this.listSession).addId("chat-scroll");
    chatContainer.addChild(chatScroll);

    // Input Container

    DivWidget inputContainer = new DivWidget().addId("input-container");

    if(this.addon.chatClient().online()) {
      if(this.addon.chatClient().muted() & !(Util.isStaff(this.labyAPI.getUniqueId()) || Util.isAdmin(this.labyAPI.getUniqueId().toString()))) {
        ComponentWidget componentWidget = ComponentWidget.i18n("moneymaker.ui.chat.muted.title").addId("chat-muted-title");
        ComponentWidget reasonWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.chat.muted.reason").append(Component.text(this.addon.chatClient().muteReason()))).addId("chat-muted-reason");
        inputContainer.addChild(componentWidget);
        inputContainer.addChild(reasonWidget);
      } else {
        inputContainer.addChild(chatInput);
      }
    } else {
      ComponentWidget componentWidget = ComponentWidget.i18n("moneymaker.ui.chat.server-offline").addId("chat-error");
      inputContainer.addChild(componentWidget);
    }

    this.document.addChild(chatContainer);
    this.document.addChild(onlineContainer);
    this.document.addChild(inputContainer);

    if(!chatMessages.isEmpty()) {
      chatScroll.scrollToBottom();
    }

  }

  @Override
  public void onOpenScreen() {
    super.onOpenScreen();

    try {
      ChatRulesActivity.create(this.addon,
          this.addon.labyAPI().minecraft().minecraftWindow().currentScreen(), true,
          chatRulesActivity -> {
            if (chatRulesActivity == null) return;
            if(chatRulesActivity.rules().has("version") &&
                this.addon.configuration().chatRulesVersion().get() == chatRulesActivity.rules().get("version").getAsInt()) return;
            this.addon.labyAPI().minecraft().minecraftWindow().displayScreen(chatRulesActivity);
          });
    } catch (Throwable ignored) {

    }

  }

  private void submitMessage() {
    String message = this.chatInput.getText();
    message = message.trim();
    if (!message.isEmpty()) {
      if(message.startsWith("/") & (Util.isStaff(this.labyAPI.getUniqueId()) || Util.isAdmin(this.labyAPI.getUniqueId().toString()))) {
        this.handleCommands(message);
        this.chatInput.setText("");
        return;
      }
      if(this.sendToServer(message)) {
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);
        this.chatInput.setText("");
        if(!Util.isAdmin(this.labyAPI.getUniqueId().toString())) {
          this.chatInput.setEditable(false);
          this.chatInput.addId("blocked");
          Task.builder(() -> {
            this.chatInput.setEditable(true);
            this.chatInput.removeId("blocked");
            this.reloadScreen();
            this.addon.labyAPI().minecraft().executeNextTick(() -> this.chatInput.setFocused(true));
          }).delay(3, TimeUnit.SECONDS).build().execute();
        } else {
          this.addon.labyAPI().minecraft().executeNextTick(() -> this.chatInput.setFocused(true));
        }
      }
    }
  }

  private void handleCommands(String input) {
    if(!(Util.isStaff(this.labyAPI.getUniqueId()) || Util.isAdmin(this.labyAPI.getUniqueId().toString()))) return;

    boolean successful = false;

    if(input.equalsIgnoreCase("/clear")) {
      successful = this.addon.chatClient().sendChatAction(this.labyAPI.getUniqueId(), this.labyAPI.getName(), ChatAction.CLEAR, null);
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
          this.addCustomChatMessage(Component.text("Failed to get uuid from " + playerName + ":", NamedTextColor.DARK_RED));
          this.addCustomChatMessage(Component.text(requestUuid.exception().getMessage(), NamedTextColor.RED));
          return;
        }
        UUID uuid = requestUuid.get();

        if(Util.isAdmin(uuid.toString()) || Util.isStaff(uuid)) {
          this.addCustomChatMessage(Component.text("Du kannst keine Teammitglieder muten.", NamedTextColor.RED));
          return;
        }

        JsonObject object = new JsonObject();
        object.addProperty("uuid", uuid.toString());
        object.addProperty("playerName", playerName);
        object.addProperty("reason", reason);

        successful = this.addon.chatClient().sendChatAction(this.labyAPI.getUniqueId(), this.labyAPI.getName(), ChatAction.MUTE, object);
        if(successful) {
          this.addCustomChatMessage(
              Component.text("Du hast ", NamedTextColor.GRAY)
                  .append(Component.text(playerName, NamedTextColor.YELLOW))
                  .append(Component.text(" erfolgreich gemutet.", NamedTextColor.GRAY))
          );
          this.addCustomChatMessage(
              Component.text("Grund: ", NamedTextColor.GRAY)
                  .append(Component.text(reason, NamedTextColor.YELLOW))
          );
        }
      } else {
        this.addCustomChatMessage(Component.text("Bitte nutze /mute <Spieler> <Grund>", NamedTextColor.RED));
        return;
      }
    }

    if(input.startsWith("/unmute")) {
      String[] args = input.split(" ");
      if(args.length == 2) {
        String playerName = args[1];

        Result<UUID> requestUuid = this.labyAPI.labyNetController().loadUniqueIdByNameSync(playerName);
        if(requestUuid.hasException()) {
          this.addCustomChatMessage(Component.text("Failed to get uuid from " + playerName + ":", NamedTextColor.DARK_RED));
          this.addCustomChatMessage(Component.text(requestUuid.exception().getMessage(), NamedTextColor.RED));
          return;
        }

        JsonObject object = new JsonObject();
        object.addProperty("uuid", requestUuid.get().toString());
        object.addProperty("playerName", playerName);

        successful = this.addon.chatClient().sendChatAction(this.labyAPI.getUniqueId(), this.labyAPI.getName(), ChatAction.UNMUTE, object);
        if(successful) {
          this.addCustomChatMessage(
              Component.text("Du hast ", NamedTextColor.GRAY)
                  .append(Component.text(playerName, NamedTextColor.YELLOW))
                  .append(Component.text(" erfolgreich entmutet.", NamedTextColor.GRAY))
          );
        }
      } else {
        this.addCustomChatMessage(Component.text("Bitte nutze /unmute <Spieler>", NamedTextColor.RED));
        return;
      }
    }

    if(!successful) {
      this.addCustomChatMessage(
          Component.text("Befehl konnte nicht ausgeführt werden. ", NamedTextColor.RED)
              .append(Component.text("(Nur für dich sichtbar)", NamedTextColor.GRAY))
      );
    }
  }

  public void addChatMessage(MoneyChatMessage chatMessage) {
    if (chatMessage == null) return;
    String CURRENT_TIME = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {

      if(chatMessages.size() <= MESSAGE_LIMIT) {
        chatMessages.add(new ChatMessageWidget(this.addon, chatMessage.fromServerCache() ? chatMessage.timeStamp() : CURRENT_TIME, chatMessage).addId("chat-message"));
      } else {
        chatMessages.removeFirst();
        chatMessages.add(new ChatMessageWidget(this.addon, chatMessage.fromServerCache() ? chatMessage.timeStamp() : CURRENT_TIME, chatMessage).addId("chat-message"));
      }

      this.reloadScreen();
    });
  }

  public void clearChat(boolean message) {
    chatMessages.clear();
    if(message) {
      String CURRENT_TIME = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
      this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {
        chatMessages.add(new ChatMessageWidget(this.addon, CURRENT_TIME, Component.translatable("moneymaker.ui.chat.chatCleared", NamedTextColor.DARK_RED)).addId("chat-message"));
      });
    }
    this.reloadScreen();
  }

  public void deleteMessage(String id) {
    List<ChatMessageWidget> remove = new ArrayList<>();
    for(ChatMessageWidget messageWidget : chatMessages) {
      if(messageWidget.chatMessage() != null) {
        if(messageWidget.chatMessage().messageId().equals(id)) {
          if(messageWidget.messageType() != MessageType.PLAYER) {
            remove.add(messageWidget);
          } else {
            messageWidget.chatMessage().message("§7§o" + I18n.translate("moneymaker.ui.chat.messageDeleted"));
            messageWidget.chatMessage().deleted(true);
          }
        }
      }
    }
    if(!remove.isEmpty()) {
      chatMessages.removeAll(remove);
    }
    this.reloadScreen();
  }

  public void addCustomChatMessage(Component chatMessage) {
    if (chatMessage == null) return;
    String CURRENT_TIME = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    this.addon.labyAPI().minecraft().executeOnRenderThread(() -> {

      if(chatMessages.size() <= MESSAGE_LIMIT) {
        chatMessages.add(new ChatMessageWidget(this.addon, CURRENT_TIME, chatMessage));
      } else {
        chatMessages.removeFirst();
        chatMessages.add(new ChatMessageWidget(this.addon, CURRENT_TIME, chatMessage));
      }

      this.reloadScreen();
    });
  }

  public void reloadScreen() {
    if(!this.isOpen()) return;
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

  private boolean sendToServer(String message) {
    MoneyChatMessage chatMessage = new MoneyChatMessage(
        "UNKNOWN",
        this.addon.labyAPI().getUniqueId(),
        this.addon.labyAPI().getName(),
        message,
        MoneyRank.USER,
        false,
        "");
    return this.addon.chatClient().sendChatMessage(chatMessage);
  }

}
