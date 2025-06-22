package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private final MoneyChatClient moneyChatClient;

    public PacketEncoder(MoneyChatClient moneyChatClient) {
        this.moneyChatClient = moneyChatClient;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) {
        int packetId = this.moneyChatClient.protocol().getPacketId(packet);
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        packetBuffer.writeVarIntToBuffer(packetId);
        packet.write(packetBuffer);
    }
}
