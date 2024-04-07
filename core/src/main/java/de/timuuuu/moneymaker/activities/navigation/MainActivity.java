package de.timuuuu.moneymaker.activities.navigation;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.activities.BoosterActivity;
import de.timuuuu.moneymaker.activities.InfoActivity;
import de.timuuuu.moneymaker.activities.SecretActivity;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;

@AutoActivity
public class MainActivity extends TabbedActivity {

  private MoneyMakerAddon addon;

  public MainActivity(MoneyMakerAddon addon) {
    this.addon = addon;
    this.register("moneymaker_start", new DefaultComponentTab(Component.translatable("moneymaker.navigation.main-menu"), addon.startActivity()));
    this.register("moneymaker_info", new DefaultComponentTab(Component.translatable("moneymaker.navigation.info"), new InfoActivity(addon)));
    this.register("moneymaker_booster", new DefaultComponentTab(Component.translatable("moneymaker.navigation.booster"), new BoosterActivity(addon)));
    this.register("moneymaker_chat", new DefaultComponentTab(Component.translatable("moneymaker.navigation.chat"), addon.chatActivity()));
  }

  public void registerSecret() {
    String id = "moneymaker_secret";
    if(getElementById(id) == null) {
      this.register("moneymaker_secret", new DefaultComponentTab(Component.translatable("moneymaker.navigation.secret"), new SecretActivity(this.addon)));
      Laby.labyAPI().minecraft().executeOnRenderThread(this::reload);
    }
  }

  public void openAndSwitchToChat() {
    this.addon.labyAPI().minecraft().minecraftWindow().displayScreen(this);
    this.addon.labyAPI().minecraft().executeNextTick(() -> this.switchTab("moneymaker_chat"));
  }

}
