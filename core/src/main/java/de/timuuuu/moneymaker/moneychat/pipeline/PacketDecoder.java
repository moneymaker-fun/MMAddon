package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    private final MoneyChatClient moneyChatClient;

    public PacketDecoder(MoneyChatClient moneyChatClient) {
        this.moneyChatClient = moneyChatClient;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 1) {
            PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
            int id = packetBuffer.readVarIntFromBuffer();
            Packet packet = this.moneyChatClient.protocol().getPacket(id);

            packet.read(packetBuffer);
            if (byteBuf.readableBytes() > 0) {
                String simpleName = packet.getClass().getSimpleName();
                throw new RuntimeException("Unknown packet type: " + simpleName);
            } else {
                list.add(packet);
            }
        }
    }
}
