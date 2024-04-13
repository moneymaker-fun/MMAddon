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

    /*
    Mine >> Kohlenmine
[12:49:26] [Render thread/INFO]: MineData >> Costs: 1 ai | Workers: {Arbeiter 21=54.00 ap, Arbeiter 20=2.70 ap, Arbeiter 25=8.64 ar, Arbeiter 24=432.00 aq, Arbeiter 23=21.60 aq, Arbeiter 22=1.08 aq, Arbeiter 29=2.02 at, Arbeiter 28=92.00 as, Arbeiter 27=4.18 as, Arbeiter 26=190.00 ar, Arbeiter 2=1.47 ai, Geröll 1 (1,5h)=7.60 aj, Arbeiter 3=17.60 ai, Arbeiter 1=0, Arbeiter 10=1.36 al, Arbeiter 8=6.95 ak, Arbeiter 30=44.50 at, Arbeiter 9=97.30 ak, Geröll 3 (6h)=7.15 an, Arbeiter 6=35.50 aj, Arbeiter 14=89.30 am, Arbeiter 7=497.00 aj, Arbeiter 13=5.58 am, Arbeiter 4=211.00 ai, Arbeiter 12=349 al, Arbeiter 5=2.53 aj, Arbeiter 11=21.8 al, Arbeiter 18=8.33 ao, Arbeiter 17=463.00 an, Arbeiter 16=25.70 an, Arbeiter 15=1.43 an, Arbeiter 19=150.00 ao, Geröll 5 (1d)=43.20 ar, Geröll 2 (3h)=5.45 al, Geröll 4 (12h)=13.50 ap}
[12:49:26] [Render thread/INFO]: Mine >> Goldmine
[12:49:26] [Render thread/INFO]: MineData >> Costs: 0 | Workers: {Arbeiter 21=28.14 ad, Arbeiter 20=1.66 ad, Arbeiter 25=2.35 af, Arbeiter 24=138.24 ae, Arbeiter 23=8.13 ae, Arbeiter 22=478.34 ad, Arbeiter 29=306.26 ag, Arbeiter 28=16.12 ag, Arbeiter 27=848.37 af, Arbeiter 26=44.65 af, Arbeiter 2=50.00 K, Arbeiter 3=450.00 K, Geröll 4 (4h)=8.28 ad, Arbeiter 1=0, Arbeiter 10=5.87 T, Arbeiter 8=48.51 B, Arbeiter 30=5.82 ah, Arbeiter 9=533.66 B, Arbeiter 6=400.95 M, Arbeiter 14=167.66 aa, Arbeiter 7=4.41 B, Arbeiter 13=12.90 aa, Arbeiter 4=4.05 M, Arbeiter 12=992.08 T, Arbeiter 5=36.35 M, Arbeiter 11=76.31 T, Arbeiter 18=7.36 ac, Arbeiter 17=490.41 ab, Arbeiter 16=32.69 ab, Arbeiter 15=2.18 ab, Geröll 1 (30min)=109.35 M, Geröll 3 (2h)=10.90 ab, Arbeiter 19=110.34 ac, Geröll 2 (1h)=23.48 T, Geröll 5 (8h)=11.75 af}
[12:49:26] [Render thread/INFO]: Mine >> Lapismine
[12:49:26] [Render thread/INFO]: MineData >> Costs: 1 bg | Workers: {Arbeiter 21=980.53 bn, Arbeiter 20=61.93 bn, Arbeiter 25=61.62 bp, Arbeiter 24=3.89 bp, Arbeiter 23=245.81 bo, Arbeiter 22=15.53 bo, Arbeiter 29=3.87 br, Arbeiter 28=244.61 bq, Arbeiter 27=15.45 bq, Arbeiter 26=975.72 bp, Arbeiter 2=15.83 bg, Arbeiter 3=250.70 bg, Arbeiter 1=0, Arbeiter 10=62.54 bj, Arbeiter 8=249.47 bi, Arbeiter 30=61.32 br, Arbeiter 9=3.95 bj, Geröll 4 (20h)=309.64 bn, Arbeiter 6=995.10 bh, Arbeiter 14=3.93 bl, Arbeiter 7=15.76 bi, Arbeiter 13=248.24 bk, Arbeiter 4=3.97 bh, Arbeiter 12=15.68 bk, Arbeiter 5=62.85 bh, Geröll 1 (2,5h)=314.24 bh, Arbeiter 11=990.22 bj, Arbeiter 18=247.03 bm, Arbeiter 17=15.60 bm, Geröll 3 (10h)=311.17 bl, Arbeiter 16=985.36 bl, Arbeiter 15=62.23 bl, Arbeiter 19=3.91 bn, Geröll 5 (40h)=308.12 bp, Geröll 2 (5h)=312.70 bj}
[12:49:26] [Render thread/INFO]: Mine >> Eisenmine
[12:49:26] [Render thread/INFO]: MineData >> Costs: 1 au | Workers: {Arbeiter 21=180.00 bb, Arbeiter 20=9.47 bb, Arbeiter 25=25.50 bd, Arbeiter 24=1.23 bd, Arbeiter 23=65.00 bc, Arbeiter 22=3.42 bc, Arbeiter 29=5.04 bf, Arbeiter 28=240.00 be, Arbeiter 27=11.40 be, Arbeiter 26=544.00 bd, Geröll 1 (2h)=22.00 av, Arbeiter 2=3.34 au, Arbeiter 3=43.40 au, Geröll 5 (32h)=130.00 bd, Arbeiter 1=0, Arbeiter 10=4.83 ax, Arbeiter 8=25.20 aw, Arbeiter 30=106.00 bf, Arbeiter 9=322.00 aw, Arbeiter 6=95.40 av, Arbeiter 14=314.00 ay, Arbeiter 7=1.52 aw, Arbeiter 13=18.50 ay, Geröll 3 (8h)=25.20 az, Arbeiter 4=565.00 au, Arbeiter 12=1.09 ay, Arbeiter 5=7.60 av, Arbeiter 11=72.50 ax, Arbeiter 18=26.20 ba, Arbeiter 17=1.54 ba, Arbeiter 16=90.80 az, Arbeiter 15=5.34 az, Arbeiter 19=498.00 ba, Geröll 4 (16h)=47.40 bb, Geröll 2 (4h)=19.30 ax}
     */


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
