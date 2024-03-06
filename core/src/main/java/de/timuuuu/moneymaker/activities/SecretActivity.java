package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.render.matrix.Stack;

@AutoActivity
@Links({@Link("secret.lss"), @Link("buttons.lss")})
public class SecretActivity extends SimpleActivity {

  MoneyMakerAddon addon;
  
  public SecretActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    this.renderBackground = false;

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.secret.title").addId("secret-title");
    this.document.addChild(titleWidget);

    Util.addFeedbackButton(this.document);

    DivWidget container = new DivWidget();
    container.addId("secret-container");
    this.document.addChild(container);

    // Show Chat Reconnect Button
    ComponentWidget chatReconnectTitle = ComponentWidget.i18n("moneymaker.ui.secret.chat.reconnect").addId("chat-reconnect-title");
    this.document.addChild(chatReconnectTitle);

    SwitchWidget chatReconnectSwitch = SwitchWidget.create(value -> {
      this.addon.configuration().chatReconnectButton().set(value);
    }).addId("chat-reconnect-switch");
    chatReconnectSwitch.setValue(this.addon.configuration().chatReconnectButton().get());
    this.document.addChild(chatReconnectSwitch);

    if(Util.isDev(this.addon.labyAPI().getUniqueId().toString())) {
      ComponentWidget chatShowAllPlayersTitle = ComponentWidget.i18n("moneymaker.ui.secret.chat.all-players").addId("chat-all-players-title");
      this.document.addChild(chatShowAllPlayersTitle);
      SwitchWidget chatShowAllPlayersSwitch = SwitchWidget.create(value -> {
        this.addon.configuration().chatShowAllPlayers().set(value);
      }).addId("chat-all-players-switch");
      chatShowAllPlayersSwitch.setValue(this.addon.configuration().chatShowAllPlayers().get());
      this.document.addChild(chatShowAllPlayersSwitch);
    }

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }
}
