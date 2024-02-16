package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class WorkerPriceWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public WorkerPriceWidget(MoneyMakerAddon addon) {
    super("mm_worker_price");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Icon.texture(ResourceLocation.create("moneymaker", "textures/hud/miner.png")));
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_worker_price.display"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(AddonSettings.inMine || AddonSettings.inFarming) {

      if(!AddonSettings.balance.equals("X") && !AddonSettings.nextWorkerCost.equals("X")) {

          String[] kontoSplit = AddonSettings.balance.split(" ");
          if(kontoSplit.length > 1) {
            String balanceUnit = kontoSplit[1];
            String workerUnit = AddonSettings.nextWorkerCost.split(" ")[1];

            double balance = Double.parseDouble(AddonSettings.balance.replaceAll("[^\\d.]", ""));
            double cost = Double.parseDouble(AddonSettings.nextWorkerCost.replaceAll("[^\\d.]", ""));
            int difference = Double.compare(balance, cost);

            String color = "§c";
            if(CurrencyUtil.get(balanceUnit) > CurrencyUtil.get(workerUnit)) {
              color = "§a§l";
            } else {
              if(CurrencyUtil.get(balanceUnit) == CurrencyUtil.get(workerUnit)) {
                if(difference >= 0) {
                  color = "§a";
                } else {
                  color = "§6";
                }
              }
            }

            itemName = color + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;

            /*if(CurrencyUtil.get(KontoEinheit) >= CurrencyUtil.get(MinerEinheit)) {
              itemName = (difference >= 0 ? "§a" : "§6") + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;
            } else {
              itemName = "§c" + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;
            }*/
          }

      }

    }
    this.textLine.updateAndFlush(itemName);
    this.textLine.setState((AddonSettings.inMine || AddonSettings.inFarming) && !AddonSettings.balance.equals("X") && !AddonSettings.nextWorkerCost.equals("X") ? State.VISIBLE : State.HIDDEN);
  }

}
