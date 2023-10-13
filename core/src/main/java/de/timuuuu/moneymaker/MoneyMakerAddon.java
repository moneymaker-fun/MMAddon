package de.timuuuu.moneymaker;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.activities.BoosterActivity;
import de.timuuuu.moneymaker.activities.ChatActivity;
import de.timuuuu.moneymaker.activities.MoneyMakerMainActivity;
import de.timuuuu.moneymaker.activities.StartActivity;
import de.timuuuu.moneymaker.activities.navigation.MoneyMakerNavigationElement;
import de.timuuuu.moneymaker.badges.MoneyIconTag;
import de.timuuuu.moneymaker.badges.MoneyTabBadge;
import de.timuuuu.moneymaker.badges.MoneyTextTag;
import de.timuuuu.moneymaker.commands.TimerCommand;
import de.timuuuu.moneymaker.hudwidget.BalanceWidget;
import de.timuuuu.moneymaker.hudwidget.BlockSessionWidget;
import de.timuuuu.moneymaker.hudwidget.BoosterCountWidget;
import de.timuuuu.moneymaker.hudwidget.BreakGoalWidget;
import de.timuuuu.moneymaker.hudwidget.DebrisPriceWidget;
import de.timuuuu.moneymaker.hudwidget.KillCountWidget;
import de.timuuuu.moneymaker.hudwidget.SwordStatsWidget;
import de.timuuuu.moneymaker.hudwidget.WorkerCountWidget;
import de.timuuuu.moneymaker.hudwidget.WorkerPriceWidget;
import de.timuuuu.moneymaker.listener.ChatReceiveListener;
import de.timuuuu.moneymaker.listener.ChatServerListener;
import de.timuuuu.moneymaker.listener.DisconnectListener;
import de.timuuuu.moneymaker.listener.EntityRenderListener;
import de.timuuuu.moneymaker.listener.MoneyAddonListener;
import de.timuuuu.moneymaker.listener.NetworkPayloadListener;
import de.timuuuu.moneymaker.listener.ScoreBoardListener;
import de.timuuuu.moneymaker.listener.TickListener;
import de.timuuuu.moneymaker.utils.ChatClient;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.util.concurrent.task.Task;

@AddonMain
public class MoneyMakerAddon extends LabyAddon<MoneyMakerConfiguration> {

  public static final HudWidgetCategory CATEGORY = new HudWidgetCategory("moneymaker");

  public ChatClient chatClient;

  public MoneyMakerMainActivity moneyMakerMainActivity;
  public ChatActivity chatActivity;
  public StartActivity startActivity;

  private static MoneyMakerAddon instance;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    instance = this;

    this.startActivity = new StartActivity(this);
    this.chatActivity = new ChatActivity(this);
    this.moneyMakerMainActivity = new MoneyMakerMainActivity(this);

    this.chatClient = new ChatClient(this);

    this.registerCommand(new TimerCommand(this));

    this.registerListener(new NetworkPayloadListener(this));
    this.registerListener(new ChatReceiveListener(this));
    this.registerListener(new DisconnectListener(this));
    this.registerListener(new MoneyAddonListener(this));
    this.registerListener(new ChatServerListener(this));
    this.registerListener(new ScoreBoardListener(this));
    this.registerListener(new EntityRenderListener(this));
    this.registerListener(new TickListener(this));

    CurrencyUtil.setUnits();

    labyAPI().navigationService().register("moneymaker_main_ui", new MoneyMakerNavigationElement(this));

    labyAPI().hudWidgetRegistry().categoryRegistry().register(CATEGORY);
    labyAPI().hudWidgetRegistry().register(new BoosterCountWidget(this));
    labyAPI().hudWidgetRegistry().register(new BlockSessionWidget(this));
    labyAPI().hudWidgetRegistry().register(new BreakGoalWidget(this));
    labyAPI().hudWidgetRegistry().register(new BalanceWidget(this));
    labyAPI().hudWidgetRegistry().register(new WorkerCountWidget(this));
    labyAPI().hudWidgetRegistry().register(new DebrisPriceWidget(this));
    labyAPI().hudWidgetRegistry().register(new WorkerPriceWidget(this));
    labyAPI().hudWidgetRegistry().register(new SwordStatsWidget(this));
    labyAPI().hudWidgetRegistry().register(new KillCountWidget(this));

    labyAPI().tagRegistry().registerAfter("labymod_role", "moneymaker_text", PositionType.ABOVE_NAME, new MoneyTextTag());
    labyAPI().tagRegistry().register("moneymaker_icon", PositionType.RIGHT_TO_NAME, new MoneyIconTag());
    Laby.references().badgeRegistry().register("moneymaker_tab_icon", net.labymod.api.client.entity.player.badge.PositionType.LEFT_TO_NAME, new MoneyTabBadge());

    this.logger().info("Enabled the Addon");

    this.chatClient.connect(false);
    this.chatClient.heartBeat();
    this.chatClient.sendLaunchData(this.labyAPI().getUniqueId().toString(), this.labyAPI().getName());
    Task.builder(() -> {
      if(this.chatClient.socket().isClosed()) {
        this.chatClient.connect(false);
      }
    }).delay(5, TimeUnit.SECONDS).build().execute();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if(configuration().exportOnShutdown().get()) {
        BoosterActivity.writeLinkedListToCSV(true);
      }
      JsonObject data = new JsonObject();
      data.addProperty("uuid", this.labyAPI().getUniqueId().toString());
      data.addProperty("userName", this.labyAPI().getName());
      data.addProperty("server", "OFFLINE");
      data.addProperty("addonVersion", this.addonInfo().getVersion());
      this.chatClient.sendMessage("playerStatus", data);
      this.chatClient.sendQuitData(this.labyAPI().getUniqueId().toString());
    }));
  }

  @Override
  protected Class<MoneyMakerConfiguration> configurationClass() {
    return MoneyMakerConfiguration.class;
  }

  public static MoneyMakerAddon instance() {
    return instance;
  }

  public void pushNotification(Component title, Component text) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png")))
        .type(Type.ADVANCEMENT);
    labyAPI().notificationController().push(builder.build());
  }

  public void pushNotification(Component title, Component text, Icon icon) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(icon)
        .type(Type.ADVANCEMENT);
    labyAPI().notificationController().push(builder.build());
  }

  public static void pushNotification(Component title, Component text, Component buttonText,
      Runnable buttonAction) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png")))
        .addButton(NotificationButton.of(buttonText, buttonAction))
        .type(Type.ADVANCEMENT);
    Laby.labyAPI().notificationController().push(builder.build());
  }
}
