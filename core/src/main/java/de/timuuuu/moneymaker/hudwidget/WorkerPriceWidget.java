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

      if(!AddonSettings.balance.equals("X")) {
        if(!AddonSettings.nextWorkerCost.equals("X")) {

          //String KontoEinheit = AddonSettings.balance.replaceAll("\\d", "").substring(1);
          //String MinerEinheit = AddonSettings.nextWorkerCost.replaceAll("\\d", "").substring(1);

          String[] kontoSplit = AddonSettings.balance.split(" ");
          if(kontoSplit.length > 1) {
            String KontoEinheit = kontoSplit[1];
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

    }
    this.textLine.updateAndFlush(itemName);
    this.textLine.setState((AddonSettings.inMine || AddonSettings.inFarming) && !AddonSettings.balance.equals("X") && !AddonSettings.nextWorkerCost.equals("X") ? State.VISIBLE : State.HIDDEN);
  }

}
