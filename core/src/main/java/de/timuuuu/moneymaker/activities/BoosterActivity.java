package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
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
import net.labymod.api.util.ListOrder;


@AutoActivity
@Link("activity.lss")
public class BoosterActivity extends Activity {

  MoneyMakerAddon addon;
  public BoosterActivity(MoneyMakerAddon addon) {
    this.addon = addon;
    Booster.insertBooster(40, 50);
    Booster.insertBooster(100, 90);
    Booster.insertBooster(80, 60);
    Booster.insertBooster(40, 45);
    Booster.insertBooster(40, 50);
    Booster.insertBooster(80, 10);
    Booster.insertBooster(80, 1440);
  }

  private boolean orderAscending = true;


  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.ui.booster.title.name");
    titleWidget.addId("booster-title");
    this.document.addChild(titleWidget);

    AtomicInteger boost = new AtomicInteger(0);
    Booster.getBoosterguilist().forEach(booster -> boost.getAndAdd(booster.getBoost()));

    ComponentWidget subTitleWidget = ComponentWidget.text("§eGesamt: §6" + boost.get() + "%");
    subTitleWidget.addId("booster-subTitle");
    this.document.addChild(subTitleWidget);

    ComponentWidget author = ComponentWidget.text("§eAddon by Timuuuu");
    author.addId("booster-author");
    this.document.addChild(author);

    author.setPressable(() -> {
      AddonSettings.id.decrementAndGet();
      if (AddonSettings.id.get() == 0) {
        System.out.println("Freigeschaltet");
        MoneyMakerAddon.getInstance().moneymakerMainAcivity.registerSecret();
      }
    });

    VerticalListWidget<ComponentWidget> listWidget = new VerticalListWidget<>();
    listWidget.addId("booster-list");
    listWidget.listOrder().set(ListOrder.NORMAL);

    ButtonWidget sortButton = ButtonWidget.text("§6Sortierung " + (orderAscending ? "§b⬆" : "§b⬇"));
    sortButton.setPressable(() -> {
      orderAscending = !orderAscending;
      this.reload();
    });
    sortButton.addId("booster-sort-button");
    this.document.addChild(sortButton);

    DivWidget container = new DivWidget();
    container.addId("booster-container");

    if (this.orderAscending) {
      for (int j = Booster.getBoosterguilist().size() - 1; j >= 0; j--) {

        Booster booster = Booster.getBoosterguilist().get(j);
        String boosterMessage = "§6" + booster.getAmnt() + " §7✗ §e" + booster.getBoost() + "%";

        String boosterTime;
        int tempTime = booster.getTime();
        if (tempTime == 60 || tempTime == 90 || tempTime == 120 || tempTime == 180
            || tempTime == 360 || tempTime == 480 || tempTime == 720) {
          if (tempTime == 60) {
            boosterTime = "1 Stunde";
          } else if (tempTime == 90) {
            boosterTime = "90 Minuten";
          } else {
            boosterTime = (booster.getTime() / 60) + " Stunden";
          }
        } else {
          boosterTime = booster.getTime() + " Minuten";
        }

        ComponentWidget widget = ComponentWidget.text(boosterMessage + " §8┃ §7" + boosterTime);
        widget.addId("booster-entry");
        listWidget.addChild(widget);
      }
    } else {
      for (int j = 0; j < Booster.getBoosterguilist().size(); j++) {

        Booster booster = Booster.getBoosterguilist().get(j);
        String boosterMessage = "§6" + booster.getAmnt() + " §7✗ §e" + booster.getBoost() + "%";

        String boosterTime;
        int tempTime = booster.getTime();
        if (tempTime == 60 || tempTime == 90 || tempTime == 120 || tempTime == 180
            || tempTime == 360 || tempTime == 480 || tempTime == 720) {
          if (tempTime == 60) {
            boosterTime = "1 Stunde";
          } else if (tempTime == 90) {
            boosterTime = "90 Minuten";
          } else {
            boosterTime = (booster.getTime() / 60) + " Stunden";
          }
        } else {
          boosterTime = booster.getTime() + " Minuten";
        }

        ComponentWidget widget = ComponentWidget.text(boosterMessage + " §8┃ §7" + boosterTime);
        widget.addId("booster-entry");
        listWidget.addChild(widget);
      }
    }

    ScrollWidget scrollWidget = new ScrollWidget(listWidget, new ListSession<>());
    container.addChild(scrollWidget);

    ButtonWidget exportBtnWidget = ButtonWidget.text("Export als CSV");
    exportBtnWidget.alignmentX().set(WidgetAlignment.CENTER);
    exportBtnWidget.addId("exportBtn");
    exportBtnWidget.setPressable(this::writeLinkedListToCSV);

    container.addChild(exportBtnWidget);

    this.document.addChild(container);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
  }

  private void writeLinkedListToCSV() {
    try {
      // Create a new SimpleDateFormat object with the desired format
      String time = new SimpleDateFormat("dd_MM_yy-HH_mm").format(new Date());

      // Öffnen Sie die CSV-Datei in einem Schreibmodus
      File file = new File("BoosterExport_"+time+".csv");
      FileWriter writer = new FileWriter(file);


      // Verwenden Sie einen `for`-Loop, um die Elemente der LinkedList zu durchlaufen
      writer.write("Anzahl;Booster;Zeit\n\n");
      for (Booster entry : Booster.getBoosterguilist()) {
        // Schreiben Sie den Wert des Elements als neue Zeile in die CSV-Datei
        writer.write(entry.toExport() + "\n");
      }
      // Schließen Sie die Datei
      writer.close();
      openGameFolder();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void openGameFolder (){
    String folderPath = this.addon.labyAPI().labyModLoader().getGameDirectory().toFile().getPath();
    // Create a File object for the folder
    File folder = new File(folderPath);
    OperatingSystem.getPlatform().openFile(folder);
  }
}
