package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.client.render.matrix.Stack;

@AutoActivity
@Link("secret.lss")
public class SecretActivity extends Activity {

  MoneyMakerAddon addon;
  
  public SecretActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.secret.title").addId("secret-title");
    this.document.addChild(titleWidget);

    Util.addFeedbackButton(this.document);

    DivWidget container = new DivWidget();
    container.addId("secret-container");
    this.document.addChild(container);

    //ShowJoins
    ComponentWidget toggleJoinMessageTitle = ComponentWidget.i18n("moneymaker.ui.secret.toggleJoinMessageTitle").addId("showJoinsTitle");
    this.document.addChild(toggleJoinMessageTitle);

    SwitchWidget showJoinsSwitch = SwitchWidget.create(value -> {
      AddonSettings.showJoins = value;
    }).addId("showJoinsSwitch");
    showJoinsSwitch.setValue(AddonSettings.showJoins);
    this.document.addChild(showJoinsSwitch);

    //ExportOnShutdown
    ComponentWidget exportOnShutdownTitle = ComponentWidget.i18n("moneymaker.ui.secret.exportOnShutdownTitle").addId("exportOnShutdownTitle");
    this.document.addChild(exportOnShutdownTitle);

    SwitchWidget exportOnShutdownSwitch = SwitchWidget.create(value -> {
      this.addon.configuration().exportOnShutdown().set(value);
    }).addId("exportOnShutdownSwitch");
    exportOnShutdownSwitch.setValue(this.addon.configuration().exportOnShutdown().get());
    this.document.addChild(exportOnShutdownSwitch);

    //ExportOnShutdown
    ComponentWidget chatReconnectTitle = ComponentWidget.i18n("moneymaker.ui.secret.chat-reconnect").addId("chat-reconnect-title");
    this.document.addChild(chatReconnectTitle);

    SwitchWidget chatReconnectSwitch = SwitchWidget.create(value -> {
      this.addon.configuration().chatReconnectButton().set(value);
    }).addId("chat-reconnect-switch");
    chatReconnectSwitch.setValue(this.addon.configuration().chatReconnectButton().get());
    this.document.addChild(chatReconnectSwitch);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }
}
