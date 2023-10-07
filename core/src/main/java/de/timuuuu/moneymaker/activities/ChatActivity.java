package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
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
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.labyconnect.LabyConnectSession;
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

    if(AddonSettings.playingOn.contains("MoneyMaker")) {
      chatInput = new TextFieldWidget();
      chatInput.addId("chat-input");
      chatInput.submitButton().set(true);
      chatInput.maximalLength(250);

      chatInput.submitHandler(message -> {
        this.submitMessage();
      });

      inputContainer.addChild(chatInput);
    } else {
      ComponentWidget componentWidget = ComponentWidget.i18n("moneymaker.ui.chat.not-connected");
      componentWidget.addId("chat-error");
      inputContainer.addChild(componentWidget);
    }

    this.document.addChild(chatContainer);
    this.document.addChild(onlineContainer);
    this.document.addChild(inputContainer);

  }

  public static void addChatMessage(MoneyChatMessage chatMessage) {
    if(chatMessages == null) return;
    if(chatMessage == null) return;
    Component component = Component.text("§e" + chatMessage.time())
        .append(Component.icon(Icon.head(chatMessage.uuid())))
        .append(Component.text("§b" + chatMessage.userName() + "§8: §7" + chatMessage.message()));
    ComponentWidget messageWidget = ComponentWidget.component(component);
    messageWidget.addId("chat-message");
    chatMessages.add(messageWidget);
  }

  private void submitMessage() {
    String message = this.chatInput.getText();
    message = message.trim();
    if(!message.isEmpty()) {
      this.chatInput.setEditable(false);
      this.chatInput.addId("blocked");
      this.labyAPI.minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);
      this.sendToClients(message);
      this.chatInput.setText("");
      this.labyAPI.minecraft().executeNextTick(() -> this.chatInput.setFocused(true));
      Task.builder(() -> {
        this.chatInput.setEditable(true);
        this.chatInput.removeId("blocked");
        this.reload();
      }).delay(3, TimeUnit.SECONDS).build().execute();
    }
  }

  private void sendToClients(String message) {
    LabyConnectSession session = this.addon.labyAPI().labyConnect().getSession();
    if(session == null) return;

    String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    MoneyChatMessage chatMessage = new MoneyChatMessage(
        time,
        this.addon.labyAPI().getUniqueId(),
        this.addon.labyAPI().getName(),
        message);

    addChatMessage(chatMessage);

    //JsonObject object = new JsonObject();
    //object.add("chatMessage", chatMessage.toJson());
    //session.sendBroadcastPayload("moneymaker_addon_chat", object);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
  }

}
