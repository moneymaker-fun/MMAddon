package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.event.BoosterInventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryListener {

  private static List<SlotItem> alreadyRendered = new ArrayList<>();

  private MoneyMakerAddon addon;

  public InventoryListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  public int getBoost() {
    AtomicInteger boost = new AtomicInteger(0);
    if(!alreadyRendered.isEmpty()) {
      alreadyRendered.forEach(slotItem -> {
        String strippedName = null;
        if(slotItem.getGameVersion().equals("1.8") || slotItem.getGameVersion().equals("1.12")) {
          strippedName = ChatUtil.stripColor(slotItem.getName()); // +150 % Booster (1)
        } else {
          List<String> rawName = Util.getTextFromJsonObject(slotItem.getName()); // "+2.000 % Booster ","(4)"
          if(rawName.size() == 2) {
            strippedName = rawName.get(0) + rawName.get(1);
          }
        }
        if(strippedName != null) { // Should be: DE > +80 % Booster (5) | EN > +70% booster (3)
          String booster = strippedName.replace("+", "").replace(".", "").replace(",", "").replace("%", "")
              .replace("Booster", "").replace("booster", "").replace(" ", ""); // Should be: 80(5)
          String[] rawBooster = booster.split("\\(");

          if(rawBooster.length == 2) {
            try {
              int boosting = Integer.parseInt(rawBooster[0]);
              int amount = Integer.parseInt(rawBooster[1].replace(")", ""));
              int finalBoost = boosting * amount;
              boost.getAndAdd(finalBoost);
            } catch (NumberFormatException ignored) {}
          }
        }

      });
    }
    return boost.get();
  }

  @Subscribe
  public void onInventoryRenderSlot(BoosterInventoryRenderSlotEvent event) {

    if(!(event.getInventoryName().startsWith("Booster-Übersicht") || event.getInventoryName().startsWith("Booster overview"))) return;
    if(!(event.getDisplayName().contains("Booster") || event.getDisplayName().contains("booster"))) return;

    // 1.8 - 1.12
    // Display Name: §b+150 % Booster §7(1)
    /* Lore:
    [
    §7,
    §7Auswirkung: §e+100 %,
    §7Dauer: §e1 Stunde,
     ,
     §eBooster §7erhöhen deinen,
     §eGesamtgewinn §7in der Mine,
     §7beim Geldeingang auf dein Konto,
      ,
      §a<Klicke zum Einsetzen>,
      §a<Rechtsklick zum Bearbeiten>
      ]
     */

    if(event.getGameVersion().equals("1.8") || event.getGameVersion().equals("1.12")) {
      if(event.getLoreList().size() >= 9) {
        String displayName = event.getDisplayName();
        String durationLore = event.getLoreList().get(2);
        SlotItem slotItem = new SlotItem(displayName, durationLore, event.getGameVersion());
        if(!alreadyRendered.contains(slotItem)) {
          alreadyRendered.add(slotItem);
        }
      }
      return;
    }

    if(event.getLoreList().size() >= 9) {
      String displayName = event.getDisplayName();
      String durationLore = event.getLoreList().get(2);
      SlotItem slotItem = new SlotItem(displayName, durationLore, event.getGameVersion());
      if(!alreadyRendered.contains(slotItem)) {
        alreadyRendered.add(slotItem);
      }
    }

    // 1.16+
    // Display Name:  {"italic":false,"extra":[{"color":"aqua","text":"+2.000 % Booster "},{"color":"gray","text":"(4)"}],"text":""}
    /* Lore:
    [
    {"italic":false,"text":""},
    {"italic":false,"extra":[{"color":"gray","text":"Auswirkung: "},{"color":"yellow","text":"+50 %"}],"text":""},
    {"italic":false,"extra":[{"color":"gray","text":"Dauer: "},{"color":"yellow","text":"30 Minuten"}],"text":""},
    {"italic":false,"text":""},
    {"italic":false,"extra":[{"color":"yellow","text":"Booster "},{"color":"gray","text":"erhöhen deinen"}],"text":""},
    {"italic":false,"extra":[{"color":"yellow","text":"Gesamtgewinn "},{"color":"gray","text":"in der Mine"}],"text":""},
    {"italic":false,"color":"gray","text":"beim Geldeingang auf dein Konto"},
    {"italic":false,"text":""},
    {"italic":false,"color":"green","text":"\u003cKlicke zum Einsetzen\u003e"},
    {"italic":false,"color":"green","text":"\u003cRechtsklick zum Bearbeiten\u003e"}
    ]
     */

  }

  @Subscribe
  public void onInventoryClose(InventoryCloseEvent event) {
    if(!(event.getInventoryName().startsWith("Booster-Übersicht") || event.getInventoryName().startsWith("Booster overview"))) return;
    int boost = getBoost();
    if(boost > 0) {
      this.addon.displayMessage(
          Component.text(this.addon.prefix)
              .append(Component.translatable(
                  "moneymaker.text.booster.inventory.total", NamedTextColor.GRAY,
                  Component.text(alreadyRendered.size(), NamedTextColor.YELLOW),
                  Component.text(boost, NamedTextColor.YELLOW)
              ))
              .hoverEvent(HoverEvent.showText(Component.translatable("moneymaker.text.booster.inventory.info", NamedTextColor.GRAY)))
      );
    }
  }

  private static class SlotItem {

    private String name;
    private String lore;
    private String gameVersion;

    public SlotItem(String name, String lore, String gameVersion) {
      this.name = name;
      this.lore = lore;
      this.gameVersion = gameVersion;
    }

    public String getName() {
      return name;
    }

    public String getLore() {
      return lore;
    }

    public String getGameVersion() {
      return gameVersion;
    }

    @Override
    public boolean equals(Object object) {
      if(object instanceof SlotItem other) {
          return other.getName().equals(this.name) && other.getLore().equals(this.lore) && other.getGameVersion().equals(this.gameVersion);
      }
      return false;
    }

  }

}
