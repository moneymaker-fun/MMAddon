package de.timuuuu.moneymaker.activities.popup;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
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

@Link("popup/feedback.lss")
@AutoActivity
public class FeedbackActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  private ScreenInstance previousScreen;

  private UUID uuid;
  private String username;
  private String date;
  private String addonVersion;
  private String minecraftVersion;
  private FeedbackType feedbackType;

  private TextFieldWidget dateInputWidget;
  private TextFieldWidget descriptionInputWidget;
  private TextFieldWidget attachmentsInputWidget;
  private TextFieldWidget availabilityInputWidget;

  private final String DATE_FORMATE = "dd.MM.yyyy HH:mm";

  public FeedbackActivity(MoneyMakerAddon addon, ScreenInstance previousScreen) {
    this.addon = addon;
    this.previousScreen = previousScreen;
    this.uuid = this.addon.labyAPI().getUniqueId();
    this.username = this.addon.labyAPI().getName();
    this.date = new SimpleDateFormat(this.DATE_FORMATE).format(new Date());
    this.addonVersion = this.addon.addonInfo().getVersion();
    this.minecraftVersion = this.addon.labyAPI().minecraft().getVersion();
    this.feedbackType = FeedbackType.BUGREPORT;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.feedback.form.title").addId("title");
    header.addEntry(titleWidget);

    //VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");

    ComponentWidget nameTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.minecraftName").addId("username");

    HorizontalListWidget nameInput = new HorizontalListWidget().addId("name-input");

    IconWidget headWidget = new IconWidget(Icon.head(this.uuid, true, false)).addId("head");

    TextFieldWidget nameInputWidget = new TextFieldWidget().addId("username-input");
    nameInputWidget.setText(this.username);
    nameInputWidget.setEditable(false);

    ComponentWidget dateTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.date").addId("date");
    dateInputWidget = new TextFieldWidget().addId("date-input");
    dateInputWidget.setText(this.date);

    ComponentWidget addonVersionTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.addonVersion").addId("addon-version");
    TextFieldWidget addonVersionInputWidget = new TextFieldWidget().addId("addon-version-input");
    addonVersionInputWidget.setText(this.addonVersion);
    addonVersionInputWidget.setEditable(false);

    ComponentWidget mcVersionTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.minecraftVersion").addId("minecraft-version");
    TextFieldWidget mcVersionInputWidget = new TextFieldWidget().addId("minecraft-version-input");
    mcVersionInputWidget.setText(this.minecraftVersion);
    mcVersionInputWidget.setEditable(false);

    ComponentWidget typeTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.type.title").addId("type");
    DropdownWidget<FeedbackType> typeDropdownWidget = DropdownWidget.create(FeedbackType.BUGREPORT, feedbackType -> {
      this.feedbackType = feedbackType;
    }).addId("type-dropdown");
    typeDropdownWidget.setTranslationKeyPrefix("moneymaker.feedback.form.type");
    typeDropdownWidget.addAll(FeedbackType.values());

    ComponentWidget descriptionTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.description.title").addId("description");
    descriptionInputWidget = new TextFieldWidget().addId("description-input");
    descriptionInputWidget.placeholder(Component.translatable("moneymaker.feedback.form.description.placeholder"));

    ComponentWidget attachmentsTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.attachments.title").addId("attachments");
    attachmentsInputWidget = new TextFieldWidget().addId("attachments-input");
    attachmentsInputWidget.placeholder(Component.translatable("moneymaker.feedback.form.attachments.placeholder"));

    ComponentWidget availabilityTitleWidget = ComponentWidget.i18n("moneymaker.feedback.form.availability").addId("availability");
    availabilityInputWidget = new TextFieldWidget().addId("availability-input");


    ButtonWidget sendButton = ButtonWidget.i18n("moneymaker.feedback.form.send").addId("send-button");
    sendButton.setPressable(() -> {
      if(sendForm()) {
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
        this.addon.pushNotification(
            Component.translatable("moneymaker.feedback.form.success.title", NamedTextColor.DARK_GREEN),
            Component.translatable("moneymaker.feedback.form.success.text", NamedTextColor.GREEN)
        );
      }
    });

    ButtonWidget closeButton = ButtonWidget.i18n("moneymaker.feedback.form.close").addId("close-button");
    closeButton.setPressable(() -> {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
    });

    FlexibleContentWidget buttonContainer = new FlexibleContentWidget().addId("button-container");
    buttonContainer.addContent(sendButton);
    buttonContainer.addContent(closeButton);

    nameInput.addEntry(headWidget);
    nameInput.addEntry(nameInputWidget);

    FlexibleContentWidget headContent = new FlexibleContentWidget().addId("head-content");

    FlexibleContentWidget nameContainer = new FlexibleContentWidget().addId("name-container");
    nameContainer.addContent(nameTitleWidget);
    nameContainer.addContent(nameInput);
    headContent.addContent(nameContainer);

    FlexibleContentWidget dateContainer = new FlexibleContentWidget().addId("date-container");
    dateContainer.addContent(dateTitleWidget);
    dateContainer.addContent(dateInputWidget);
    headContent.addContent(dateContainer);

    FlexibleContentWidget addonVersionContainer = new FlexibleContentWidget().addId("addon-version-container");
    addonVersionContainer.addContent(addonVersionTitleWidget);
    addonVersionContainer.addContent(addonVersionInputWidget);
    headContent.addContent(addonVersionContainer);

    FlexibleContentWidget mcVersionContainer = new FlexibleContentWidget().addId("minecraft-version-container");
    mcVersionContainer.addContent(mcVersionTitleWidget);
    mcVersionContainer.addContent(mcVersionInputWidget);
    headContent.addContent(mcVersionContainer);

    FlexibleContentWidget typeContainer = new FlexibleContentWidget().addId("type-container");
    typeContainer.addContent(typeTitleWidget);
    typeContainer.addContent(typeDropdownWidget);
    headContent.addContent(typeContainer);

    FlexibleContentWidget content = new FlexibleContentWidget().addId("content");

    FlexibleContentWidget descriptionContainer = new FlexibleContentWidget().addId("description-container");
    descriptionContainer.addContent(descriptionTitleWidget);
    descriptionContainer.addContent(descriptionInputWidget);
    content.addContent(descriptionContainer);

    FlexibleContentWidget attachmentsContainer = new FlexibleContentWidget().addId("attachments-container");
    attachmentsContainer.addContent(attachmentsTitleWidget);
    attachmentsContainer.addContent(attachmentsInputWidget);
    content.addContent(attachmentsContainer);

    FlexibleContentWidget availabilityContainer = new FlexibleContentWidget().addId("availability-container");
    availabilityContainer.addContent(availabilityTitleWidget);
    availabilityContainer.addContent(availabilityInputWidget);
    content.addContent(availabilityContainer);

    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(headContent);
    container.addContent(content);

    this.document.addChild(container);
  }

  private boolean sendForm() {
    SimpleDateFormat formatter = new SimpleDateFormat(this.DATE_FORMATE);
    formatter.setLenient(false);
    try {
      formatter.parse(dateInputWidget.getText());
    } catch (Exception ignored) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.feedback.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.feedback.form.invalid.invalidDate", NamedTextColor.RED, Component.text(this.DATE_FORMATE))
      );
      return false;
    }

    if(descriptionInputWidget.getText().length() < 5) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.feedback.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.feedback.form.invalid.descriptionShort", NamedTextColor.RED)
      );
      return false;
    }

    if(feedbackType == FeedbackType.BUGREPORT) {
      if(attachmentsInputWidget.getText().isBlank()) {
        this.addon.pushNotification(
            Component.translatable("moneymaker.feedback.form.invalid.title", NamedTextColor.DARK_RED),
            Component.translatable("moneymaker.feedback.form.invalid.noAttachment", NamedTextColor.RED)
        );
        return false;
      }
      if(!(attachmentsInputWidget.getText().startsWith("http://") || attachmentsInputWidget.getText().startsWith("https://"))) {
        this.addon.pushNotification(
            Component.translatable("moneymaker.feedback.form.invalid.title", NamedTextColor.DARK_RED),
            Component.translatable("moneymaker.feedback.form.invalid.noUrl", NamedTextColor.RED)
        );
        return false;
      }
    }

    if(availabilityInputWidget.getText().isBlank()) {
      this.addon.pushNotification(
          Component.translatable("moneymaker.feedback.form.invalid.title", NamedTextColor.DARK_RED),
          Component.translatable("moneymaker.feedback.form.invalid.noAvailability", NamedTextColor.RED)
      );
      return false;
    }

    //TODO: Send API Request or Chat Server message

    return true;
  }

  public enum FeedbackType {
    IDEA,
    BUGREPORT
  }

}
