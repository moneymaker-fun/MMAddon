package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import de.timuuuu.moneymaker.event.InventoryClickEvent;
import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import de.timuuuu.moneymaker.events.MineSwitchEvent;
import de.timuuuu.moneymaker.events.ProfileSwitchEvent;
import de.timuuuu.moneymaker.utils.AddonUtil.MineType;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryListener {

  // TODO: Display current mine in Discord RPC, MoneyMaker Chat & Gamemode Switch Notification
  // -> Detect Mine switch via Inventory Click
  // -> Default value is null, when value is null don't display anything

  private static List<SlotItem> alreadyRendered = new ArrayList<>();
  private static int totalBoosters = 0;
  private int previousBoost = -1;
  private int previousTotalBoosters = -1;
  private long lastDisplayTime = 0; // Initialize last display time to 0

  private final long DISPLAY_COOLDOWN = 30000;

  private String currentProfile = "";

  private MoneyMakerAddon addon;

  public InventoryListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onInventoryClick(InventoryClickEvent event) {

    if(event.getInventoryName().equals("Server wechseln") || event.getInventoryName().equals("Switch server")) {

      this.addon.logger().info(event.getItemName());

      String mineName = null;
      if(event.textVersion() == TextVersion.RAW) {
        mineName = event.getItemName();
      } else {
        JsonElement element = JsonParser.parseString(event.getItemName());
        if(element != null && element.isJsonObject()) {
          JsonObject object = element.getAsJsonObject();
          if(object.has("text")) {
            mineName = object.get("text").getAsString();
          }
        }
      }

      if(mineName != null) {
        MineType mineType = this.addon.addonUtil().mineByTranslation(mineName);
        if(mineType != null) {
          if(this.addon.addonUtil().currentMine() != mineType) {
            Laby.fireEvent(new MineSwitchEvent(this.addon.addonUtil().currentMine(), mineType));
            this.addon.addonUtil().currentMine(mineType);
          }
        }
      } else {
        this.addon.displayMessage("Mine Name is null");
      }

    }

    if(event.getInventoryName().equals("Profil-Übersicht") || event.getInventoryName().equals("Profile overview")) {

      if(event.getItemName().contains("Profil-Slot") || event.getItemName().contains("Profile slot") ||
          event.getItemName().contains("Event-Profil") || event.getItemName().contains("Event profile")) {

        String newProfile = null;
        if(event.textVersion() == TextVersion.RAW) {
          newProfile = event.getItemName();
        } else {
          // {"italic":false,"color":"aqua","text":"Profil-Slot 1"}
          try {
            JsonElement element = JsonParser.parseString(event.getItemName());
            if(element != null && element.isJsonObject()) {
              JsonObject object = element.getAsJsonObject();
              if(object.has("text")) {
                newProfile = object.get("text").getAsString();
              }
            }
          } catch (JsonSyntaxException ignored) {}
        }

        if(newProfile != null) {
          if(!this.currentProfile.equals(newProfile)) {
            Laby.fireEvent(new ProfileSwitchEvent(this.currentProfile, newProfile));
            this.currentProfile = newProfile;
          }
        }

      }
    }
  }

  public static void clearAlreadyRendered() {
    alreadyRendered.clear();
    totalBoosters = 0;
  }

  public int getBoost() {
    AtomicInteger boost = new AtomicInteger(0);
    if(!alreadyRendered.isEmpty()) {
      alreadyRendered.forEach(slotItem -> {
        String strippedName = null;
        if(slotItem.textVersion() == TextVersion.RAW) {
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
              int boosting = Util.parseInteger(rawBooster[0], this.getClass());
              int amount = Util.parseInteger(rawBooster[1].replace(")", ""), this.getClass());
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
  public void onInventoryRenderSlot(InventoryRenderSlotEvent event) {

    if(!(event.getInventoryName().startsWith("Booster-Übersicht") || event.getInventoryName().startsWith("Booster overview"))) return;
    if(!(event.getDisplayName().contains("Booster") || event.getDisplayName().contains("booster"))) return;
    if(!this.addon.configuration().showTotalBoostMessage().get()) return;

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

    if(event.textVersion() == TextVersion.RAW) {
      if(event.getLoreList().size() >= 9) {
        String displayName = event.getDisplayName();
        String durationLore = event.getLoreList().get(2);
        SlotItem slotItem = new SlotItem(displayName, durationLore, event.textVersion());
        if(!alreadyRendered.contains(slotItem)) {
          alreadyRendered.add(slotItem);
        }
      }
      return;
    }

    if(event.getLoreList().size() >= 9) {
      String displayName = event.getDisplayName();
      String durationLore = event.getLoreList().get(2);
      SlotItem slotItem = new SlotItem(displayName, durationLore, event.textVersion());
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
    if(!this.addon.configuration().showTotalBoostMessage().get()) return;

    // Inventory Name: DE > Booster-Übersicht (§e328§7/§61.000§r) | EN > Booster overview (§e328§7/§61,000§r)
    String inventoryName = ChatUtil.stripColor(event.getInventoryName()); // DE > Booster-Übersicht (328/1.000) | EN > Booster overview (328/1,000)
    String[] rawInvName = inventoryName.split("\\(");
    if(rawInvName.length == 2) {
      try {
        totalBoosters = Util.parseInteger(rawInvName[1].split("/")[0].replace(".", "").replace(",", ""), this.getClass());
      } catch (NumberFormatException ignored) {}
    }

    int boost = getBoost();
    long currentTime = System.currentTimeMillis();
    if(boost > 0 && totalBoosters > 0 && (boost != previousBoost || totalBoosters != previousTotalBoosters || currentTime - lastDisplayTime >= DISPLAY_COOLDOWN)) {
      this.addon.displayMessage(
          this.addon.prefix.copy()
              .append(Component.translatable(
                  "moneymaker.text.booster.inventory.total", NamedTextColor.GRAY,
                  Component.text(Util.format(totalBoosters), NamedTextColor.YELLOW),
                  Component.text(Util.format(boost) + "%", NamedTextColor.YELLOW)
              ))
              .hoverEvent(HoverEvent.showText(Component.translatable("moneymaker.text.booster.inventory.info", NamedTextColor.GRAY)))
      );

      previousBoost = boost;
      previousTotalBoosters = totalBoosters;
      lastDisplayTime = currentTime;
    }

    clearAlreadyRendered();
  }


  private static class SlotItem {

    private String name;
    private String lore;
    private TextVersion textVersion;

    public SlotItem(String name, String lore, TextVersion textVersion) {
      this.name = name;
      this.lore = lore;
      this.textVersion = textVersion;
    }

    public String getName() {
      return name;
    }

    public String getLore() {
      return lore;
    }

    public TextVersion textVersion() {
      return textVersion;
    }

    @Override
    public boolean equals(Object object) {
      if(object instanceof SlotItem other) {
          return other.getName().equals(this.name) && other.getLore().equals(this.lore) && other.textVersion().equals(this.textVersion);
      }
      return false;
    }

  }

}
