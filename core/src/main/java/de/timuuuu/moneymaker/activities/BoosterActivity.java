package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.Constants.Resources;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
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

    AtomicInteger boost = new AtomicInteger(0);
    Booster.getBoosterguilist().forEach(booster -> boost.getAndAdd(booster.getBoost()));

    ComponentWidget subTitleWidget = ComponentWidget.text("§eGesamt: §6" + boost.get() + "%");
    subTitleWidget.addId("booster-subTitle");
    this.document.addChild(subTitleWidget);

    ComponentWidget author = ComponentWidget.text("§eAddon by Timuuuu");
    author.addId("booster-author");
    this.document.addChild(author);

    VerticalListWidget<ComponentWidget> listWidget = new VerticalListWidget<>();
    listWidget.addId("booster-list");

    ButtonWidget sortButton = ButtonWidget.text("§6Sortierung " + (orderAscending ? "§b⬆" : "§b⬇"));
    sortButton.setPressable(() -> {
      orderAscending = !orderAscending;
      this.reload();
    });
    sortButton.addId("booster-sort-button");
    this.document.addChild(sortButton);

    DivWidget container = new DivWidget();
    container.addId("booster-container");

    LinkedList<Booster> list = new LinkedList<>();

    if (this.orderAscending) {
      for (int j = Booster.getBoosterguilist().size() - 1; j >= 0; j--) {
        Booster booster = Booster.getBoosterguilist().get(j);
        list.add(booster);
      }
    } else {
      list.addAll(Booster.getBoosterguilist());
    }

    list.forEach(booster -> {
      String boosterMessage = "§6" + booster.getAmnt() + " §7✗ §e" + booster.getBoost() + "%";

      String boosterTime;
      int tempTime = booster.getTime();
      if (tempTime > 59) {
        if (tempTime == 90) {
          boosterTime = "90 Minuten";
        } else {
          boosterTime = (booster.getTime() / 60) + " Stunde/n";
        }
      } else {
        boosterTime = booster.getTime() + " Minuten";
      }

      ComponentWidget widget = ComponentWidget.text(boosterMessage + " §8┃ §7" + boosterTime);
      widget.addId("booster-entry");
      listWidget.addChild(widget);
    });

    ScrollWidget scrollWidget = new ScrollWidget(listWidget, new ListSession<>());
    container.addChild(scrollWidget);

    ButtonWidget exportBtnWidget = ButtonWidget.text("Export als CSV");
    exportBtnWidget.alignmentX().set(WidgetAlignment.CENTER);
    exportBtnWidget.addId("exportBtn");
    exportBtnWidget.setPressable(BoosterActivity::writeLinkedListToCSV);

    container.addChild(exportBtnWidget);

    //Toggle secret
    ButtonWidget secretButton = ButtonWidget.text("");
    secretButton.setActionListener(() -> {
      AddonSettings.id.decrementAndGet();
      if (AddonSettings.id.get() == 0) {
        System.out.println("Freigeschaltet");
        this.addon.moneyMakerMainActivity.registerSecret();
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);
      }
    });
    secretButton.addId("secret-button");
    this.document.addChild(secretButton);
    //Feedback Button
    ButtonWidget feedbackButton = ButtonWidget.text("§6Feedback §7/ §cBugreport");
    feedbackButton.setPressable(() -> {
      OperatingSystem.getPlatform().openUrl("https://forms.gle/rWteNnvwqC5Q9Pz76");
    });
    feedbackButton.addId("feedback-button");
    this.document.addChild(feedbackButton);

    this.document.addChild(container);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
  }

  public static void writeLinkedListToCSV() {
    try {
      String time = new SimpleDateFormat("dd_MM_yy-HH_mm").format(new Date());
      File file = new File("BoosterExport_"+time+".csv");
      FileWriter writer = new FileWriter(file);

      writer.write("Anzahl;Booster;Zeit\n\n");
      for (Booster entry : Booster.getBoosterguilist()) {
        writer.write(entry.toExport() + "\n");
      }
      writer.close();
      MoneyMakerAddon.pushNotification(Component.text("Booster-Export"), Component.text("Die Übersicht der gefarmten Booster wurde gespeichert"), Component.text("Ordner öffnen"), () -> {
        OperatingSystem.getPlatform().openFile(new File(Laby.labyAPI().labyModLoader().getGameDirectory().toFile().getPath()));
      });
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
