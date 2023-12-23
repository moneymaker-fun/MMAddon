package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.render.matrix.Stack;

@AutoActivity
@Links({@Link("info.lss"), @Link("buttons.lss")})
public class InfoActivity extends Activity {

  private MoneyMakerAddon addon;

  public InfoActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    Util.addFeedbackButton(this.document);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.info.title").addId("info-title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("info-container");

    ComponentWidget commandsTitle = ComponentWidget.i18n("moneymaker.ui.info.commandsTitle").addId("commands-title");
    container.addChild(commandsTitle);

    ComponentWidget commands = ComponentWidget.i18n("moneymaker.ui.info.commands").addId("commands");
    container.addChild(commands);

    container.addChild(Util.addDiscordButton());

    this.document.addChild(container);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }
}
