package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;

public class ChatMessageWidget extends HorizontalListWidget {

  private String time;
  private MoneyChatMessage chatMessage = null;
  private String customMessage;

  public ChatMessageWidget(String time, MoneyChatMessage chatMessage) {
    this.time = time;
    this.chatMessage = chatMessage;
  }

  public ChatMessageWidget(String time, String customMessage) {
    this.time = time;
    this.customMessage = customMessage;
  }


  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(this.chatMessage != null) {

      String color;
      if(!Util.isDev(chatMessage.uuid().toString())) {
        color = chatMessage.staff() ? "§8[§cStaff§8] §c" : "§b";
      } else {
        color = "§8[§4Dev§8] §c";
      }

      this.addEntry(ComponentWidget.text("§e" + time + " ").addId("chat-msg-time"));
      this.addEntry(ComponentWidget.component(Component.icon(Icon.head(this.chatMessage.uuid(), true, false), 10)).addId("chat-msg-icon"));
      this.addEntry(ComponentWidget.text(" " + color + chatMessage.userName() + " §8- §7").addId("chat-msg-playerName"));
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
