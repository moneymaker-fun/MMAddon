package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.util.I18n;

public class OnlineEntryWidget extends FlexibleContentWidget {

  private MoneyMakerAddon addon;

  private MoneyPlayer player;

  public OnlineEntryWidget(MoneyMakerAddon addon, MoneyPlayer player) {
    this.addon = addon;
    this.player = player;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    HorizontalListWidget entry = new HorizontalListWidget().addId("head");

    entry.addEntry(new IconWidget(Icon.head(this.player.uuid())).addId("avatar"));
    Component nameComponent = Component.text(this.player.rank().getChatPrefix() + this.player.userName());
    nameComponent.clickEvent(ClickEvent.openUrl("https://laby.net/@" + this.player.userName()));
    entry.addEntry(ComponentWidget.component(nameComponent).addId("userName"));
    if(Util.isDev(this.labyAPI.getUniqueId().toString())) {
      entry.addEntry(ComponentWidget.text("§8(§e" + this.player.addonVersion() + "§8)").addId("addonVersion"));
    }

    HorizontalListWidget data = new HorizontalListWidget().addId("data");

    String server = this.player.server();
    if(server.startsWith("Farming")) {
      if(server.split(" - ").length == 2) {
        String cave = server.split(" - ")[1];
        server = "Farming - " + I18n.translate(this.addon.addonUtil().caveByName(cave).translation());
      }
    }

    data.addEntry(ComponentWidget.text("§8➥ §7Server§8: §7" + server).addId("currentServer"));

    this.addContent(entry);
    this.addContent(data);
  }
}
