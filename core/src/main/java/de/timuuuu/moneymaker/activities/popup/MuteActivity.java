package de.timuuuu.moneymaker.activities.popup;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserMute;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import java.util.UUID;

@Link("popup/mute.lss")
@AutoActivity
public class MuteActivity extends SimpleActivity {

  private MoneyMakerAddon addon;
  private UUID executorUUID;
  private String executorName;
  private MoneyChatMessage chatMessage;
  private UUID uuid;
  private String userName;

  private ScreenInstance previousScreen;

  private TextFieldWidget reasonInput;

  public MuteActivity(MoneyMakerAddon addon, UUID executorUUID, String executorName, MoneyChatMessage chatMessage,
      ScreenInstance previousScreen) {
    this.addon = addon;
    this.executorUUID = executorUUID;
    this.executorName = executorName;
    this.chatMessage = chatMessage;
    this.uuid = chatMessage.uuid();
    this.userName = chatMessage.userName();
    this.previousScreen = previousScreen;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.mute.form.title", this.userName).addId("title");
    header.addEntry(headWidget);
    header.addEntry(titleWidget);

    ComponentWidget messageTitleWidget = ComponentWidget.i18n("moneymaker.mute.form.message").addId("message-title");
    TextFieldWidget messageInputWidget = new TextFieldWidget().addId("message-input");
    messageInputWidget.setEditable(false);
    messageInputWidget.setText(this.chatMessage.message());

    ComponentWidget reasonTitleWidget = ComponentWidget.i18n("moneymaker.mute.form.reason").addId("reason-title");
    reasonInput = new TextFieldWidget().addId("reason-input");


    ButtonWidget sendButton = ButtonWidget.i18n("moneymaker.mute.form.send").addId("send-button");
    sendButton.setPressable(() -> {
      if(sendForm()) {
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
        this.addon.pushNotification(
            Component.translatable("moneymaker.mute.form.success.title", NamedTextColor.DARK_GREEN),
            Component.translatable("moneymaker.mute.form.success.text", NamedTextColor.GREEN, Component.text(this.userName, NamedTextColor.YELLOW), Component.text(this.reasonInput.getText(), NamedTextColor.YELLOW))
        );
      }
    });

    ButtonWidget closeButton = ButtonWidget.i18n("moneymaker.mute.form.close").addId("close-button");
    closeButton.setPressable(() -> {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
    });

    FlexibleContentWidget buttonContainer = new FlexibleContentWidget().addId("button-container");
    buttonContainer.addContent(sendButton);
    buttonContainer.addContent(closeButton);

    FlexibleContentWidget content = new FlexibleContentWidget().addId("content");

    FlexibleContentWidget messageContainer = new FlexibleContentWidget().addId("message-container");
    messageContainer.addContent(messageTitleWidget);
    messageContainer.addContent(messageInputWidget);
    content.addContent(messageContainer);

    FlexibleContentWidget reasonContainer = new FlexibleContentWidget().addId("reason-container");
    reasonContainer.addContent(reasonTitleWidget);
    reasonContainer.addContent(reasonInput);
    content.addContent(reasonContainer);

    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(content);

    this.document.addChild(container);
  }

  private boolean sendForm() {

    if(!Util.isStaff(this.executorUUID)) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.mute.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.mute.form.invalid.noStaff", NamedTextColor.RED)
      );
      return false;
    }

    if(reasonInput.getText().isBlank()) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.mute.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.mute.form.invalid.noReason", NamedTextColor.RED)
      );
      return false;
    }

    this.addon.moneyChatClient().sendPacket(new PacketUserMute(this.executorUUID, this.executorName, this.uuid, this.userName, reasonInput.getText()));

    return true;
  }



}
