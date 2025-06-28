package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketDisconnect;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionRequest;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketLoginComplete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserMute;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserRankUpdate;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserUnmute;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class MoneyPacketHandler extends SimpleChannelInboundHandler<MoneyPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MoneyPacket packet) {
    this.handlePacket(packet);
  }

  protected void handlePacket(MoneyPacket packet) {
    packet.handle(this);
  }

  public abstract void handle(MoneyPacketEncryptionRequest packet);
  public abstract void handle(MoneyPacketLoginComplete packet);
  public abstract void handle(PacketMessage packet);
  public abstract void handle(PacketClearChat packet);
  public abstract void handle(PacketMessageDelete packet);
  public abstract void handle(PacketUserMute packet);
  public abstract void handle(PacketUserUnmute packet);
  public abstract void handle(PacketUserRankUpdate packet);
  public abstract void handle(PacketPlayerStatus  packet);

  public abstract void handle(MoneyPacketDisconnect packet);

  public abstract void handle(MoneyPacketPing packet);

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
  }



}
