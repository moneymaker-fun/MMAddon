package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.boosters.BoosterUtil;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

public class BoosterWidget extends SimpleWidget {

  private Booster booster;

  public BoosterWidget(Booster booster) {
    this.booster = booster;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    addId("booster");

    ComponentWidget amountWidget = ComponentWidget.text("§6" + this.booster.amount() + " §7x").addId("amount");
    addChild(amountWidget);
    IconWidget iconWidget = new IconWidget(BoosterUtil.getIcon(this.booster)).addId("icon");
    addChild(iconWidget);
    ComponentWidget boostWidget = ComponentWidget.text(this.booster.boost() + "% (" + this.booster.readableTime() + ")", NamedTextColor.YELLOW).addId("boost");
    addChild(boostWidget);
  }
}
