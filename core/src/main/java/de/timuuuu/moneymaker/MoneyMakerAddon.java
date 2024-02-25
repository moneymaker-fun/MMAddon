package de.timuuuu.moneymaker;

import de.timuuuu.moneymaker.activities.ChatActivity;
import de.timuuuu.moneymaker.activities.MainActivity;
import de.timuuuu.moneymaker.activities.StartActivity;
import de.timuuuu.moneymaker.activities.navigation.MoneyMakerNavigationElement;
import de.timuuuu.moneymaker.badges.MoneyIconTag;
import de.timuuuu.moneymaker.badges.MoneyTabBadge;
import de.timuuuu.moneymaker.badges.MoneyTextTag;
import de.timuuuu.moneymaker.boosters.BoosterUtil;
import de.timuuuu.moneymaker.chat.ChatClient;
import de.timuuuu.moneymaker.commands.ResetCommand;
import de.timuuuu.moneymaker.commands.TimerCommand;
import de.timuuuu.moneymaker.hudwidget.BalanceWidget;
import de.timuuuu.moneymaker.hudwidget.DebrisPriceWidget;
import de.timuuuu.moneymaker.hudwidget.DebrisTimerWidget;
import de.timuuuu.moneymaker.hudwidget.TimerDisplayWidget;
import de.timuuuu.moneymaker.hudwidget.WorkerCountWidget;
import de.timuuuu.moneymaker.hudwidget.WorkerPriceWidget;
import de.timuuuu.moneymaker.hudwidget.farming.ActivatedBoosterWidget;
import de.timuuuu.moneymaker.hudwidget.farming.BlockSessionWidget;
import de.timuuuu.moneymaker.hudwidget.farming.BoosterCountWidget;
import de.timuuuu.moneymaker.hudwidget.farming.BreakGoalWidget;
import de.timuuuu.moneymaker.hudwidget.farming.KillCountWidget;
import de.timuuuu.moneymaker.hudwidget.farming.LatestBoosterDisplayWidget;
import de.timuuuu.moneymaker.hudwidget.farming.SwordStatsWidget;
import de.timuuuu.moneymaker.listener.ChatReceiveListener;
import de.timuuuu.moneymaker.listener.ChatServerListener;
import de.timuuuu.moneymaker.listener.EntityRenderListener;
import de.timuuuu.moneymaker.listener.MoneyAddonListener;
import de.timuuuu.moneymaker.listener.NetworkPayloadListener;
import de.timuuuu.moneymaker.listener.ScoreBoardListener;
import de.timuuuu.moneymaker.listener.TickListener;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.DiscordAPI;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.settings.MoneyMakerConfiguration;
import de.timuuuu.moneymaker.utils.ApiUtil;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import de.timuuuu.moneymaker.utils.MoneyTextures.Common;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class MoneyMakerAddon extends LabyAddon<MoneyMakerConfiguration> {

  public static final HudWidgetCategory CATEGORY = new HudWidgetCategory("moneymaker");

  public String prefix = "§8‖ §6MoneyMaker §8» §7";

  private ChatClient chatClient;

  private MainActivity mainActivity;
  private ChatActivity chatActivity;
  private StartActivity startActivity;

  private DiscordAPI discordAPI;
  private ApiUtil apiUtil;

  private AddonSettings addonSettings;
  private AddonUtil addonUtil;

  private static MoneyMakerAddon instance;

  @Override
  protected void preConfigurationLoad() {
    Laby.references().revisionRegistry().register(new SimpleRevision("moneymaker", new SemanticVersion("1.4.0"), "2024-02-21"));
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();

    instance = this;
    this.discordAPI = new DiscordAPI(this);
    this.apiUtil = new ApiUtil(this);

    this.addonSettings = new AddonSettings();
    this.addonUtil = new AddonUtil(this);

    this.startActivity = new StartActivity(this);
    this.chatActivity = new ChatActivity(this);
    this.mainActivity = new MainActivity(this);

    this.chatClient = new ChatClient(this);

    this.registerCommand(new TimerCommand(this));
    this.registerCommand(new ResetCommand(this));

    this.registerListener(new NetworkPayloadListener(this));
    this.registerListener(new ChatReceiveListener(this));
    this.registerListener(new MoneyAddonListener(this));
    this.registerListener(new ChatServerListener(this));
    this.registerListener(new ScoreBoardListener(this));
    this.registerListener(new EntityRenderListener(this));
    this.registerListener(new TickListener(this));

    CurrencyUtil.setUnits();
    BoosterUtil.loadData();

    labyAPI().navigationService().register("moneymaker_main_ui", new MoneyMakerNavigationElement(this));

    labyAPI().hudWidgetRegistry().categoryRegistry().register(CATEGORY);
    labyAPI().hudWidgetRegistry().register(new BoosterCountWidget(this));
    labyAPI().hudWidgetRegistry().register(new BlockSessionWidget(this));
    labyAPI().hudWidgetRegistry().register(new BreakGoalWidget(this));
    labyAPI().hudWidgetRegistry().register(new BalanceWidget(this));
    labyAPI().hudWidgetRegistry().register(new WorkerCountWidget(this));
    labyAPI().hudWidgetRegistry().register(new DebrisPriceWidget(this));
    labyAPI().hudWidgetRegistry().register(new DebrisTimerWidget(this));
    labyAPI().hudWidgetRegistry().register(new WorkerPriceWidget(this));
    labyAPI().hudWidgetRegistry().register(new SwordStatsWidget(this));
    labyAPI().hudWidgetRegistry().register(new KillCountWidget(this));
    labyAPI().hudWidgetRegistry().register(new TimerDisplayWidget(this));
    labyAPI().hudWidgetRegistry().register(new LatestBoosterDisplayWidget(this));
    labyAPI().hudWidgetRegistry().register(new ActivatedBoosterWidget(this));

    labyAPI().tagRegistry().registerAfter("labymod_role", "moneymaker_text", PositionType.ABOVE_NAME, new MoneyTextTag(this));
    labyAPI().tagRegistry().register("moneymaker_icon", PositionType.RIGHT_TO_NAME, new MoneyIconTag(this));
    Laby.references().badgeRegistry().register("moneymaker_tab_icon", net.labymod.api.client.entity.player.badge.PositionType.LEFT_TO_NAME, new MoneyTabBadge(this));

    this.logger().info("Enabled the Addon");

    this.chatClient.connectStartUp();

    this.addonSettings.setFallbackCoordinates(false);
    this.addonSettings.selectUpdateMode(this.configuration().updateMode().get());
    this.configuration().updateMode().addChangeListener((type, oldValue, newValue) -> this.addonSettings.selectUpdateMode(newValue));
    this.apiUtil.loadCoordinates();

    /*Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      this.chatClient.sendStatistics(true, this.labyAPI().getUniqueId().toString(), this.labyAPI().getName());
      JsonObject data = new JsonObject();
      data.addProperty("uuid", this.labyAPI().getUniqueId().toString());
      data.addProperty("userName", this.labyAPI().getName());
      data.addProperty("server", "OFFLINE");
      data.addProperty("addonVersion", this.addonInfo().getVersion());
      this.chatClient.sendMessage("playerStatus", data);

      this.chatClient.closeConnection();
      if(configuration().exportBoosterOnShutdown().get()) {
        BoosterActivity.writeLinkedListToCSV(true);
      }
    }));*/
  }

  @Override
  protected Class<MoneyMakerConfiguration> configurationClass() {
    return MoneyMakerConfiguration.class;
  }

  public static MoneyMakerAddon instance() {
    return instance;
  }

  public DiscordAPI discordAPI() {
    return discordAPI;
  }

  public ApiUtil apiUtil() {
    return apiUtil;
  }

  public AddonUtil addonUtil() {
    return addonUtil;
  }

  public AddonSettings addonSettings() {
    return addonSettings;
  }

  public MainActivity mainActivity() {
    return mainActivity;
  }

  public ChatActivity chatActivity() {
    return chatActivity;
  }

  public ChatClient chatClient() {
    return chatClient;
  }

  public StartActivity startActivity() {
    return startActivity;
  }

  public void pushNotification(Component title, Component text) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Common.ICON)
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }

  public void pushNotification(Component title, Component text, Icon icon) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(icon)
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }

  public static void pushNotification(Component title, Component text, Component buttonText,
      Runnable buttonAction) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Common.ICON)
        .addButton(NotificationButton.of(buttonText, buttonAction))
        .type(Type.SYSTEM);
    Laby.labyAPI().notificationController().push(builder.build());
  }

  public void pushNotification(Component title, Component text, Icon icon,
      Component buttonText, Runnable buttonAction) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(icon)
        .addButton(NotificationButton.of(buttonText, buttonAction))
        .type(Type.SYSTEM);
    Laby.labyAPI().notificationController().push(builder.build());
  }

}
