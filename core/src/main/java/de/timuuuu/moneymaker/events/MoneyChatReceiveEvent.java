package de.timuuuu.moneymaker.events;

import de.timuuuu.moneymaker.moneychat.util.MoneyChatMessage;
import net.labymod.api.event.Event;

public class MoneyChatReceiveEvent implements Event {

  private final MoneyChatMessage chatMessage;

  public MoneyChatReceiveEvent(MoneyChatMessage chatMessage) {
    this.chatMessage = chatMessage;
  }

  public MoneyChatMessage chatMessage() {
    return chatMessage;
  }
}
