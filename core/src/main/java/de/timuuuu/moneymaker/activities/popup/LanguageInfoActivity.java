package de.timuuuu.moneymaker.activities.popup;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;

@Link("popup/language.lss")
@AutoActivity
public class LanguageInfoActivity extends SimpleActivity {

  private MoneyMakerAddon addon;

  private ScreenInstance previousScreen;

  public LanguageInfoActivity(MoneyMakerAddon addon, ScreenInstance previousScreen) {
    this.addon = addon;
    this.previousScreen = previousScreen;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    ComponentWidget titleWidget = ComponentWidget.i18n("moneymaker.language-info.title").addId("title");
    header.addEntry(titleWidget);

    FlexibleContentWidget descriptionContainer = new FlexibleContentWidget().addId("description-container");
    ComponentWidget line1 = ComponentWidget.i18n("moneymaker.language-info.line1", NamedTextColor.RED);
    ComponentWidget line2 = ComponentWidget.i18n("moneymaker.language-info.line2", NamedTextColor.GRAY);
    descriptionContainer.addContent(line1);
    descriptionContainer.addContent(line2);

    ButtonWidget closeButton = ButtonWidget.i18n("moneymaker.mute.form.close")
        .addId("close-button");
    closeButton.setPressable(() -> {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
      this.addon.configuration().languageInfoClosed().set(true);
    });

    FlexibleContentWidget buttonContainer = new FlexibleContentWidget().addId("button-container");
    buttonContainer.addContent(closeButton);

    FlexibleContentWidget content = new FlexibleContentWidget().addId("content");

    content.addContent(descriptionContainer);
    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(content);

    this.document.addChild(container);
  }
}
