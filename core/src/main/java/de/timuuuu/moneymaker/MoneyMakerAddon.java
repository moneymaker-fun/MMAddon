package de.timuuuu.moneymaker;

import de.timuuuu.moneymaker.activities.ChatActivity;
import de.timuuuu.moneymaker.activities.MoneyMakerMainActivity;
import de.timuuuu.moneymaker.activities.navigation.MoneyMakerNavigationElement;
import de.timuuuu.moneymaker.hudwidget.BoosterCountWidget;
import de.timuuuu.moneymaker.listener.ChatReceiveListener;
import de.timuuuu.moneymaker.listener.DisconnectListener;
import de.timuuuu.moneymaker.listener.MoneyAddonListener;
import de.timuuuu.moneymaker.listener.NetworkPayloadListener;
import de.timuuuu.moneymaker.utils.ChatClient;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;
import net.labymod.api.notification.Notification.Type;

@AddonMain
public class MoneyMakerAddon extends LabyAddon<MoneyMakerConfiguration> {

  public static final HudWidgetCategory CATEGORY = new HudWidgetCategory("moneymaker");

  public MoneyMakerMainActivity moneyMakerMainActivity;
  public ChatActivity chatActivity;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.chatActivity = new ChatActivity(this);
    this.moneyMakerMainActivity = new MoneyMakerMainActivity(this);

    this.registerListener(new NetworkPayloadListener(this));
    this.registerListener(new ChatReceiveListener());
    this.registerListener(new DisconnectListener(this));
    this.registerListener(new MoneyAddonListener(this));

    labyAPI().navigationService().register("moneymaker_main_ui", new MoneyMakerNavigationElement(this));

    labyAPI().hudWidgetRegistry().categoryRegistry().register(CATEGORY);
    labyAPI().hudWidgetRegistry().register(new BoosterCountWidget(this));

    this.logger().info("Enabled the Addon");

    new ChatClient().connect();
  }

  @Override
  protected Class<MoneyMakerConfiguration> configurationClass() {
    return MoneyMakerConfiguration.class;
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

  public void pushNotification(Component title, Component text, Component buttonText, Runnable buttonAction) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png")))
        .addButton(NotificationButton.of(buttonText, buttonAction))
        .type(Type.ADVANCEMENT);
    labyAPI().notificationController().push(builder.build());
  }

}
