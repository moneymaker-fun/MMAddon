package de.timuuuu.moneymaker.moneychat;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.moneychat.MoneyChatClient.Initiator;
import de.timuuuu.moneymaker.moneychat.MoneyChatClient.MoneyChatState;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketAddonMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketAddonStatistics;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketDisconnect;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionRequest;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionResponse;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketLoginComplete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserMute;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserRankUpdate;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserUnmute;
import de.timuuuu.moneymaker.moneychat.util.CryptManager;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import de.timuuuu.moneymaker.utils.Util;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.session.MinecraftAuthenticator;
import net.labymod.api.client.session.Session;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.UUID;

public class MoneyChatSession extends MoneyPacketHandler {

  private final MoneyChatClient moneyChatClient;

  private Session session;
  private boolean premium;
  private boolean connectionEstablished;
  private boolean authenticated;

  private boolean muted;
  private String muteReason;

  public MoneyChatSession(MoneyChatClient moneyChatClient, Session session) {
    this.moneyChatClient = moneyChatClient;
    this.session = session;
    this.premium = session.isPremium();
    this.connectionEstablished = false;
    this.authenticated = false;
  }

  protected void handlePacket(MoneyPacket packet) {
    this.connectionEstablished = true;
    super.handlePacket(packet);
    this.moneyChatClient.keepAlive();
  }

  public void handle(MoneyPacketEncryptionRequest encryptionRequest) {
    try {
      PublicKey publicKey = CryptManager.decodePublicKey(encryptionRequest.getPublicKey());
      SecretKey secretKey = CryptManager.createNewSharedKey();
      String serverId = encryptionRequest.getServerId();
      MinecraftAuthenticator authenticator = this.moneyChatClient.addon().labyAPI().minecraft().authenticator();

      String hash = CryptManager.getServerIdHash(serverId, publicKey, secretKey);
      NioSocketChannel nio = this.moneyChatClient.getChannel();
      authenticator.joinServer(this.session, hash).exceptionally((throwable) -> false).thenAccept((result) -> {
        if (this.moneyChatClient.getChannel() == nio) {
          this.moneyChatClient.sendPacket(new MoneyPacketEncryptionResponse(secretKey.getEncoded()));
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      this.moneyChatClient.disconnect(Initiator.CLIENT, e.getMessage());
    }

  }

  @Override
  public void handle(MoneyPacketPing packet) {
    this.moneyChatClient.sendPacket(new MoneyPacketPong());
    this.moneyChatClient.keepAlive();
  }

  public void handle(MoneyPacketLoginComplete packet) {
    this.moneyChatClient.updateState(MoneyChatState.PLAY);
    this.authenticated = true;
    this.moneyChatClient.keepAlive();
    this.moneyChatClient.addon().chatActivity().reloadScreen();

    this.moneyChatClient.sendPacket(new PacketAddonStatistics("add", this.session.getUniqueId(), this.session.getUsername(),
        this.moneyChatClient.addon().addonInfo().getVersion(), this.moneyChatClient.addon().labyAPI().minecraft().getVersion(), this.moneyChatClient.addon().labyAPI().labyModLoader().isAddonDevelopmentEnvironment()));

    this.moneyChatClient.sendPacket(new PacketPlayerStatus(Laby.labyAPI().getUniqueId(), Laby.labyAPI().getName(), MoneyRank.USER,
        Util.currentServer(), MoneyMakerAddon.instance().addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion(),
        Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment(), this.moneyChatClient.addon().configuration().chatConfiguration.hideOnlineStatus.get()));

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

  @Override
  public void handle(PacketUserMute packet) {
    if(this.moneyChatClient.addon().labyAPI().getUniqueId().equals(packet.targetUUID())) {
      this.muted = true;
      this.muteReason = packet.reason();
      this.moneyChatClient.addon().chatActivity().addCustomChatMessage(Component.translatable("moneymaker.mute.ui.muted", NamedTextColor.RED));
      this.moneyChatClient.addon().chatActivity().reloadScreen();
    }
  }

  @Override
  public void handle(PacketUserUnmute packet) {
    if(this.moneyChatClient.addon().labyAPI().getUniqueId().equals(packet.targetUUID())) {
      this.muted = false;
      this.muteReason = "";
      this.moneyChatClient.addon().chatActivity().addCustomChatMessage(Component.translatable("moneymaker.mute.ui.unmuted", NamedTextColor.GREEN));
      this.moneyChatClient.addon().chatActivity().reloadScreen();
    }
  }

  @Override
  public void handle(PacketUserRankUpdate packet) {
    UUID uuid = UUID.fromString(packet.uuid());
    MoneyRank rank = MoneyRank.byName(packet.rank());
    if(rank != null) {
      if(AddonUtil.playerStatus.containsKey(uuid)) {
        AddonUtil.playerStatus.get(uuid).rank(rank);
      }
      if(this.moneyChatClient.addon().labyAPI().getUniqueId().equals(uuid)) {
        this.moneyChatClient.addon().pushNotification(
            Component.translatable("moneymaker.notification.chat.rank-changed.title", NamedTextColor.GREEN),
            Component.translatable("moneymaker.notification.chat.rank-changed.text", NamedTextColor.YELLOW, Component.text(rank.getOnlineColor() + rank.getName())),
            rank.getIcon()
        );
      }
    }
  }

  @Override
  public void handle(PacketPlayerStatus packet) {
    Laby.fireEvent(new MoneyPlayerStatusEvent(
        packet.uuid(),
        new MoneyPlayer(packet.uuid(),
            packet.username(),
            packet.server(),
            packet.addonVersion(),
            packet.minecraftVersion(),
            packet.rank(),
            packet.hideOnlineStatus()
        )
    ));
  }

  @Override
  public void handle(MoneyPacketDisconnect packet) {
    this.moneyChatClient.disconnect(Initiator.SERVER, packet.reason());
  }

  @Override
  public void handle(MoneyPacketAddonMessage packet) {
    if(packet.getKey().equals("unauthenticated")) {
      this.resetAuthentication();
    }
  }

  public void dispose() {
    this.connectionEstablished = false;
  }

  private void resetAuthentication() {
    this.authenticated = false;
    this.premium = false;
    this.moneyChatClient.updateState(MoneyChatState.LOGIN);
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

  public boolean muted() {
    return muted;
  }

  public String muteReason() {
    return muteReason;
  }

}
