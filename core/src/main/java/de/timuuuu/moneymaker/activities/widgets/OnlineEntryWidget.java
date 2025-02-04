package de.timuuuu.moneymaker.activities.widgets;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;

public class OnlineEntryWidget extends FlexibleContentWidget {

  private MoneyMakerAddon addon;

  private boolean otherServers;
  private MoneyPlayer player;

  private boolean placeholder = false;
  private Component placeholderTitle = Component.text("");

  public OnlineEntryWidget(MoneyMakerAddon addon, MoneyPlayer player, boolean otherServers) {
    this.addon = addon;
    this.player = player;
    this.otherServers = otherServers;
  }

  public OnlineEntryWidget(MoneyMakerAddon addon, Component placeholderTitle) {
    this.addon = addon;
    this.placeholder = true;
    this.placeholderTitle = placeholderTitle;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    HorizontalListWidget entry = new HorizontalListWidget().addId("head");
    if(this.placeholder) {
      this.addId("placeholder");
    }

    entry.addEntry(new IconWidget(!this.placeholder ? Icon.head(this.player.uuid()) : Icon.texture(
        ResourceLocation.create("moneymaker", "textures/icon.png"))).addId("avatar"));

    if(!this.placeholder) {
      Component nameComponent = Component.text(this.player.rank().getChatPrefix() + this.player.userName());
      nameComponent.clickEvent(ClickEvent.openUrl("https://laby.net/@" + this.player.userName()));
      entry.addEntry(ComponentWidget.component(nameComponent).addId("userName"));

      if(Util.isAdmin(this.labyAPI.getUniqueId())) {
        Component addonVersionComponent = Component.text("(", NamedTextColor.DARK_GRAY)
                .append(Component.text(this.player.addonVersion(), NamedTextColor.YELLOW))
                .append(Component.text(")", NamedTextColor.DARK_GRAY));
        Component minecraftVersionComponent = Component.text(" (", NamedTextColor.DARK_GRAY)
                .append(Component.text("MC " + this.player.minecraftVersion(), NamedTextColor.GREEN))
                .append(Component.text(")", NamedTextColor.DARK_GRAY));
        Component versionComponent = addonVersionComponent.append(minecraftVersionComponent);
        entry.addEntry(ComponentWidget.component(versionComponent).addId("versions"));
      }
    } else {
      entry.addEntry(ComponentWidget.component(this.placeholderTitle).addId("userName"));
    }

    HorizontalListWidget data = new HorizontalListWidget().addId("data");

    if(!this.placeholder || this.otherServers) {
      String server = this.player.server();
      if(server.startsWith("Farming") && server.contains(" - ")) {
        if(server.split(" - ").length == 2) {
          String cave = server.split(" - ")[1];
          server = "Farming - " + I18n.translate(this.addon.addonUtil().caveByName(cave).translation());
        }
      }
      Component component = Component.text("➥ ", NamedTextColor.DARK_GRAY)
              .append(Component.text("Server", NamedTextColor.GRAY))
              .append(Component.text(":", NamedTextColor.DARK_GRAY))
              .append(Component.text(server, NamedTextColor.GRAY));
      data.addEntry(ComponentWidget.component(component).addId("currentServer"));
    }

    this.addContent(entry);
    this.addContent(data);
  }
}
