package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketPrepender extends MessageToByteEncoder<ByteBuf> {

  protected void encode(ChannelHandlerContext ctx, ByteBuf buffer, ByteBuf byteBuf) {
    int var4 = buffer.readableBytes();
    int var5 = MoneyPacketBuffer.getVarIntSize(var4);
    if (var5 > 3) {
      throw new IllegalArgumentException("unable to fit " + var4 + " into 3");
    } else {
      byteBuf.ensureWritable(var5 + var4);
      MoneyPacketBuffer.writeVarIntToBuffer(byteBuf, var4);
      byteBuf.writeBytes(buffer, buffer.readerIndex(), var4);
    }
  }

}
