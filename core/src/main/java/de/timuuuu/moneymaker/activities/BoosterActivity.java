package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.BoosterWidget;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.utils.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.Laby;
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
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.models.OperatingSystem;

@AutoActivity
@Links({@Link("booster.lss"), @Link("buttons.lss")})
public class BoosterActivity extends SimpleActivity {

  MoneyMakerAddon addon;
  public BoosterActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private Sorting sorting = Sorting.DESCENDING;

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.booster.title");
    titleWidget.addId("title");
    this.document.addChild(titleWidget);

    AtomicInteger boost = new AtomicInteger(0);
    Booster.boosterList().forEach(booster -> boost.getAndAdd(booster.boost()));

    DivWidget container = new DivWidget().addId("container");

    LinkedList<Booster> list = new LinkedList<>(Booster.boosterList());
    if(this.sorting == Sorting.ASCENDING) {
      list.sort(Comparator.comparing(Booster::boost));
    }
    if(this.sorting == Sorting.TIME) {
      list.sort(Comparator.comparing(Booster::farmDate));
    }

    TilesGridWidget<BoosterWidget> boosters = new TilesGridWidget<>().addId("booster-grid");
    list.forEach(booster -> boosters.addTile(new BoosterWidget(booster)));

    container.addChild(new ScrollWidget(boosters, new ListSession<>()));

    DivWidget sideContainer = new DivWidget().addId("side-container");

    ComponentWidget totalBoostWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.booster.boost-total", TextColor.color(255, 255, 85),
        Component.text(boost.get() + "%", TextColor.color(255, 170, 0))));
    totalBoostWidget.addId("total-boost");
    sideContainer.addChild(totalBoostWidget);

    ComponentWidget averageBoostersWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.booster.average-boosters", TextColor.color(255, 255, 85)).append(
        Component.text(Booster.sessionBoosters.get() > 0 && this.addon.addonUtil().sessionBlocks() > 0 ? "\n" + (float) Booster.sessionBoosters.get() / this.addon.addonUtil().sessionBlocks() + " (" + ((float) Booster.sessionBoosters.get() / this.addon.addonUtil().sessionBlocks()) * 100 +  " %)" : "\nN/A", TextColor.color(255, 170, 0))
    )).addId("average-boosters");
    sideContainer.addChild(averageBoostersWidget);

    ComponentWidget boostersPerBlocksWidget = ComponentWidget.component(
        Component.text(Booster.sessionBoosters.get() + " Boosters", NamedTextColor.GOLD)
            .append(Component.text(" / ", NamedTextColor.GRAY))
            .append(Component.text(this.addon.addonUtil().sessionBlocks() + " ", NamedTextColor.YELLOW))
            .append(Component.translatable("moneymaker.hudWidget.mm_block_session.blocks", NamedTextColor.YELLOW))
    ).addId("boosters-per-block");
    sideContainer.addChild(boostersPerBlocksWidget);

    ButtonWidget exportBtnWidget = ButtonWidget.i18n("moneymaker.ui.booster.export").addId("export-button");
    exportBtnWidget.setPressable(() -> writeLinkedListToCSV(false));

    ButtonWidget clearListButton = ButtonWidget.i18n("moneymaker.ui.booster.clear").addId("clear-button");
    clearListButton.setPressable(() -> {
      if(!Booster.boosterList().isEmpty()) {
        Booster.boosterList().clear();
        this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
      }
    });

    ButtonWidget sortButton = ButtonWidget.component(Component.translatable("moneymaker.ui.booster.sorting.button", NamedTextColor.GOLD)
        .append(Component.text(" (", NamedTextColor.DARK_GRAY).append(this.sortIcon()).append(Component.text(")", NamedTextColor.DARK_GRAY)))).addId("sort-button");
    sortButton.setPressable(() -> {
      switch (this.sorting) {
        case DESCENDING -> this.sorting = Sorting.ASCENDING;
        case ASCENDING -> this.sorting = Sorting.TIME;
        case TIME -> this.sorting = Sorting.DESCENDING;
      }
      this.reload();
    });

    sideContainer.addChild(sortButton);
    sideContainer.addChild(clearListButton);
    sideContainer.addChild(exportBtnWidget);

    this.document.addChild(container);
    this.document.addChild(sideContainer);

    this.document.addChild(Util.feedbackButton());
    this.document.addChild(Util.discordButton());
    this.document.addChild(Util.leaderboardButton());
  }

  private Component sortIcon() {
    if(this.sorting == Sorting.ASCENDING) {
      return Component.text("⬆", NamedTextColor.AQUA);
    }
    if(this.sorting == Sorting.TIME) {
      return Component.text("⌚", NamedTextColor.AQUA);
    }
    return Component.text("⬇", NamedTextColor.AQUA);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawAuthor(this.labyAPI, this.bounds(), stack);
  }

  public static void writeLinkedListToCSV(boolean quit) {
    if(Booster.boosterList().isEmpty()) {
      if(!quit) {
        MoneyMakerAddon.instance().pushNotification(
            Component.translatable("moneymaker.notification.booster-export.title"),
            Component.translatable("moneymaker.notification.booster-export.no-boosters", TextColor.color(255, 85, 85))
        );
      }
      return;
    }
    try {

      File folder = new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile(), "MoneyMaker");
      if(!folder.exists()) {
        folder.mkdir();
      }

      String time = new SimpleDateFormat("dd_MM_yy-HH_mm").format(new Date());
      File file = new File(folder, "BoosterExport_"+time+".csv");
      FileWriter writer = new FileWriter(file);

      writer.write("Anzahl;Booster;Zeit\n\n");
      for (Booster entry : Booster.boosterList()) {
        writer.write(entry.toExport() + "\n");
      }
      writer.close();
      MoneyMakerAddon.pushNotification(Component.translatable("moneymaker.notification.booster-export.title"),
          Component.translatable("moneymaker.notification.booster-export.saved", TextColor.color(85, 255, 85)),
          Component.translatable("moneymaker.notification.booster-export.open-folder"), () -> OperatingSystem.getPlatform().openFile(folder));
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public enum Sorting {
    DESCENDING, ASCENDING, TIME
  }

}
