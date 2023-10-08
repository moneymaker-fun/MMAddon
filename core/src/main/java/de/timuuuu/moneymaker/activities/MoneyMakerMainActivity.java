package de.timuuuu.moneymaker.activities;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;

@AutoActivity
public class MoneyMakerMainActivity extends TabbedActivity {

  private MoneyMakerAddon addon;

  public MoneyMakerMainActivity(MoneyMakerAddon addon) {
    this.addon = addon;
    this.register("moneymaker_start", new DefaultComponentTab(Component.text("Hauptmenü"), new StartActivity(addon)));
    this.register("moneymaker_booster", new DefaultComponentTab(Component.text("Booster"), new BoosterActivity(addon)));
    this.register("moneymaker_chat", new DefaultComponentTab(Component.text("Chat"), addon.chatActivity));
  }

  public void registerSecret() {
    String id = "moneymaker_secret";
    if(getElementById(id) == null) {
      this.register("moneymaker_secret", new DefaultComponentTab(Component.text("Secret"), new SecretActivity(this.addon)));
      Laby.labyAPI().minecraft().executeOnRenderThread(this::reload);
    }
  }

}
