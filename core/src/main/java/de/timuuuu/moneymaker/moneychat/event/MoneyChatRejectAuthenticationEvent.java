package de.timuuuu.moneymaker.moneychat.event;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;

public class MoneyChatRejectAuthenticationEvent extends MoneyChatEvent {

  public MoneyChatRejectAuthenticationEvent(MoneyChatClient chatClient) {
    super(chatClient);
  }

}
