package de.timuuuu.moneymaker.moneychat.pipeline;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class PacketSplitter extends ByteToMessageDecoder {

  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> objects) {
    buffer.markReaderIndex();
    byte[] a = new byte[3];

    for(int i = 0; i < a.length; ++i) {
      if (!buffer.isReadable()) {
        buffer.resetReaderIndex();
        return;
      }

      a[i] = buffer.readByte();
      if (a[i] >= 0) {
        MoneyPacketBuffer buf = new MoneyPacketBuffer(Unpooled.wrappedBuffer(a));

        try {
          int varInt = buf.readVarIntFromBuffer();
          if (buffer.readableBytes() >= varInt) {
            objects.add(buffer.readBytes(varInt));
            return;
          }

          buffer.resetReaderIndex();
        } finally {
          buf.buffer().release();
        }

        return;
      }
    }

    throw new RuntimeException("length wider than 21-bit");
  }

}
