package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.util.I18n;

@SpriteSlot(x = 2)
public class DebrisPriceWidget extends TextHudWidget<TextHudWidgetConfig> {

  private MoneyMakerAddon addon;
  private TextLine textLine;

  public DebrisPriceWidget(MoneyMakerAddon addon) {
    super("mm_debris_price");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("moneymaker.hudWidget.mm_debris_price.name"), "0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(this.addon.addonUtil().connectedToMoneyMaker()) {

      if(!this.addon.addonUtil().balance().equals("X") && !this.addon.addonUtil().debrisCost().equals("X")) {

        if(this.addon.addonUtil().nextWorkerCost().equals("X")) {

          String[] kontoSplit = this.addon.addonUtil().balance().split(" ");
          if(kontoSplit.length > 1) {
            String balanceUnit = kontoSplit[1];
            String debrisUnit = this.addon.addonUtil().debrisCost().split(" ")[1];

            double balance = Double.parseDouble(this.addon.addonUtil().balance().replaceAll("[^\\d.]", ""));
            double cost = Double.parseDouble(this.addon.addonUtil().debrisCost().replaceAll("[^\\d.]", ""));
            int difference = Double.compare(balance, cost);

            // -> Colors are not working when using as a Component
            // -> however Text Decorations are working fine

            String color = "§c";
            if(CurrencyUtil.get(balanceUnit) > CurrencyUtil.get(debrisUnit)) {
              color = "§a§l";
            } else {
              if(CurrencyUtil.get(balanceUnit) == CurrencyUtil.get(debrisUnit)) {
                if(difference >= 0) {
                  color = "§a";
                } else {
                  color = "§6";
                }
              }
            }

            itemName = color + this.addon.addonUtil().balance() + " / " + this.addon.addonUtil().debrisCost();

            /*if(CurrencyUtil.units.get(balanceUnit) >= CurrencyUtil.units.get(debrisUnit)) {
              itemName = (difference >= 0 ? "§a" : "§6") + AddonSettings.balance + " / " + AddonSettings.debrisCost;
            } else {
              itemName = "§c" + AddonSettings.balance + " / " + AddonSettings.debrisCost;
            }*/
          } else {
            itemName = "§c" + this.addon.addonUtil().balance() + " / " + this.addon.addonUtil().debrisCost();
          }

        } else {
          itemName = I18n.translate("moneymaker.hudWidget.mm_debris_price.unlock_last_miner");
        }

      }

    }

    this.textLine.updateAndFlush(itemName);
    this.textLine.setState(this.addon.addonUtil().connectedToMoneyMaker() && !this.addon.addonUtil().balance().equals("X") && !this.addon.addonUtil().debrisCost().equals("X") ? State.VISIBLE : State.HIDDEN);
  }

}
