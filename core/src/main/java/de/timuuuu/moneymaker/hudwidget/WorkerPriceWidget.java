package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import de.timuuuu.moneymaker.utils.MoneyTextures.Common;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;

public class WorkerPriceWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public WorkerPriceWidget(MoneyMakerAddon addon) {
    super("mm_worker_price");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
    this.setIcon(Common.MINER);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_worker_price.display"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(this.addon.addonUtil().connectedToMoneyMaker()) {

      if(!this.addon.addonUtil().balance().equals("X") && !this.addon.addonUtil().nextWorkerCost().equals("X")) {

          String[] kontoSplit = this.addon.addonUtil().balance().split(" ");
          if(kontoSplit.length > 1) {
            String balanceUnit = kontoSplit[1];
            String workerUnit = this.addon.addonUtil().nextWorkerCost().split(" ")[1];

            double balance = Double.parseDouble(this.addon.addonUtil().balance().replaceAll("[^\\d.]", ""));
            double cost = Double.parseDouble(this.addon.addonUtil().nextWorkerCost().replaceAll("[^\\d.]", ""));
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

            itemName = color + this.addon.addonUtil().balance() + " / " + this.addon.addonUtil().nextWorkerCost();

            /*if(CurrencyUtil.get(KontoEinheit) >= CurrencyUtil.get(MinerEinheit)) {
              itemName = (difference >= 0 ? "§a" : "§6") + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;
            } else {
              itemName = "§c" + AddonSettings.balance + " / " + AddonSettings.nextWorkerCost;
            }*/
          }

      }

    }
    this.textLine.updateAndFlush(itemName);
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.addonUtil().balance().equals("X") && !this.addon.addonUtil().nextWorkerCost().equals("X") ? State.VISIBLE : State.HIDDEN);
  }

}
