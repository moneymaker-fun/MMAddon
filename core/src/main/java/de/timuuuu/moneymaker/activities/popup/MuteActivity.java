package de.timuuuu.moneymaker.activities.popup;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.moneychat.util.MoneyChatMessage;
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
import net.labymod.api.util.TimeUnit;
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
  private String reason;

  private ScreenInstance previousScreen;

  private TextFieldWidget reasonInput;
  private TextFieldWidget durationInput;

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

  public MuteActivity(MoneyMakerAddon addon, UUID executorUUID, String executorName, String userName, UUID uuid, ScreenInstance previousScreen, String reason) {
    this.addon = addon;
    this.executorUUID = executorUUID;
    this.executorName = executorName;
    this.uuid = uuid;
    this.userName = userName;
    this.previousScreen = previousScreen;
    this.reason = reason;
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
    if(this.chatMessage != null) {
      messageInputWidget.setText(this.chatMessage.message());
    }

    ComponentWidget reasonTitleWidget = ComponentWidget.i18n("moneymaker.mute.form.reason").addId("reason-title");
    reasonInput = new TextFieldWidget().addId("reason-input");
    if(this.reason != null) {
      reasonInput.setText(this.reason);
    }

    ComponentWidget durationTitleWidget = ComponentWidget.i18n("moneymaker.mute.form.duration").addId("duration-title");
    durationInput = new TextFieldWidget().addId("duration-input");
    durationInput.placeholder(Component.text("7d 2h 10m"));

    ButtonWidget sendButton = ButtonWidget.i18n("moneymaker.mute.form.send").addId("send-button");
    sendButton.setPressable(this::sendForm);

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

    FlexibleContentWidget durationContainer = new FlexibleContentWidget().addId("duration-container");
    durationContainer.addContent(durationTitleWidget);
    durationContainer.addContent(durationInput);
    content.addContent(durationContainer);

    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(content);

    this.document.addChild(container);
  }

  private void sendForm() {

    if(!Util.isStaff(this.executorUUID)) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.mute.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.mute.form.invalid.noStaff", NamedTextColor.RED)
      );
      return;
    }

    if(reasonInput.getText().isBlank()) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.mute.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.mute.form.invalid.noReason", NamedTextColor.RED)
      );
      return;
    }

    long duration = TimeUnit.parseToLong(this.durationInput.getText());
    if(duration <= 0) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.mute.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.mute.form.invalid.invalidDuration", NamedTextColor.RED)
      );
      return;
    }

    this.addon.moneyChatClient().sendPacket(new PacketUserMute(this.executorUUID, this.executorName, this.uuid, this.userName,
        reasonInput.getText(), this.chatMessage != null ? this.chatMessage.messageId() : "-", duration));

    Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
    this.addon.pushNotification(
        Component.translatable("moneymaker.mute.form.success.title", NamedTextColor.DARK_GREEN),
        Component.translatable("moneymaker.mute.form.success.text", NamedTextColor.GREEN,
            Component.text(this.userName, NamedTextColor.YELLOW),
            Component.text(this.reasonInput.getText(), NamedTextColor.YELLOW),
            Component.text(TimeUnit.parseToString(duration), NamedTextColor.YELLOW))
    );
  }



}
