package de.timuuuu.moneymaker.moneychat;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyChatProtocol;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketDisconnect;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketLogin;
import de.timuuuu.moneymaker.moneychat.session.MoneySession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.labymod.api.client.session.Session;
import net.labymod.api.client.session.Session.Type;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.concurrent.ThreadFactoryBuilder;
import net.labymod.api.event.EventBus;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkLoginEvent;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.labymod.api.util.I18n;
import net.labymod.api.util.io.LabyExecutors;
import net.labymod.api.util.time.TimeUtil;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MoneyChatClient {

  private MoneyMakerAddon addon;

  private final String ADDRESS = "chat.moneymakeraddon.de";
  private final int PORT = 62894;

  private SessionAccessor sessionAccessor;
  private MoneyChatProtocol protocol = new  MoneyChatProtocol();
  private MoneyChatState state;
  private MoneyChatSession session = null;
  private MoneyChatChannelHandler channelHandler = null;

  private Bootstrap bootstrap;
  private final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).withNameFormat("MoneyChatNio#%d").build());
  private final ExecutorService executor = LabyExecutors.newFixedThreadPool(2, "MoneyChatExecutor#%d");
  private final ScheduledExecutorService timeoutExecutor = LabyExecutors.newScheduledThreadPool(1, "MoneyChatTimeoutExecutor#%d");

  private final byte[] verifyToken = new byte[10];

  private long timeLastKeepAlive;
  private long timeNextConnect;
  private boolean doNotConnect;
  private int connectTries;
  private long lastConnectTriesReset;
  private String lastDisconnectReason;

  public MoneyChatClient(MoneyMakerAddon addon, SessionAccessor sessionAccessor, EventBus eventBus) {
    this.addon = addon;
    this.state = MoneyChatState.OFFLINE;
    this.timeNextConnect = TimeUtil.getMillis();
    this.connectTries = 0;
    this.lastConnectTriesReset = 0L;
    this.sessionAccessor = sessionAccessor;

    eventBus.registerListener(this);
  }

  public void prepareAsync() {
    LabyExecutors.executeBackgroundTask(this::prepare);
  }

  private void prepare() {
    this.timeoutExecutor.scheduleWithFixedDelay(() -> {
      try {
        long durationKeepAlive = TimeUtil.getMillis() - this.timeLastKeepAlive;
        long durationConnect = this.timeNextConnect - TimeUtil.getMillis();
        if (this.state != MoneyChatState.OFFLINE && durationKeepAlive > 25000L) {
          this.disconnect(Initiator.CLIENT, I18n.translate("moneymaker.notification.chat.no-connection"));
        }

        if (this.state == MoneyChatState.OFFLINE && !this.doNotConnect && durationConnect < 0L) {
          this.connect();
        }

        if (this.lastConnectTriesReset + 300000L < TimeUtil.getMillis()) {
          this.lastConnectTriesReset = TimeUtil.getMillis();
          this.connectTries = 0;
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }

    }, 0L, 5L, TimeUnit.SECONDS);
  }

  public void connect() {
    this.doNotConnect = false;
    this.executor.execute(() -> {
      synchronized(this) {
        if (this.state == MoneyChatState.OFFLINE) {
          this.keepAlive();
          this.updateState(MoneyChatState.HELLO);
          ++this.connectTries;
          Session session = this.sessionAccessor.getSession();
          if (session == null) {
            session = new MoneySession("Player", UUID.randomUUID(), null, Type.LEGACY);
          }

          this.session = new MoneyChatSession(this, session);
          this.channelHandler = new MoneyChatChannelHandler(this, this.session);
          this.lastDisconnectReason = null;
          this.bootstrap = new Bootstrap();
          this.bootstrap.group(this.nioEventLoopGroup);
          this.bootstrap.option(ChannelOption.TCP_NODELAY, true);
          this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
          this.bootstrap.channel(NioSocketChannel.class);
          this.bootstrap.handler(this.channelHandler);
          //ConnectAddressResolver.resolveAddress();

          try {
            this.bootstrap.connect(ADDRESS, PORT).syncUninterruptibly();
            this.sendPacket(new MoneyPacketLogin(this.addon.labyAPI().getName(), this.addon.labyAPI().getUniqueId()));
          } catch (Exception e) {
            e.printStackTrace();
            this.updateState(MoneyChatState.OFFLINE);
          }

        }
      }
    });
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {
    if (event.isAnotherAccount()) {
      this.disconnect(Initiator.USER, I18n.translate("labymod.activity.labyconnect.protocol.disconnect.sessionSwitch"));
      if (event.newSession().isPremium()) {
        this.connect();
      }

    }
  }

  @Subscribe
  public void onNetworkLogin(NetworkLoginEvent event) {
    if (!this.isAuthenticated()) {
      this.timeNextConnect = TimeUtil.getMillis() + 10000L;
    }

  }

  public void disconnect(Initiator initiator, String reason) {
    long delay = (long)((double)1000.0F * Math.random() * (double)60.0F);
    this.timeNextConnect = TimeUtil.getMillis() + 10000L + delay;
    if (this.doNotConnect && this.lastDisconnectReason != null) {
      this.lastDisconnectReason = reason;
    }

    if (this.state != MoneyChatState.OFFLINE) {
      if (this.session != null) {
        this.session.dispose();
      }

      //this.fireEventSync(new LabyConnectDisconnectEvent(this, initiator, I18n.translate(reason, new Object[0])));
      this.updateState(MoneyChatState.OFFLINE);
      this.sendPacket(new MoneyPacketDisconnect("Logout"), (channel) -> {
        if (channel.isOpen()) {
          channel.close();
        }

      });
      this.session = null;
    }

  }

  public void sendPacket(MoneyPacket packet) {
    this.sendPacket(packet, null);
  }

  public void sendPacket(MoneyPacket packet, Consumer<NioSocketChannel> callback) {
    NioSocketChannel channel = this.getChannel();
    if (channel != null && channel.isActive()) {
      if (channel.eventLoop().inEventLoop()) {
        channel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        if (callback != null) {
          callback.accept(channel);
        }
      } else {
        channel.eventLoop().execute(() -> {
          channel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
          if (callback != null) {
            callback.accept(channel);
          }

        });
      }

    }
  }

  public void updateState(MoneyChatState state) {
    synchronized(this) {
      this.state = state;
    }

    //this.fireEventSync(new LabyConnectStateUpdateEvent(this, this.state));
  }

  public void keepAlive() {
    this.timeLastKeepAlive = TimeUtil.getMillis();
  }

  public boolean isAuthenticated() {
    return this.state == MoneyChatState.PLAY;
  }

  public boolean isConnectionEstablished() {
    return this.state != MoneyChatState.OFFLINE && this.session != null && this.session.isConnectionEstablished();
  }

  public NioSocketChannel getChannel() {
    return this.channelHandler == null ? null : this.channelHandler.getChannel();
  }

  public MoneyChatSession session() {
    return session;
  }

  public MoneyChatProtocol protocol() {
    return protocol;
  }

  public MoneyMakerAddon addon() {
    return addon;
  }

  public byte[] getVerifyToken() {
    return verifyToken;
  }

  public String getLastDisconnectReason() {
    return lastDisconnectReason;
  }

  public enum MoneyChatState {
    HELLO(-1),
    LOGIN(0),
    PLAY(1),
    ALL(2),
    OFFLINE(3);

    private final int id;

    MoneyChatState(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

  }

  public enum Initiator {
    USER,
    CLIENT,
    SERVER
  }

}
