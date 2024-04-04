package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.render.matrix.Stack;

@AutoActivity
@Links({@Link("info.lss"), @Link("buttons.lss")})
public class InfoActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  public InfoActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.info.title").addId("info-title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("info-container");

    ComponentWidget commandsTitle = ComponentWidget.i18n("moneymaker.ui.info.commandsTitle").addId("commands-title");
    container.addChild(commandsTitle);

    Component commands = Component.translatable("moneymaker.ui.info.commands.timer.command", NamedTextColor.AQUA)
        .append(Component.translatable("moneymaker.ui.info.commands.timer.description", NamedTextColor.GOLD)).append(Component.text("\n"))
        .append(Component.translatable("moneymaker.ui.info.commands.reset.command", NamedTextColor.AQUA))
        .append(Component.translatable("moneymaker.ui.info.commands.reset.description", NamedTextColor.GOLD));

    ComponentWidget commandsWidget = ComponentWidget.component(commands).addId("commands");
    container.addChild(commandsWidget);

    container.addChild(Util.feedbackButton());
    container.addChild(Util.discordButton());
    container.addChild(Util.leaderboardButton());

    this.document.addChild(container);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }
}
