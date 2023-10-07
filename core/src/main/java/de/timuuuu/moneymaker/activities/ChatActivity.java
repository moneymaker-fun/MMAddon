package de.timuuuu.moneymaker.activities;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import net.labymod.api.Constants.Resources;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@AutoActivity
@Link("chat.lss")
public class ChatActivity extends Activity {

  private MoneyMakerAddon addon;

  private TextFieldWidget chatInput;

  public ChatActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.chat.title");
    titleWidget.addId("chat-title");
    this.document.addChild(titleWidget);

    DivWidget chatContainer = new DivWidget();
    chatContainer.addId("chat-container");

    VerticalListWidget<ComponentWidget> chatList = new VerticalListWidget<>();
    chatList.addId("chat-messages");

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

  private void submitMessage() {
    String message = this.chatInput.getText();
    message = message.trim();
    if(!message.isEmpty()) {
      this.chatInput.setEditable(false);
      this.labyAPI.minecraft().sounds().playSound(Resources.SOUND_CHAT_MESSAGE, 0.35F, 1.0F);
      this.sendToClients(message);
      this.chatInput.setText("");
      this.labyAPI.minecraft().executeNextTick(() -> {
        this.chatInput.setFocused(true);
      });
      Task.builder(() -> {
        chatInput.setEditable(true);
      }).delay(3, TimeUnit.SECONDS);
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
    JsonObject object = new JsonObject();
    object.add("chatMessage", chatMessage.toJson());
    session.sendBroadcastPayload("moneymaker_addon_chat", object);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
  }

}
