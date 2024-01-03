package de.timuuuu.moneymaker.hudwidget;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.CurrencyUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;

@SpriteSlot(x = 2)
public class DebrisPriceWidget extends ItemHudWidget<HudWidgetConfig> {

  private MoneyMakerAddon addon;

  public DebrisPriceWidget(MoneyMakerAddon addon) {
    super("mm_debris_price");
    this.addon = addon;
    this.bindCategory(MoneyMakerAddon.CATEGORY);
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    String itemName = "N/A";
    if(AddonSettings.inMine || AddonSettings.inFarming) {

      if(!AddonSettings.balance.equals("X")) {
        if(!AddonSettings.debrisCost.equals("X")) {

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
              itemName = (difference >= 0 ? "§6" : "§c") + AddonSettings.balance + " / " + AddonSettings.debrisCost;
            } else {
              itemName = "§c" + AddonSettings.balance + " / " + AddonSettings.debrisCost;
            }
          }

        }
      }

    }

    this.updateItemName(Component.text(itemName), isEditorContext);
  }

  @Override
  public boolean isVisibleInGame() {
    return (AddonSettings.inMine || AddonSettings.inFarming) && !AddonSettings.balance.equals("X") && !AddonSettings.debrisCost.equals("X");
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.sprite16(ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 2, 0);
  }

}
