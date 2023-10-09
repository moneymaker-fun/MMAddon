package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class WorkerPriceWidget extends ItemHudWidget<HudWidgetConfig> {

  private MoneyMakerAddon addon;

  public WorkerPriceWidget(MoneyMakerAddon addon) {
    super("worker_price");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/miner.png")));
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(AddonSettings.playingOn.contains("MoneyMaker")) {

      if(!AddonSettings.balance.equals("X")) {
        if(!AddonSettings.nextWorkerCost.equals("X")) {

          //String KontoEinheit = AddonSettings.balance.replaceAll("\\d", "").substring(1);
          //String MinerEinheit = AddonSettings.nextWorkerCost.replaceAll("\\d", "").substring(1);

          String KontoEinheit = AddonSettings.balance.split(" ")[1];
          String MinerEinheit = AddonSettings.nextWorkerCost.split(" ")[1];

          if(CurrencyUtil.units.get(KontoEinheit) >= CurrencyUtil.units.get(MinerEinheit)) {
            double d1 = Double.parseDouble(AddonSettings.balance.replaceAll("[^\\d.]", ""));
            double d2 = Double.parseDouble(AddonSettings.nextWorkerCost.replaceAll("[^\\d.]", ""));
            int difference = Double.compare(d1, d2);
            itemName = (difference >= 0 ? "§6" : "§c") + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;
          } else {
            itemName = "§c" + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;
          }

        }
      }

    }

    this.updateItemName(Component.text(itemName), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return AddonSettings.playingOn.contains("MoneyMaker") && !AddonSettings.balance.equals("X") && !AddonSettings.nextWorkerCost.equals("X");
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/miner.png"));
  }

}
