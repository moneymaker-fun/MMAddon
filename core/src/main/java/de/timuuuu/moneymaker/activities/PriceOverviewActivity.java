package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import java.util.HashMap;
import java.util.List;

@AutoActivity
@Links({@Link("price-overview.lss"), @Link("buttons.lss")})
public class PriceOverviewActivity extends SimpleActivity {

  MoneyMakerAddon addon;

  public PriceOverviewActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private HashMap<String, MineData> priceData = new HashMap<>();

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.priceOverview.title").addId("title");
    this.document.addChild(titleWidget);

    DivWidget container = new DivWidget().addId("container");

    DivWidget goldContainer = new DivWidget().addId("gold-container");
    goldContainer.addChild(ComponentWidget.i18n("moneymaker.ui.priceOverview.gold").addId("gold-title"));

    if(priceData.containsKey("Goldmine")) {
      MineData goldData = priceData.get("Goldmine");
      goldContainer.addChild(ComponentWidget.component(Component.translatable("moneymaker.ui.priceOverview.unlockCosts").append(Component.text(goldData.costs()))).addId("gold-unlock"));
      VerticalListWidget<ComponentWidget> list = new VerticalListWidget<>().addId("gold-list");
      goldData.workers().forEach(entry -> {
        String name = entry.split(";")[0];
        String cost = entry.split(";")[1];
        TextColor textColor = NamedTextColor.GRAY;
        if(entry.contains("Geröll")) {
          textColor = NamedTextColor.YELLOW;
        }
        list.addChild(ComponentWidget.text(name + " ➡ " + cost, textColor));
      });
      goldContainer.addChild(new ScrollWidget(list, new ListSession<>()).addId("gold-scroll"));
    }

    container.addChild(goldContainer);

    DivWidget coalContainer = new DivWidget().addId("coal-container");
    coalContainer.addChild(ComponentWidget.i18n("moneymaker.ui.priceOverview.coal").addId("coal-title"));

    if(priceData.containsKey("Kohlemine")) {
      MineData coalData = priceData.get("Kohlemine");
      coalContainer.addChild(ComponentWidget.component(Component.translatable("moneymaker.ui.priceOverview.unlockCosts").append(Component.text(coalData.costs()))).addId("coal-unlock"));
      VerticalListWidget<ComponentWidget> list = new VerticalListWidget<>().addId("coal-list");
      coalData.workers().forEach(entry -> {
        String name = entry.split(";")[0];
        String cost = entry.split(";")[1];
        TextColor textColor = NamedTextColor.GRAY;
        if(entry.contains("Geröll")) {
          textColor = NamedTextColor.YELLOW;
        }
        list.addChild(ComponentWidget.text(name + " ➡ " + cost, textColor));
      });
      coalContainer.addChild(new ScrollWidget(list, new ListSession<>()).addId("coal-scroll"));
    }

    container.addChild(coalContainer);

    DivWidget ironContainer = new DivWidget().addId("iron-container");
    ironContainer.addChild(ComponentWidget.i18n("moneymaker.ui.priceOverview.iron").addId("iron-title"));

    if(priceData.containsKey("Eisenmine")) {
      MineData ironData = priceData.get("Eisenmine");
      ironContainer.addChild(ComponentWidget.component(Component.translatable("moneymaker.ui.priceOverview.unlockCosts").append(Component.text(ironData.costs()))).addId("iron-unlock"));
      VerticalListWidget<ComponentWidget> list = new VerticalListWidget<>().addId("iron-list");
      ironData.workers().forEach(entry -> {
        String name = entry.split(";")[0];
        String cost = entry.split(";")[1];
        TextColor textColor = NamedTextColor.GRAY;
        if(entry.contains("Geröll")) {
          textColor = NamedTextColor.YELLOW;
        }
        list.addChild(ComponentWidget.text(name + " ➡ " + cost, textColor));
      });
      ironContainer.addChild(new ScrollWidget(list, new ListSession<>()).addId("iron-scroll"));
    }

    container.addChild(ironContainer);

    DivWidget lapisContainer = new DivWidget().addId("lapis-container");
    lapisContainer.addChild(ComponentWidget.i18n("moneymaker.ui.priceOverview.lapis").addId("lapis-title"));

    if(priceData.containsKey("Lapismine")) {
      MineData lapisData = priceData.get("Lapismine");
      lapisContainer.addChild(ComponentWidget.component(Component.translatable("moneymaker.ui.priceOverview.unlockCosts").append(Component.text(lapisData.costs()))).addId("lapis-unlock"));
      VerticalListWidget<ComponentWidget> list = new VerticalListWidget<>().addId("lapis-list");
      lapisData.workers().forEach(entry -> {
        String name = entry.split(";")[0];
        String cost = entry.split(";")[1];
        TextColor textColor = NamedTextColor.GRAY;
        if(entry.contains("Geröll")) {
          textColor = NamedTextColor.YELLOW;
        }
        list.addChild(ComponentWidget.text(name + " ➡ " + cost, textColor));
      });
      lapisContainer.addChild(new ScrollWidget(list, new ListSession<>()).addId("lapis-scroll"));
    }

    container.addChild(lapisContainer);


    this.document.addChild(container);

    this.document.addChild(Util.feedbackButton());
    this.document.addChild(Util.leaderboardButton());
    this.document.addChild(Util.discordButton());

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }

  public HashMap<String, MineData> priceData() {
    return priceData;
  }

  public static class MineData {

    private String costs;
    private List<String> workers;

    public MineData(String costs, List<String> workers) {
      this.costs = costs;
      this.workers = workers;
    }

    public String costs() {
      return costs;
    }

    public List<String> workers() {
      return workers;
    }
  }

}
