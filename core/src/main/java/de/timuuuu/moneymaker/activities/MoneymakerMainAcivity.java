package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.ExampleAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;

@AutoActivity
public class MoneymakerMainAcivity extends TabbedActivity {

  private ExampleAddon addon;

  public MoneymakerMainAcivity(ExampleAddon addon) {
    this.addon = addon;
    this.register("moneymaker_booster", new DefaultComponentTab(Component.text("Booster"), new BoosterActivity(this.addon)));
  }

  public void registerSecret() {
    String id = "moneymaker_secret";
    if(getElementById(id) == null) {
      this.register("moneymaker_secret", new DefaultComponentTab(Component.text("Secret"), new Secret(this.addon)));
      Laby.labyAPI().minecraft().executeOnRenderThread(this::reload);
    }
  }

}
