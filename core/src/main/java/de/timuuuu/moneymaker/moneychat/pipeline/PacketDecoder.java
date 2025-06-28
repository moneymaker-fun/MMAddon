package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.labymod.api.util.logging.Logging;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

  private final Logging LOGGER = Logging.getLogger();

    private final MoneyChatClient moneyChatClient;

    public PacketDecoder(MoneyChatClient moneyChatClient) {
        this.moneyChatClient = moneyChatClient;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 1) {
            MoneyPacketBuffer packetBuffer = new MoneyPacketBuffer(byteBuf);
            int id = packetBuffer.readVarIntFromBuffer();
            MoneyPacket packet = this.moneyChatClient.protocol().getPacket(id);

            LOGGER.debug("[MoneyChatClient] [IN] " + id + " " + packet.getClass().getSimpleName());

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
