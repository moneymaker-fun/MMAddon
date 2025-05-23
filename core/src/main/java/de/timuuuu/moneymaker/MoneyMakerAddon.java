package de.timuuuu.moneymaker;

import com.google.gson.Gson;
import de.timuuuu.moneymaker.activities.ChatActivity;
import de.timuuuu.moneymaker.activities.LeaderboardActivity;
import de.timuuuu.moneymaker.activities.PriceOverviewActivity;
import de.timuuuu.moneymaker.activities.navigation.MainActivity;
import de.timuuuu.moneymaker.activities.StartActivity;
import de.timuuuu.moneymaker.activities.navigation.MoneyMakerNavigationElement;
import de.timuuuu.moneymaker.badges.MoneyChatPrefix;
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
import de.timuuuu.moneymaker.listener.InventoryListener;
import de.timuuuu.moneymaker.listener.MoneyAddonListener;
import de.timuuuu.moneymaker.listener.NetworkPayloadListener;
import de.timuuuu.moneymaker.listener.ScoreBoardListener;
import de.timuuuu.moneymaker.listener.TickListener;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.settings.MoneyMakerConfiguration;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.ApiUtil;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import de.timuuuu.moneymaker.utils.DiscordAPI;
import de.timuuuu.moneymaker.utils.MoneyTextures.Common;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class MoneyMakerAddon extends LabyAddon<MoneyMakerConfiguration> {

  public static final HudWidgetCategory CATEGORY = new HudWidgetCategory("moneymaker");

  public Component prefix = Component.text("‖ ", NamedTextColor.DARK_GRAY)
      .append(Component.text("MoneyMaker ", NamedTextColor.GOLD))
      .append(Component.text("» ", NamedTextColor.DARK_GRAY));

  private ChatClient chatClient;

  private MainActivity mainActivity;
  private PriceOverviewActivity priceOverviewActivity;
  private LeaderboardActivity leaderboardActivity;
  private ChatActivity chatActivity;
  private StartActivity startActivity;

  private DiscordAPI discordAPI;
  private ApiUtil apiUtil;

  private AddonSettings addonSettings;
  private AddonUtil addonUtil;

  private EntityRenderListener entityRenderListener;

  private Gson gson;
  private static MoneyMakerAddon instance;

  @Override
  protected void preConfigurationLoad() {
    Laby.references().revisionRegistry().register(new SimpleRevision("moneymaker", new SemanticVersion("1.6.7"), "2025-03-31"));
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
    this.priceOverviewActivity = new PriceOverviewActivity(this);
    this.leaderboardActivity = new LeaderboardActivity(this);
    this.chatActivity = new ChatActivity(this);
    this.mainActivity = new MainActivity(this);

    this.chatClient = new ChatClient(this);

    this.gson = GsonUtil.DEFAULT_GSON;

    this.apiUtil.loadSettings();

    this.registerCommand(new TimerCommand(this));
    this.registerCommand(new ResetCommand(this));

    this.registerListener(new NetworkPayloadListener(this));
    this.registerListener(new ChatReceiveListener(this));
    this.registerListener(new MoneyAddonListener(this));
    this.registerListener(new ChatServerListener(this));
    this.registerListener(new ScoreBoardListener(this));
    this.registerListener(this.entityRenderListener = new EntityRenderListener(this));
    this.registerListener(new TickListener(this));
    this.registerListener(new InventoryListener(this));

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
    labyAPI().chatProvider().prefixRegistry().register("moneymaker_icon", new MoneyChatPrefix(this));

    this.logger().info("Enabled the Addon");

    this.chatClient.connectStartUp();

    this.addonSettings.setFallbackCoordinates(false);
    this.addonSettings.selectUpdateMode(this.configuration().updateMode().get());
    this.apiUtil.loadCoordinates();
    this.apiUtil.loadLeaderboard(false);

    // Configuration Listeners

    this.configuration().updateMode().addChangeListener((type, oldValue, newValue) -> this.addonSettings.selectUpdateMode(newValue));

    this.configuration().chatConfiguration.showDetailedLocation().addChangeListener((type, oldValue, newValue) ->
        this.chatClient().util().sendPlayerStatus(this.labyAPI().getUniqueId().toString(), this.labyAPI().getName(), false)
    );

    this.configuration().discordConfiguration.enabled().addChangeListener((type, oldValue, newValue) -> {
      if(newValue) {
        this.discordAPI.update();
      } else {
        this.discordAPI.removeCustom();
      }
    });

    //this.configuration().discordConfiguration.showLocation().addChangeListener(aBoolean -> this.discordAPI.update());
    //this.configuration().discordConfiguration.showStats().addChangeListener(aBoolean -> this.discordAPI.update());
    //this.configuration().discordConfiguration.showCaveLevel().addChangeListener(aBoolean -> this.discordAPI.update());

  }

  @Override
  protected Class<MoneyMakerConfiguration> configurationClass() {
    return MoneyMakerConfiguration.class;
  }

  public Gson gson() {
    return gson;
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

  public PriceOverviewActivity priceOverviewActivity() {
    return priceOverviewActivity;
  }

  public LeaderboardActivity leaderboardActivity() {
    return leaderboardActivity;
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

  public EntityRenderListener entityRenderListener() {
    return entityRenderListener;
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

  public void sendServerUpdate(String gameMode) {
    if(!configuration().showCustomGameSwitchNotifications().get()) return;
    LabyConnectSession session = labyAPI().labyConnect().getSession();
    if(session == null) return;
    session.sendCurrentServer(labyAPI().serverController().getCurrentServerData(), gameMode, false);
  }

}
