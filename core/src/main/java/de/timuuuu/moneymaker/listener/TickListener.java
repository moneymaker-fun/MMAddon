package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.event.SwordTickEvent;
import de.timuuuu.moneymaker.settings.AddonSettings;
import net.labymod.api.event.Subscribe;

public class TickListener {

  private MoneyMakerAddon addon;

  public TickListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private int tickCount = 0;

  @Subscribe
  public void onSwordTick(SwordTickEvent event) {
    if(!AddonSettings.inFarming) return;
    tickCount++;
    if(tickCount == AddonSettings.CHECK_TICK) {
      tickCount = 0;

      if(event.getLoreList().get(2) == null || event.getLoreList().get(3) == null) return;
      String rankingLine = event.getLoreList().get(2);
      String mobsLine = event.getLoreList().get(3);

      /*
      §bStatistiken: [Statistiken:]
      §7Ranking: §6§6Platz 9.472 §7(Getötete Mobs) [Ranking: Platz 9.472 (Getötete Mobs)]
      §7Getötete Mobs: §e103 [Getötete Mobs: 103]
      */

      if(event.getGameVersion().equals("1.8") || event.getGameVersion().equals("1.12")) {

        rankingLine = ChatUtil.stripColor(rankingLine);
        mobsLine = ChatUtil.stripColor(mobsLine);

        if(rankingLine.startsWith("Ranking: ")) {
          if(!(rankingLine.contains("Lädt...") || rankingLine.contains("Loading..."))) {
            AddonSettings.swordRanking = Integer.parseInt(rankingLine.split(" ")[2]
                .replace(".", "").replace(",", ""));
          }
        }

        if(mobsLine.startsWith("Getötete Mobs: ")) {
          AddonSettings.swordMobs = Integer.parseInt(mobsLine.replace("Getötete Mobs: ", "")
              .replace(".", "").replace(",", ""));
        }

        if(mobsLine.startsWith("Killed mobs: ")) {
          AddonSettings.swordMobs = Integer.parseInt(mobsLine.replace("Killed mobs: ", "").replace(".", "").replace(",", ""));
        }

        return;
      }

      if(rankingLine.contains("Ranking: ")) {
        try {
          JsonObject loreObject = JsonParser.parseString(rankingLine).getAsJsonObject();
          if(!loreObject.has("extra")) return;
          if(!loreObject.get("extra").isJsonArray()) return;
          JsonElement extraElement = loreObject.get("extra").getAsJsonArray().get(0);
          if(extraElement == null) return;
          if(!extraElement.isJsonObject()) return;
          JsonObject extraObject = extraElement.getAsJsonObject();
          if(!extraObject.has("extra")) return;
          if(!extraObject.get("extra").isJsonArray()) return;
          JsonArray array = extraObject.get("extra").getAsJsonArray();
          for(int i = 0; i != array.size(); i++) {
            if(array.get(i).isJsonObject()) {
              JsonObject object = array.get(i).getAsJsonObject();
              if(object.has("text")) {
                String text = object.get("text").getAsString();
                if(text.contains("Platz ")) {
                  AddonSettings.swordRanking = Integer.parseInt(text.replace("Platz ", "")
                      .replace(".", "").replace(",", "").strip());
                }
                if(text.contains("Rank ")) {
                  AddonSettings.swordRanking = Integer.parseInt(text.replace("Rank ", "")
                      .replace(".", "").replace(",", "").strip());
                }
              }
            }
          }

        } catch (JsonSyntaxException ignored) {}
      }

      if(mobsLine.contains("Getötete Mobs: ") || mobsLine.contains("Killed mobs: ")) {
        try {
          JsonObject loreObject = JsonParser.parseString(mobsLine).getAsJsonObject();
          if(!loreObject.has("extra")) return;
          if(!loreObject.get("extra").isJsonArray()) return;
          JsonElement extraElement = loreObject.get("extra").getAsJsonArray().get(0);
          if(extraElement == null) return;
          if(!extraElement.isJsonObject()) return;
          JsonObject extraObject = extraElement.getAsJsonObject();
          if(!extraObject.has("extra")) return;
          if(!extraObject.get("extra").isJsonArray()) return;
          JsonArray array = extraObject.get("extra").getAsJsonArray();
          for(int i = 0; i != array.size(); i++) {
            if(array.get(i).isJsonObject()) {
              JsonObject object = array.get(i).getAsJsonObject();
              if(object.has("text")) {
                String text = object.get("text").getAsString();
                if(!(text.contains("Getötete Mobs: ") || text.contains("Killed mobs: "))) {
                  AddonSettings.swordMobs = Integer.parseInt(text.replace(".", "").replace(",", ""));
                }
              }
            }
          }

        } catch (JsonSyntaxException ignored) {}
      }

      if(AddonSettings.swordMobs != 0) {
        if(AddonSettings.mobKills == 0) {
          AddonSettings.mobKills = AddonSettings.swordMobs;
        } else {
          int sessionKills = AddonSettings.swordMobs - AddonSettings.mobKills;
          if(sessionKills >= 0) {
            AddonSettings.sessionKills = sessionKills;
          }
        }
      }

      if(AddonSettings.swordRanking != 0 && AddonSettings.savedSwordRanking == 0) {
        AddonSettings.savedSwordRanking = AddonSettings.swordRanking;
      }

    }

    /*
    Lore-List:
    {"italic":false,"text":""}
    {"italic":false,"extra":[{"color":"aqua","text":"Statistiken:"}],"text":""}
    {"italic":false,"extra":[{"extra":[{"color":"gray","text":"Ranking: "},{"color":"gold","text":"Platz 9.523 "},{"color":"gray","text":"(Getötete Mobs)"}],"text":""}],"text":""}
    {"italic":false,"extra":[{"extra":[{"color":"gray","text":"Getötete Mobs: "},{"color":"yellow","text":"101"}],"text":""}],"text":""}

    Visualisation: https://jsonlint.com/
     */
  }

}
