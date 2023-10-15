package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer.Rank;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;

public class ChatMessageWidget extends HorizontalListWidget { // FlexibleContentWidget

  private String time;
  private MoneyChatMessage chatMessage = null;
  private String customMessage;
  private boolean headless = false;

  public ChatMessageWidget(String time, MoneyChatMessage chatMessage) {
    this.time = time;
    this.chatMessage = chatMessage;
  }

  public ChatMessageWidget(String time, String customMessage) {
    this.time = time;
    this.customMessage = customMessage;
    this.headless = true;
  }

  /*@Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    VerticalListWidget<Widget> flex = new VerticalListWidget<>().addId("flex");

    if(!this.headless) {
      HorizontalListWidget header = new HorizontalListWidget().addId("message-header");
      String color;
      if(!Util.isDev(chatMessage.uuid().toString())) {
        color = chatMessage.staff() ? "§8[§cStaff§8] §c" : "§b";
      } else {
        color = "§8[§4Dev§8] §c";
      }
      header.addEntry(ComponentWidget.text(color + chatMessage.userName()).addId("sender"));
      header.addEntry(ComponentWidget.text(time).addId("timestamp"));
      flex.addChild(header);
    }

    VerticalListWidget<Widget> messageContentWidget = new VerticalListWidget<>().addId("message-content");
    ComponentWidget componentMessageWidget = ComponentWidget.text(chatMessage != null ? chatMessage.message() : customMessage).addId(new String[] {"component-message", "tile"});
    messageContentWidget.addChild(componentMessageWidget);
    if(!this.headless) {
      messageContentWidget.addId("headless");
    }

    this.addContent(flex);
    if(!this.headless) {
      IconWidget avatar = new IconWidget(Icon.head(chatMessage.uuid())).addId("avatar");
      this.addContent(avatar);
    }

  }*/

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(this.chatMessage != null) {

      String prefix;
      if(chatMessage.rank() == Rank.DEVELOPER) {
        prefix = "§8[§4Dev§8] §4";
      } else if(chatMessage.rank() == Rank.STAFF) {
        prefix = "§8[§cStaff§8] §c";
      } else if(chatMessage.rank() == Rank.DONATOR) {
        prefix = "§8[§6Don§8] §6";
      } else {
        prefix = "§e";
      }

      this.addEntry(ComponentWidget.text("§e" + time + " ").addId("chat-msg-time"));
      this.addEntry(ComponentWidget.component(
          Component.icon(Icon.head(this.chatMessage.uuid(), true, false), 10)).addId("chat-msg-icon"));
      this.addEntry(ComponentWidget.text(" " + prefix + chatMessage.userName() + " §8- §7").addId("chat-msg-playerName"));
      //this.addEntry(ComponentWidget.text(chatMessage.message()).addId("chat-msg-message"));

      // Split: 65; 130; 195;

      ComponentWidget messageWidget;
      if(chatMessage.message().length() > 195) {
        String split1 = chatMessage.message().substring(0, 65);
        String split2 = chatMessage.message().substring(65, 130);
        String split3 = chatMessage.message().substring(130, 195);
        String split4 = chatMessage.message().substring(195);
        messageWidget = ComponentWidget.text(split1 + "\n" + split2 + "\n" + split3 + "\n" + split4);
      } else if(chatMessage.message().length() > 130) {
        String split1 = chatMessage.message().substring(0, 65);
        String split2 = chatMessage.message().substring(65, 130);
        String split3 = chatMessage.message().substring(130);
        messageWidget = ComponentWidget.text(split1 + "\n" + split2 + "\n" + split3);
      } else if(chatMessage.message().length() > 65) {
        String split1 = chatMessage.message().substring(0, 65);
        String split2 = chatMessage.message().substring(65);
        messageWidget = ComponentWidget.text(split1 + "\n" + split2);
      } else {
        messageWidget = ComponentWidget.text(chatMessage.message());
        this.heightPrecision().set(10F);
      }
      this.addEntry(messageWidget.addId("chat-msg-message"));

    } else {

      ComponentWidget timeWidget = ComponentWidget.text("§e" + time + " ");
      ComponentWidget messageWidget = ComponentWidget.text(this.customMessage);

      this.addEntry(timeWidget);
      this.addEntry(messageWidget);

    }

  }

}
