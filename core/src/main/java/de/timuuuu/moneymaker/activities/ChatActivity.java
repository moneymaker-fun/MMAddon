package de.timuuuu.moneymaker.activities;

import com.google.gson.JsonParser;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.ChatClient;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.util.concurrent.task.Task;

@AutoActivity
@Link("chat.lss")
public class ChatActivity extends Activity {

  private MoneyMakerAddon addon;

  private TextFieldWidget chatInput;
   private static List<ComponentWidget> chatMessages;

  public ChatActivity(MoneyMakerAddon addon) {
    this.addon = addon;
    chatMessages = new ArrayList<>();
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.chat.title");
    titleWidget.addId("chat-title");
    this.document.addChild(titleWidget);

    DivWidget chatContainer = new DivWidget();
    chatContainer.addId("chat-container");

    VerticalListWidget<ComponentWidget> chatList = new VerticalListWidget<>().addId("chat-messages");

    chatMessages.forEach(chatList::addChild);

    ScrollWidget chatScroll = new ScrollWidget(chatList, new ListSession<>());
    chatContainer.addChild(chatScroll);

    DivWidget onlineContainer = new DivWidget();
    onlineContainer.addId("online-container");

    DivWidget inputContainer = new DivWidget();
    inputContainer.addId("input-container");

    chatInput = new TextFieldWidget();
    chatInput.addId("chat-input");
    chatInput.submitButton().set(true);
    chatInput.maximalLength(250);

    chatInput.submitHandler(message -> {
      this.submitMessage();
    });

    inputContainer.addChild(chatInput);

    this.document.addChild(chatContainer);
    this.document.addChild(onlineContainer);
    this.document.addChild(inputContainer);

    // Start a thread to listen for incoming messages from the server
    new Thread(() -> {
      try {
        Socket socket = new Socket("78.31.64.201", 12345); // Replace with your server IP and port
        BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String serverMessage;
        while ((serverMessage = serverIn.readLine()) != null) {
          // Handle the received message (e.g., display it in your chat interface)
          MoneyChatMessage chatMessage = MoneyChatMessage.fromJson(new JsonParser().parse(serverMessage).getAsJsonObject()); // Implement this method
          addChatMessage(chatMessage);
        }

        socket.close(); // Close the socket when done
      } catch (IOException e) {
        e.printStackTrace();
        // Handle connection error
      }
    }).start();
  }

  private void submitMessage() {
    String message = this.chatInput.getText();
    message = message.trim();
    if (!message.isEmpty()) {
      this.chatInput.setEditable(false);
      this.chatInput.addId("blocked");
      this.labyAPI.minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);

      // Send the message to the chat server
      try {
        Socket socket = new Socket("78.31.64.201", 12345); // Replace with your server IP and port
        PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
        serverOut.println(message);
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
        // Handle connection error
      }

      this.chatInput.setText("");
      this.labyAPI.minecraft().executeNextTick(() -> this.chatInput.setFocused(true));
      Task.builder(() -> {
        this.chatInput.setEditable(true);
        this.chatInput.removeId("blocked");
        this.reload();
      }).delay(3, TimeUnit.SECONDS).build().execute();
    }
  }

  public void addChatMessage(MoneyChatMessage chatMessage) {
    if(chatMessages == null) return;
    if(chatMessage == null) return;
    String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    Component component = Component.text("§e" + time + "  ")
        .append(Component.icon(Icon.head(chatMessage.uuid(), true, false), 15))
        .append(Component.text("  §b" + chatMessage.userName() + "§8: §7" + chatMessage.message()));
    ComponentWidget messageWidget = ComponentWidget.component(component);
    messageWidget.addId("chat-message");
    chatMessages.add(messageWidget);
    this.reload();
  }

  private void sendToServer(String message) {
    MoneyChatMessage chatMessage = new MoneyChatMessage(
        this.addon.labyAPI().getUniqueId(),
        this.addon.labyAPI().getName(),
        message);

    addChatMessage(chatMessage);
    ChatClient.sendMessage(chatMessage);
  }

}
