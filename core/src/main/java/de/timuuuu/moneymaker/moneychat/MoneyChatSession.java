package de.timuuuu.moneymaker.moneychat;

import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.moneychat.MoneyChatClient.MoneyChatState;
import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import net.labymod.api.Laby;
import net.labymod.api.client.session.Session;

public class MoneyChatSession extends PacketHandler {

  private final MoneyChatClient moneyChatClient;

  private Session session;
  private boolean premium;
  private boolean connectionEstablished;
  private boolean authenticated;

  public MoneyChatSession(MoneyChatClient moneyChatClient, Session session) {
    this.moneyChatClient = moneyChatClient;
    this.session = session;
    this.premium = session.isPremium();
    this.connectionEstablished = false;
    this.authenticated = false;
  }

  protected void handlePacket(Packet packet) {
    this.connectionEstablished = true;
    super.handlePacket(packet);
    this.moneyChatClient.keepAlive();
  }

  @Override
  public void handle(PacketMessage packet) {
    Laby.fireEvent(new MoneyChatReceiveEvent(packet.message()));
  }

  @Override
  public void handle(PacketClearChat packet) {
    this.moneyChatClient.addon().chatActivity().clearChat(true);
  }

  @Override
  public void handle(PacketMessageDelete packet) {
    this.moneyChatClient.addon().chatActivity().deleteMessage(packet.messageId());
  }

  public void dispose() {
    this.connectionEstablished = false;
  }

  private void resetAuthentication() {
    this.authenticated = false;
    this.premium = false;
    this.moneyChatClient.updateState(MoneyChatState.HELLO);
    //this.labyConnect.fireEventSync(new LabyConnectRejectAuthenticationEvent(this.labyConnect));
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public boolean isPremium() {
    return premium;
  }

  public boolean isConnectionEstablished() {
    return connectionEstablished;
  }
}
