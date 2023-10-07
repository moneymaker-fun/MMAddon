package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
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
public class Secret extends Activity {

  MoneyMakerAddon addon;
  
  public Secret(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.text("Super secret settings");
    titleWidget.addId("booster-title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("secret-container");
    this.document.addChild(container);
    //ShowJoins
    ComponentWidget toggleJoinMessageTitle = ComponentWidget.i18n("moneymaker.ui.secret.toggleJoinMessageTitle");
    toggleJoinMessageTitle.addId("showJoinsTitle");
    this.document.addChild(toggleJoinMessageTitle);
    SwitchWidget showJoinsSwitch = SwitchWidget.create(value -> {
      AddonSettings.showJoins = value;
    }).addId("showJoinsSwitch");
    showJoinsSwitch.setValue(AddonSettings.showJoins);
    this.document.addChild(showJoinsSwitch);
    //ExportOnShutdown
    ComponentWidget exportOnShutdownTitle = ComponentWidget.i18n("moneymaker.ui.secret.exportOnShutdownTitle");
    exportOnShutdownTitle.addId("exportOnShutdownTitle");
    this.document.addChild(exportOnShutdownTitle);
    SwitchWidget exportOnShutdownSwitch = SwitchWidget.create(value -> {
      this.addon.configuration().getExportOnShutdown().set(value);
    }).addId("exportOnShutdownSwitch");
    exportOnShutdownSwitch.setValue(this.addon.configuration().getExportOnShutdown().get());
    this.document.addChild(exportOnShutdownSwitch);
  }
//this.addon.configuration().getExportOnShutdown().get()
  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
  }

}
