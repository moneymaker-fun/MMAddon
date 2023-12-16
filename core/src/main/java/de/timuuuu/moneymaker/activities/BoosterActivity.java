package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import de.timuuuu.moneymaker.utils.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.Constants.Resources;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.attributes.WidgetAlignment;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.models.OperatingSystem;

@AutoActivity
@Link("booster.lss")
public class BoosterActivity extends Activity {

  MoneyMakerAddon addon;
  public BoosterActivity(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private boolean orderAscending = true;

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.booster.title");
    titleWidget.addId("booster-title");
    this.document.addChild(titleWidget);

    Util.addFeedbackButton(this.document);

    AtomicInteger boost = new AtomicInteger(0);
    Booster.boosterList().forEach(booster -> boost.getAndAdd(booster.boost()));

    ComponentWidget totalBoostWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.booster.boost-total", TextColor.color(255, 255, 85),
        Component.text(boost.get() + "%", TextColor.color(255, 170, 0))));
    totalBoostWidget.addId("booster-totalBoost");
    this.document.addChild(totalBoostWidget);

    ComponentWidget averageBoostersWidget = ComponentWidget.component(Component.translatable("moneymaker.ui.booster.average-boosters", TextColor.color(255, 255, 85)).append(
        Component.text(Booster.sessionBoosters.get() > 0 && AddonSettings.sessionBlocks > 0 ? (float) Booster.sessionBoosters.get() / AddonSettings.sessionBlocks + "(" + ((float) Booster.sessionBoosters.get() / AddonSettings.sessionBlocks) * 100 +  " %)" : "N/A", TextColor.color(255, 170, 0))
    ));
    averageBoostersWidget.setHoverComponent(Component.text(Booster.sessionBoosters.get() + " Booster / " + AddonSettings.sessionBlocks + " Blöcke"));
    averageBoostersWidget.addId("booster-averageBoosters");
    this.document.addChild(averageBoostersWidget);

    VerticalListWidget<ComponentWidget> listWidget = new VerticalListWidget<>().addId("booster-list");

    ButtonWidget sortButton = ButtonWidget.component(Component.translatable("moneymaker.ui.booster.sorting", TextColor.color(255, 170, 0))
        .append(Component.text(orderAscending ? " §b⬆" : " §b⬇")));
    sortButton.setPressable(() -> {
      orderAscending = !orderAscending;
      this.reload();
    });
    sortButton.addId("booster-sort-button");
    this.document.addChild(sortButton);

    DivWidget container = new DivWidget().addId("booster-container");

    LinkedList<Booster> list = new LinkedList<>();

    if (this.orderAscending) {
      for (int j = Booster.boosterList().size() - 1; j >= 0; j--) {
        Booster booster = Booster.boosterList().get(j);
        list.add(booster);
      }
    } else {
      list.addAll(Booster.boosterList());
    }

    list.forEach(booster -> {
      String boosterMessage = "§6" + booster.amount() + " §7✗ §e" + booster.boost() + "%";
      listWidget.addChild(ComponentWidget.text(boosterMessage + " §8┃ §7" + booster.readableTime()).addId("booster-entry"));
    });

    container.addChild(new ScrollWidget(listWidget, new ListSession<>()));

    ButtonWidget exportBtnWidget = ButtonWidget.i18n("moneymaker.ui.booster.export").addId("exportBtn");
    exportBtnWidget.alignmentX().set(WidgetAlignment.CENTER);
    exportBtnWidget.setPressable(() -> writeLinkedListToCSV(false));

    container.addChild(exportBtnWidget);

    this.document.addChild(container);
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
      String time = new SimpleDateFormat("dd_MM_yy-HH_mm").format(new Date());
      File file = new File("BoosterExport_"+time+".csv");
      FileWriter writer = new FileWriter(file);

      writer.write("Anzahl;Booster;Zeit\n\n");
      for (Booster entry : Booster.boosterList()) {
        writer.write(entry.toExport() + "\n");
      }
      writer.close();
      MoneyMakerAddon.pushNotification(Component.translatable("moneymaker.notification.booster-export.title"),
          Component.translatable("moneymaker.notification.booster-export.saved", TextColor.color(85, 255, 85)),
          Component.translatable("moneymaker.notification.booster-export.open-folder"), () -> {
        OperatingSystem.getPlatform().openFile(new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile().getPath()));
      });
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
