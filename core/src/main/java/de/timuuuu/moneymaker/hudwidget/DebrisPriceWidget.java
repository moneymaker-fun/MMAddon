package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
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
    if(AddonSettings.inMine || AddonSettings.inFarming) {

      if(!AddonSettings.balance.equals("X") && !AddonSettings.debrisCost.equals("X")) {

        if(AddonSettings.nextWorkerCost.equals("X")) {

          //String KontoEinheit = AddonSettings.balance.replaceAll("\\d", "").substring(1);
          //String MinerEinheit = AddonSettings.nextWorkerCost.replaceAll("\\d", "").substring(1);

          String[] kontoSplit = AddonSettings.balance.split(" ");
          if(kontoSplit.length > 1) {
            String KontoEinheit = kontoSplit[1];
            String DebrisEinheit = AddonSettings.debrisCost.split(" ")[1];

            if(CurrencyUtil.units.get(KontoEinheit) >= CurrencyUtil.units.get(DebrisEinheit)) {
              double d1 = Double.parseDouble(AddonSettings.balance.replaceAll("[^\\d.]", ""));
              double d2 = Double.parseDouble(AddonSettings.debrisCost.replaceAll("[^\\d.]", ""));
              int difference = Double.compare(d1, d2);
              itemName = (difference >= 0 ? "§a" : "§6") + AddonSettings.balance + " / " + AddonSettings.debrisCost;
            } else {
              itemName = "§c" + AddonSettings.balance + " / " + AddonSettings.debrisCost;
            }
          }

        } else {
          itemName = I18n.translate("moneymaker.hudWidget.mm_debris_price.unlock_last_miner");
        }

      }

    }

    this.textLine.updateAndFlush(itemName);
    this.textLine.setState((AddonSettings.inMine || AddonSettings.inFarming) && !AddonSettings.balance.equals("X") && !AddonSettings.debrisCost.equals("X") ? State.VISIBLE : State.HIDDEN);
  }

}
