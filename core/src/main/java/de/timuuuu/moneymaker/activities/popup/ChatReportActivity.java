package de.timuuuu.moneymaker.activities.popup;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatClient.ChatAction;
import de.timuuuu.moneymaker.chat.MoneyChatMessage;
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
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import java.util.UUID;

@Link("popup/report.lss")
@AutoActivity
public class ChatReportActivity extends SimpleActivity {

  private MoneyMakerAddon addon;
  private UUID reporterUUID;
  private String reporterName;
  private MoneyChatMessage chatMessage;
  private String uuid;
  private String userName;

  private ScreenInstance previousScreen;

  private ReportReason reportReason = ReportReason.SPAM;

  public ChatReportActivity(MoneyMakerAddon addon, UUID reporterUUID, String reporterName, MoneyChatMessage chatMessage,
      ScreenInstance previousScreen) {
    this.addon = addon;
    this.reporterUUID = reporterUUID;
    this.reporterName = reporterName;
    this.chatMessage = chatMessage;
    this.uuid = chatMessage.uuid().toString();
    this.userName = chatMessage.userName();
    this.previousScreen = previousScreen;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    IconWidget headWidget = new IconWidget(Icon.head(UUID.fromString(this.uuid)).enableHat()).addId("head");
    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.report.form.title", this.userName).addId("title");
    header.addEntry(headWidget);
    header.addEntry(titleWidget);

    ComponentWidget messageTitleWidget = ComponentWidget.i18n("moneymaker.report.form.message").addId("message-title");
    TextFieldWidget messageInputWidget = new TextFieldWidget().addId("message-input");
    messageInputWidget.setEditable(false);
    messageInputWidget.setText(this.chatMessage.message());

    ComponentWidget reasonTitleWidget = ComponentWidget.i18n("moneymaker.report.form.reason.title").addId("reason-title");
    DropdownWidget<ReportReason> reasonDropdownWidget = DropdownWidget.create(ReportReason.SPAM, reason -> {
      this.reportReason = reason;
    }).addId("reason-input");
    reasonDropdownWidget.setTranslationKeyPrefix("moneymaker.report.form.reason.type");
    reasonDropdownWidget.addAll(ReportReason.values());

    ButtonWidget sendButton = ButtonWidget.i18n("moneymaker.report.form.send").addId("send-button");
    sendButton.setPressable(() -> {
      if(sendForm()) {
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
        this.addon.pushNotification(
            Component.translatable("moneymaker.report.form.success.title", NamedTextColor.DARK_GREEN),
            Component.translatable("moneymaker.report.form.success.text", NamedTextColor.GREEN, Component.text(this.userName, NamedTextColor.YELLOW))
        );
      }
    });

    ButtonWidget closeButton = ButtonWidget.i18n("moneymaker.report.form.abort").addId("close-button");
    closeButton.setPressable(() -> {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
    });

    FlexibleContentWidget content = new FlexibleContentWidget().addId("content");

    FlexibleContentWidget messageContainer = new FlexibleContentWidget().addId("message-container");
    messageContainer.addContent(messageTitleWidget);
    messageContainer.addContent(messageInputWidget);
    content.addContent(messageContainer);

    FlexibleContentWidget reasonContainer = new FlexibleContentWidget().addId("reason-container");
    reasonContainer.addContent(reasonTitleWidget);
    reasonContainer.addContent(reasonDropdownWidget);
    content.addContent(reasonContainer);

    FlexibleContentWidget buttonContainer = new FlexibleContentWidget().addId("button-container");
    buttonContainer.addContent(sendButton);
    buttonContainer.addContent(closeButton);
    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(content);

    this.document.addChild(container);
  }

  private boolean sendForm() {
    JsonObject object = new JsonObject();
    object.addProperty("uuid", this.uuid);
    object.addProperty("playerName", this.userName);
    object.addProperty("reason", this.reportReason.getName());
    object.addProperty("originalChatMessage", this.chatMessage.message());

    if(!this.addon.chatClient().sendChatAction(this.reporterUUID, this.reporterName, ChatAction.REPORT, object)) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.mute.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.mute.form.invalid.chatError", NamedTextColor.RED)
      );
      return false;
    }

    return true;
  }

  public enum ReportReason {
    SPAM("Spamming"),
    CHOICEOFWORDS("Wortwahl"),
    BEHAVIOR("Verhalten"),
    ADVERTISING("Werbung"),
    PROVOCATION("Provokation"),
    OTHER("Sonstiges");

    private final String name;

    ReportReason(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

}
