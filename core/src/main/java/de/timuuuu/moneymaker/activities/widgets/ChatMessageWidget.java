package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.popup.ChatReportActivity;
import de.timuuuu.moneymaker.activities.popup.MuteActivity;
import de.timuuuu.moneymaker.moneychat.util.MoneyChatMessage;
import de.timuuuu.moneymaker.enums.MoneyChatMessageType;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import de.timuuuu.moneymaker.utils.Util;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteLabyMod;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.user.group.Group;

public class ChatMessageWidget extends FlexibleContentWidget { // FlexibleContentWidget | Default > HorizontalListWidget

  private MoneyMakerAddon addon;

  private String time;
  private MoneyChatMessage chatMessage = null;
  private Component customMessage;
  private MoneyChatMessageType messageType;

  public ChatMessageWidget(MoneyMakerAddon addon, String time, MoneyChatMessage chatMessage) {
    this.addon = addon;
    this.time = time;
    this.chatMessage = chatMessage;
    this.messageType = chatMessage.messageType();
  }

  public ChatMessageWidget(MoneyMakerAddon addon, String time, Component customMessage) {
    this.addon = addon;
    this.time = time;
    this.customMessage = customMessage;
    this.messageType = MoneyChatMessageType.SERVER;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    UUID uuid = Laby.labyAPI().getUniqueId();

    VerticalListWidget<Widget> flex = new VerticalListWidget<>().addId("flex");

    HorizontalListWidget header = new HorizontalListWidget().addId("message-header");
    if (this.messageType == MoneyChatMessageType.PLAYER) {
      header.addEntry(new IconWidget(Icon.head(chatMessage.uuid())).addId("avatar"));
      Component senderComponent = Component.text(chatMessage.rank().getChatPrefix() + chatMessage.userName());
      senderComponent.clickEvent(ClickEvent.openUrl("https://laby.net/@" + this.chatMessage().userName()));
      header.addEntry(ComponentWidget.component(senderComponent).addId("sender"));

      Group labyRank = Laby.references().gameUserService().gameUser(chatMessage.uuid()).visibleGroup();
      if(!labyRank.isDefault()) {
        IconWidget labyIconWidget = new IconWidget(SpriteLabyMod.WHITE_WOLF_HIGH_RES).addId("labymod-rank");
        labyIconWidget.color().set(labyRank.getColor().getRGB());
        if(labyRank.getTagName() != null) {
          labyIconWidget.setHoverComponent(Component.text("LABYMOD", NamedTextColor.WHITE).decorate(
              TextDecoration.BOLD).append(Component.text(" ")).append(Component.text(labyRank.getTagName()).color(labyRank.getTextColor())));
        }
        header.addEntry(labyIconWidget);
      }
    } else {
      header.addEntry(new IconWidget(this.messageType.icon()).addId("avatar"));
      header.addEntry(ComponentWidget.component(this.messageType.userName()).addId("sender"));
    }
    header.addEntry(ComponentWidget.text(time).addId("timestamp"));
    if(this.chatMessage != null && this.chatMessage.fromServerCache()) {
      ComponentWidget cacheInfoWidget = ComponentWidget.component(Component.icon(SpriteCommon.EXCLAMATION_MARK, 5)).addId("cache-info");
      cacheInfoWidget.setHoverComponent(Component.translatable("moneymaker.ui.chat.info.cache"));
      header.addEntry(cacheInfoWidget);
    }
    if(Util.isStaff(uuid)) {
      if(this.chatMessage != null) {
        if(!this.chatMessage.messageId().equals("UNKNOWN")) {
          header.addEntry(ComponentWidget.text("(ID: " + this.chatMessage.messageId() + ")").addId("message-id"));
        }
        if(this.chatMessage.deleted()) {
          header.addEntry(ComponentWidget.text("(Gelöscht)").addId("deleted-info"));
        }
      }
    }

    if(this.messageType == MoneyChatMessageType.PLAYER) {
      if(this.chatMessage != null && !this.chatMessage.deleted()) {

        // User is Staff member - add Mute button
        if(Util.isStaff(uuid)) {
          if(!this.chatMessage.uuid().equals(Laby.labyAPI().getUniqueId())) {

            if(!Util.isStaff(this.chatMessage.uuid())) {
              ButtonWidget muteButton = ButtonWidget.i18n("moneymaker.ui.chat.button.mute").addId("mute-button");
              muteButton.setPressable(() -> {
                Laby.labyAPI().minecraft().executeNextTick(() -> {
                  Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new MuteActivity(
                      this.addon,
                      Laby.labyAPI().getUniqueId(),
                      Laby.labyAPI().getName(),
                      this.chatMessage,
                      Laby.labyAPI().minecraft().minecraftWindow().currentScreen()
                  ));
                });
              });
              header.addEntry(muteButton);
            }
          }

          // User is normal - add Report button
        } else {

          if(!Util.isAdmin(this.chatMessage.uuid())) {
            if(!this.chatMessage.uuid().equals(Laby.labyAPI().getUniqueId())) {
              ButtonWidget reportButton = ButtonWidget.i18n("moneymaker.ui.chat.button.report").addId("report-button");
              reportButton.setPressable(() -> {
                Laby.labyAPI().minecraft().executeNextTick(() -> {
                  Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ChatReportActivity(
                      this.addon,
                      Laby.labyAPI().getUniqueId(),
                      Laby.labyAPI().getName(),
                      this.chatMessage,
                      Laby.labyAPI().minecraft().minecraftWindow().currentScreen()
                  ));
                });
              });
              header.addEntry(reportButton);
            }
          }

        }

      }
    }

    if(this.chatMessage != null && !this.chatMessage.deleted()) {
      ButtonWidget deleteButton = ButtonWidget.deleteButton().addId("delete-button");
      deleteButton.setPressable(() -> {
        this.addon.moneyChatClient().sendPacket(new PacketMessageDelete(Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), this.chatMessage.messageId()));
      });
      header.addEntry(deleteButton);
    }

    flex.addChild(header);

    VerticalListWidget<Widget> messageContentWidget = new VerticalListWidget<>().addId("message-content");
    ComponentWidget componentMessageWidget;
    if(chatMessage != null) {
      componentMessageWidget = ComponentWidget.component(PlainTextComponentSerializer.plainUrl()
          .deserialize(chatMessage.message()));
      if(this.chatMessage.deleted()) {
        componentMessageWidget.addId("deleted");
      }
    } else {
      componentMessageWidget = ComponentWidget.component(customMessage);
    }
    componentMessageWidget.addId("component-message", "tile");
    messageContentWidget.addChild(componentMessageWidget);

    flex.addChild(messageContentWidget);
    this.addContent(flex);

  }

  public MoneyChatMessage chatMessage() {
    return chatMessage;
  }

  public MoneyChatMessageType messageType() {
    return messageType;
  }
}
