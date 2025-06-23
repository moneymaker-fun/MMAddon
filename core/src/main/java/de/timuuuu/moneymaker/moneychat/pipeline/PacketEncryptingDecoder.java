package de.timuuuu.moneymaker.moneychat.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import java.util.List;

public class PacketEncryptingDecoder extends MessageToMessageDecoder<ByteBuf> {

  private final EncryptionTranslator decryptionCodec;

  public PacketEncryptingDecoder(Cipher cipher) {
    this.decryptionCodec = new EncryptionTranslator(cipher);
  }

  protected void decode(
      ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws ShortBufferException, Exception {
    list.add(this.decryptionCodec.decipher(context, byteBuf));
  }

}
