package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.SwordTickEvent;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatUtil;
import net.labymod.api.event.Subscribe;

public class TickListener {

  private MoneyMakerAddon addon;

  public TickListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private int tickCount = 0;
  private int checkTick = 5*20; // 5 Seconds

  @Subscribe
  public void onSwordTick(SwordTickEvent event) {
    if(!AddonSettings.playingOn.contains("Farming")) return;
    tickCount++;
    if(tickCount == checkTick) {
      tickCount = 0;

      String rankingLine = event.getLoreList().get(2);
      String mobsLine = event.getLoreList().get(3);
      if(rankingLine == null || mobsLine == null) return;

      /*
      §bStatistiken: [Statistiken:]
      §7Ranking: §6§6Platz 9.472 §7(Getötete Mobs) [Ranking: Platz 9.472 (Getötete Mobs)]
      §7Getötete Mobs: §e103 [Getötete Mobs: 103]
      */

      if(event.getGameVersion().equals("1.8") || event.getGameVersion().equals("1.12")) {

        rankingLine = ChatUtil.stripColor(rankingLine);
        mobsLine = ChatUtil.stripColor(mobsLine);

        if(rankingLine.startsWith("Ranking: ")) {
          AddonSettings.swordRanking = rankingLine.split(" ")[2];
        }

        if(mobsLine.startsWith("Getötete Mobs: ")) {
          AddonSettings.swordMobs = mobsLine.split(" ")[2];
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
          if(!extraObject.get("extra").isJsonArray()) return;
          JsonArray array = extraObject.get("extra").getAsJsonArray();
          for(int i = 0; i != array.size(); i++) {
            if(array.get(i).isJsonObject()) {
              JsonObject object = array.get(i).getAsJsonObject();
              if(object.has("text")) {
                String text = object.get("text").getAsString();
                if(text.contains("Platz ")) {
                  AddonSettings.swordRanking = text.replace("Platz ", "");
                }
                if(text.contains("Rank ")) {
                  AddonSettings.swordRanking = text.replace("Rank ", "");
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
          if(!extraObject.get("extra").isJsonArray()) return;
          JsonArray array = extraObject.get("extra").getAsJsonArray();
          for(int i = 0; i != array.size(); i++) {
            if(array.get(i).isJsonObject()) {
              JsonObject object = array.get(i).getAsJsonObject();
              if(object.has("text")) {
                String text = object.get("text").getAsString();
                if(!text.contains("Getötete Mobs: ")) {
                  AddonSettings.swordMobs = text;
                }
                if(!text.contains("Killed mobs: ")) {
                  AddonSettings.swordMobs = text;
                }
              }
            }
          }

        } catch (JsonSyntaxException ignored) {}
      }

      if(!AddonSettings.swordMobs.equals("X")) {
        try {
          int mobKills = Integer.parseInt(AddonSettings.swordMobs);
          if(AddonSettings.mobKills == 0) {
            AddonSettings.mobKills = mobKills;
          } else {
            int sessionKills = mobKills - AddonSettings.mobKills;
            if(sessionKills >= 0) {
              AddonSettings.sessionKills = sessionKills;
            }
          }
        } catch (NumberFormatException ignored) {}
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
