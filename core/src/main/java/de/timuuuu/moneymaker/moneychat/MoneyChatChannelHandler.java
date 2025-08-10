package de.timuuuu.moneymaker.moneychat;

import de.timuuuu.moneymaker.moneychat.pipeline.PacketDecoder;
import de.timuuuu.moneymaker.moneychat.pipeline.PacketEncoder;
import de.timuuuu.moneymaker.moneychat.pipeline.PacketSplitter;
import de.timuuuu.moneymaker.moneychat.pipeline.PacketPrepender;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;

public class MoneyChatChannelHandler extends ChannelInitializer<NioSocketChannel> {

  private MoneyChatClient moneyChatClient;
  private final MoneyPacketHandler packetHandler;
  private NioSocketChannel channel;

  public MoneyChatChannelHandler(MoneyChatClient moneyChatClient, MoneyPacketHandler packetHandler) {
    this.moneyChatClient = moneyChatClient;
    this.packetHandler = packetHandler;
  }

  public void initChannel(NioSocketChannel channel) {
    this.channel = channel;
    channel.pipeline()
        .addLast("timeout", new ReadTimeoutHandler(30L, TimeUnit.SECONDS))
        .addLast("splitter", new PacketSplitter())
        .addLast("decoder", new PacketDecoder(this.moneyChatClient))
        .addLast("prepender", new PacketPrepender())
        .addLast("encoder", new PacketEncoder(this.moneyChatClient))
        .addLast(this.packetHandler);
  }

  public NioSocketChannel getChannel() {
    return channel;
  }
}
