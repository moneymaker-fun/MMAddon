package de.timuuuu.moneymaker.moneychat.event;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import net.labymod.api.event.Event;

public class MoneyChatEvent implements Event {

  private final MoneyChatClient chatClient;

  protected MoneyChatEvent(MoneyChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public MoneyChatClient chatClient() {
    return chatClient;
  }

}
