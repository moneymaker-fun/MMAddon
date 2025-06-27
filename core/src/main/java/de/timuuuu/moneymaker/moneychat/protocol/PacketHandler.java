package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketDisconnect;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.PacketEncryptionRequest;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.PacketHelloPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.PacketLoginComplete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserMute;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserRankUpdate;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserUnmute;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class PacketHandler extends SimpleChannelInboundHandler<Packet> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
    this.handlePacket(packet);
  }

  protected void handlePacket(Packet packet) {
    packet.handle(this);
  }

  public abstract void handle(PacketHelloPong packet);

  public abstract void handle(PacketEncryptionRequest packet);

  public abstract void handle(PacketPing packet);

  public abstract void handle(PacketLoginComplete packet);

  public abstract void handle(PacketMessage packet);
  public abstract void handle(PacketClearChat packet);
  public abstract void handle(PacketMessageDelete packet);
  public abstract void handle(PacketUserMute packet);
  public abstract void handle(PacketUserUnmute packet);
  public abstract void handle(PacketUserRankUpdate packet);
  public abstract void handle(PacketPlayerStatus  packet);

  public abstract void handle(PacketDisconnect packet);

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
  }



}
