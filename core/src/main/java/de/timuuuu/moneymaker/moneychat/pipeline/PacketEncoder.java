package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<MoneyPacket> {

    private final MoneyChatClient moneyChatClient;

    public PacketEncoder(MoneyChatClient moneyChatClient) {
        this.moneyChatClient = moneyChatClient;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MoneyPacket packet, ByteBuf byteBuf) {
        int packetId = this.moneyChatClient.protocol().getPacketId(packet);
        MoneyPacketBuffer packetBuffer = new MoneyPacketBuffer(byteBuf);
        packetBuffer.writeVarIntToBuffer(packetId);
        packet.write(packetBuffer);
    }
}
