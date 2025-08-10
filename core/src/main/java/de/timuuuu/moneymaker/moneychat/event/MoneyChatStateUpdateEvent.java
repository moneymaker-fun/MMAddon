package de.timuuuu.moneymaker.moneychat.event;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;

public class MoneyChatStateUpdateEvent extends MoneyChatEvent {

  private final MoneyChatClient.MoneyChatState state;

  public MoneyChatStateUpdateEvent(MoneyChatClient chatClient, MoneyChatClient.MoneyChatState state) {
    super(chatClient);
    this.state = state;
  }

  public MoneyChatClient.MoneyChatState state() {
    return state;
  }

}
