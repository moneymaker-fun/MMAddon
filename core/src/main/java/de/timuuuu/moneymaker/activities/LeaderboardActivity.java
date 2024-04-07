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
import net.labymod.api.client.render.matrix.Stack;

@AutoActivity
@Links({@Link("leaderboard.lss"), @Link("buttons.lss")})
public class LeaderboardActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  public LeaderboardActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.leaderboard.title").addId("title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget();
    container.addId("container");

    this.document.addChild(container);

    this.document.addChild(Util.feedbackButton());
    this.document.addChild(Util.discordButton());
    this.document.addChild(Util.leaderboardButton());
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }
}
