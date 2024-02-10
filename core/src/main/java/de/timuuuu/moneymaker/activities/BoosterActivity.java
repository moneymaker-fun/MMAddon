package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.widgets.BoosterWidget;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.utils.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
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

  private boolean orderAscending = true;

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    this.renderBackground = false;

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.booster.title");
    titleWidget.addId("title");
    this.document.addChild(titleWidget);

    Util.addFeedbackButton(this.document);

    AtomicInteger boost = new AtomicInteger(0);
    Booster.boosterList().forEach(booster -> boost.getAndAdd(booster.boost()));

    DivWidget container = new DivWidget().addId("container");

    LinkedList<Booster> list = new LinkedList<>();

    if (this.orderAscending) {
      for (int j = Booster.boosterList().size() - 1; j >= 0; j--) {
        Booster booster = Booster.boosterList().get(j);
        list.add(booster);
      }
    } else {
      list.addAll(Booster.boosterList());
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
        Component.text(Booster.sessionBoosters.get() > 0 && AddonSettings.sessionBlocks > 0 ? "\n" + (float) Booster.sessionBoosters.get() / AddonSettings.sessionBlocks + " (" + ((float) Booster.sessionBoosters.get() / AddonSettings.sessionBlocks) * 100 +  " %)" : "\nN/A", TextColor.color(255, 170, 0))
    ));
    averageBoostersWidget.setHoverComponent(Component.text(Booster.sessionBoosters.get() + " Booster / " + AddonSettings.sessionBlocks + " Blöcke"));
    averageBoostersWidget.addId("average-boosters");
    sideContainer.addChild(averageBoostersWidget);

    ButtonWidget exportBtnWidget = ButtonWidget.i18n("moneymaker.ui.booster.export").addId("export-button");
    exportBtnWidget.setPressable(() -> writeLinkedListToCSV(false));

    ButtonWidget clearListButton = ButtonWidget.i18n("moneymaker.ui.booster.clear").addId("clear-button");
    clearListButton.setPressable(() -> {
      if(!Booster.boosterList().isEmpty()) {
        Booster.boosterList().clear();
        this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
      }
    });

    ButtonWidget sortButton = ButtonWidget.component(Component.translatable("moneymaker.ui.booster.sorting", TextColor.color(255, 170, 0))
        .append(Component.text(orderAscending ? " §b⬆" : " §b⬇"))).addId("sort-button");
    sortButton.setPressable(() -> {
      orderAscending = !orderAscending;
      this.reload();
    });

    sideContainer.addChild(sortButton);
    sideContainer.addChild(clearListButton);
    sideContainer.addChild(exportBtnWidget);

    this.document.addChild(container);
    this.document.addChild(sideContainer);
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
          Component.translatable("moneymaker.notification.booster-export.open-folder"), () -> {
        OperatingSystem.getPlatform().openFile(folder);
      });
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
