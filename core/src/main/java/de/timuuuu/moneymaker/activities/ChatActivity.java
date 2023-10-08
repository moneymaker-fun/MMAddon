package de.timuuuu.moneymaker.activities;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatClient;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
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
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.OperatingSystem;
import net.labymod.api.util.concurrent.task.Task;

@AutoActivity
@Link("chat.lss")
public class ChatActivity extends Activity {

  private MoneyMakerAddon addon;

  private TextFieldWidget chatInput;
  private static List<ComponentWidget> chatMessages = new ArrayList<>();

  public ChatActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.chat.title");
    titleWidget.addId("chat-title");
    this.document.addChild(titleWidget);

    ComponentWidget statusWidget = ComponentWidget.i18n("moneymaker.ui.chat.server." + (ChatClient.online ? "online" : "offline"));
    statusWidget.addId("chat-status");
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
              data.addProperty("afk", false);
              data.addProperty("addonVersion", this.addon.addonInfo().getVersion());
              ChatClient.sendMessage("playerStatus", data);

              JsonObject object = new JsonObject();
              object.addProperty("uuid", this.addon.labyAPI().getUniqueId().toString());
              ChatClient.sendMessage("retrievePlayerData", object);
            }

          }).delay(5, TimeUnit.SECONDS).build().execute();
      });
      this.document.addChild(reconnectButton);
    }

    // Chat Container

    DivWidget chatContainer = new DivWidget();
    chatContainer.addId("chat-container");

    VerticalListWidget<ComponentWidget> chatList = new VerticalListWidget<>().addId("chat-messages");

    chatMessages.forEach(chatList::addChild);

    ScrollWidget chatScroll = new ScrollWidget(chatList, new ListSession<>());
    chatContainer.addChild(chatScroll);

    // Online Container

    ComponentWidget onlineTextWidget = ComponentWidget.i18n("moneymaker.ui.chat.online");
    onlineTextWidget.addId("chat-online-text");
    this.document.addChild(onlineTextWidget);

    DivWidget onlineContainer = new DivWidget();
    onlineContainer.addId("online-container");

    VerticalListWidget<ComponentWidget> onlineList = new VerticalListWidget<>().addId("online-list");

    AddonSettings.playerStatus.keySet().forEach(uuid -> {
      MoneyPlayer moneyPlayer = AddonSettings.playerStatus.get(uuid);
      if(moneyPlayer.server().contains("MoneyMaker")) {
        String color = moneyPlayer.staff() ? "§c" : "§e";

        Icon onlineStatus;
        if(!moneyPlayer.afk()) {
          onlineStatus = Icon.sprite16(ResourceLocation.create("moneymaker", "textures/ui/chat.png"), 0, 0);
        } else {
          onlineStatus = Icon.sprite16(ResourceLocation.create("moneymaker", "textures/ui/chat.png"), 1, 0);
        }

        Component component = Component.icon(Icon.head(uuid, true, false), 10)
            .append(Component.text(" " + color + moneyPlayer.userName() + " §8- §b" + moneyPlayer.server().replace("MoneyMaker", "") + " ")
                .append(Component.icon(onlineStatus))
            );
        ComponentWidget componentWidget = ComponentWidget.component(component);
        componentWidget.addId("online-entry");
        onlineList.addChild(componentWidget);
      }
    });

    ScrollWidget onlineScroll = new ScrollWidget(onlineList, new ListSession<>());
    onlineContainer.addChild(onlineScroll);

    // Input Container

    DivWidget inputContainer = new DivWidget();
    inputContainer.addId("input-container");

    if(ChatClient.online) {
      chatInput = new TextFieldWidget();
      chatInput.addId("chat-input");
      chatInput.submitButton().set(true);
      chatInput.maximalLength(250);
      chatInput.submitHandler(message -> this.submitMessage());

      inputContainer.addChild(chatInput);
    } else {
      ComponentWidget componentWidget = ComponentWidget.i18n("moneymaker.ui.chat.server-offline");
      componentWidget.addId("chat-error");
      inputContainer.addChild(componentWidget);
    }

    this.document.addChild(chatContainer);
    this.document.addChild(onlineContainer);
    this.document.addChild(inputContainer);

    //Feedback Button
    ButtonWidget feedbackButton = ButtonWidget.text("§6Feedback §7/ §cBugreport");
    feedbackButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://forms.gle/rWteNnvwqC5Q9Pz76");
    });
    feedbackButton.addId("feedback-button");
    this.document.addChild(feedbackButton);
  }

  private void submitMessage() {
    String message = this.chatInput.getText();
    message = message.trim();
    if (!message.isEmpty()) {
      this.chatInput.setEditable(false);
      this.chatInput.addId("blocked");
      this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);
      this.sendToServer(message);
      this.chatInput.setText("");
      this.addon.labyAPI().minecraft().executeNextTick(() -> this.chatInput.setFocused(true));
      Task.builder(() -> {
        this.chatInput.setEditable(true);
        this.chatInput.removeId("blocked");
        this.reload();
      }).delay(3, TimeUnit.SECONDS).build().execute();
    }
  }

  public void addChatMessage(MoneyChatMessage chatMessage) {
    if (chatMessages == null) return;
    if (chatMessage == null) return;
    String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    String color = chatMessage.staff() ? "§8[§cStaff§8] §c" : "§b";
    Component component = Component.text("§e" + time + "  ")
        .append(Component.icon(Icon.head(chatMessage.uuid(), true, false), 10))
        .append(Component.text(" " + color + chatMessage.userName() + "§8: §7" + chatMessage.message()));
    ComponentWidget messageWidget = ComponentWidget.component(component);
    messageWidget.addId("chat-message");
    chatMessages.add(messageWidget);
    reloadScreen();
  }

  public void reloadScreen() {
    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

  private void sendToServer(String message) {
    MoneyChatMessage chatMessage = new MoneyChatMessage(
        this.addon.labyAPI().getUniqueId(),
        this.addon.labyAPI().getName(),
        message,
        false);
    ChatClient.sendChatMessage(chatMessage);
  }
}
