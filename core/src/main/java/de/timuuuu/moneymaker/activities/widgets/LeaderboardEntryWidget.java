package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import java.util.UUID;

public class LeaderboardEntryWidget extends HorizontalListWidget {

  private UUID uuid;
  private String playerName;
  private int ranking;
  private int blocks;
  private int pickaxeRanking;
  private int swordRanking;
  private String lastUpdate;

  public LeaderboardEntryWidget(UUID uuid, String playerName, int ranking,
      int blocks, int pickaxeRanking, int swordRanking, String lastUpdate) {
    this.uuid = uuid;
    this.playerName = playerName;
    this.ranking = ranking;
    this.blocks = blocks;
    this.pickaxeRanking = pickaxeRanking;
    this.swordRanking = swordRanking;
    this.lastUpdate = lastUpdate;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    HorizontalListWidget userWidget = new HorizontalListWidget().addId("user");
    userWidget.addEntry(new IconWidget(Icon.head(this.uuid)).addId("head"));
    Component userName = Component.text(this.playerName);
    userName.clickEvent(ClickEvent.openUrl("https://laby.net/@" + this.playerName));
    userWidget.addEntry(ComponentWidget.component(userName).addId("userName"));

    this.addEntry(userWidget);

    this.addEntry(ComponentWidget.component(Component.text(Util.format(this.ranking))).addId("ranking"));
    this.addEntry(ComponentWidget.component(Component.text(this.blocks != -2 ? Util.format(this.blocks) : "-")).addId("blocks"));
    this.addEntry(ComponentWidget.component(Component.text(Util.format(this.pickaxeRanking))).addId("pickaxe-ranking"));
    this.addEntry(ComponentWidget.component(Component.text(Util.format(this.swordRanking))).addId("sword-ranking"));
  }

  public String playerName() {
    return playerName;
  }

  public int ranking() {
    return ranking;
  }

  public int blocks() {
    return blocks;
  }

  public int pickaxeRanking() {
    return pickaxeRanking;
  }

  public int swordRanking() {
    return swordRanking;
  }

  public String lastUpdate() {
    return lastUpdate;
  }

}
