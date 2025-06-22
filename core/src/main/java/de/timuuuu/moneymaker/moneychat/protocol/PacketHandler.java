package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
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

  public abstract void handle(PacketMessage packet);
  public abstract void handle(PacketClearChat packet);
  public abstract void handle(PacketMessageDelete packet);

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
  }



}
