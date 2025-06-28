package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.MoneyChatClient;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.labymod.api.util.logging.Logging;

public class PacketEncoder extends MessageToByteEncoder<MoneyPacket> {

  private final Logging LOGGER = Logging.getLogger();

    private final MoneyChatClient moneyChatClient;

    public PacketEncoder(MoneyChatClient moneyChatClient) {
        this.moneyChatClient = moneyChatClient;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MoneyPacket packet, ByteBuf byteBuf) {
        int packetId = this.moneyChatClient.protocol().getPacketId(packet);

      LOGGER.debug("[MoneyChatClient] [OUT] " + packetId + " " + packet.getClass().getSimpleName());

        MoneyPacketBuffer packetBuffer = new MoneyPacketBuffer(byteBuf);
        packetBuffer.writeVarIntToBuffer(packetId);
        packet.write(packetBuffer);
    }
}
