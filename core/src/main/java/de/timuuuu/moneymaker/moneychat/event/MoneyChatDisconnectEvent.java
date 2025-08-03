package de.timuuuu.moneymaker.moneychat.event;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.MoneyChatClient.Initiator;

public class MoneyChatDisconnectEvent extends MoneyChatEvent {

  private final String reason;
  private final Initiator initiator;

  public MoneyChatDisconnectEvent(MoneyChatClient chatClient, Initiator initiator, String reason) {
    super(chatClient);
    this.initiator = initiator;
    this.reason = reason;
  }

  public Initiator getInitiator() {
    return this.initiator;
  }

  public String getReason() {
    return this.reason;
  }

}
