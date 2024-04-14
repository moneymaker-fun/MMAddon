package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.event.SwordTickEvent;
import de.timuuuu.moneymaker.events.CaveLevelChangeEvent;
import de.timuuuu.moneymaker.utils.AddonUtil.MiningCave;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import java.util.List;
import java.util.Objects;

public class TickListener {

  private MoneyMakerAddon addon;

  public TickListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private int swordTickCount = 0;
  private int generalTickCount = 0;

  @Subscribe
  public void onGameTick(GameTickEvent event) {
    if(!this.addon.addonUtil().inFarming()) return;
    if(this.addon.labyAPI().minecraft().getClientPlayer() == null) return;
    generalTickCount++;
    if(generalTickCount >= this.addon.addonSettings().CHECK_TICK()) {
      generalTickCount = 0;

      float playerY = Objects.requireNonNull(this.addon.labyAPI().minecraft().getClientPlayer()).position().getY();
      MiningCave currentCave = this.addon.addonUtil().miningCave();

      // Gold Ebene
      if(playerY > MiningCave.GOLD.minY()) {
        if(currentCave != MiningCave.GOLD) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, MiningCave.GOLD));
        }

      // Kohle Ebene
      } else if (playerY <= MiningCave.COAL.maxY() && playerY > MiningCave.COAL.minY()) {
        if(currentCave != MiningCave.COAL) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, MiningCave.COAL));
        }

      // Eisen Ebene
      } else if(playerY <= MiningCave.IRON.maxY()) {
        if(currentCave != MiningCave.IRON) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, MiningCave.IRON));
        }

      // Unknown
      } else {
        if(currentCave != MiningCave.UNKNOWN) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, MiningCave.UNKNOWN));
        }
      }

    }
  }

  @Subscribe
  public void onSwordTick(SwordTickEvent event) {
    if(!this.addon.addonUtil().inFarming()) return;
    swordTickCount++;
    if(swordTickCount >= this.addon.addonSettings().CHECK_TICK()) {
      swordTickCount = 0;

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
            this.addon.addonUtil().swordRanking(Util.parseInteger(rankingLine.split(" ")[2]
                .replace(".", "").replace(",", ""), this.getClass()));
          }
        }

        if(mobsLine.startsWith("Getötete Mobs: ")) {
          this.addon.addonUtil().swordMobs(Util.parseInteger(mobsLine.replace("Getötete Mobs: ", "")
              .replace(".", "").replace(",", ""), this.getClass()));
        }

        if(mobsLine.startsWith("Killed mobs: ")) {
          this.addon.addonUtil().swordMobs(Util.parseInteger(mobsLine.replace("Killed mobs: ", "")
              .replace(".", "").replace(",", ""), this.getClass()));
        }

        return;
      }

      if(rankingLine.contains("Ranking: ")) {
        List<String> line = Util.getTextFromJsonObject(rankingLine);
        if(line.size() != 3) return;
        if(line.get(1) == null) return;
        String text = line.get(1);
        if(text.contains("Platz ")) {
          this.addon.addonUtil().swordRanking(Util.parseInteger(text.replace("Platz ", "")
              .replace(".", "").replace(",", "").strip(), this.getClass()));
        }
        if(text.contains("Rank ")) {
          this.addon.addonUtil().swordRanking(Util.parseInteger(text.replace("Rank ", "")
              .replace(".", "").replace(",", "").strip(), this.getClass()));
        }
      }

      if(mobsLine.contains("Getötete Mobs: ") || mobsLine.contains("Killed mobs: ")) {
        List<String> line = Util.getTextFromJsonObject(mobsLine);
        if(line.size() != 2) return;
        if(line.get(1) == null) return;
        this.addon.addonUtil().swordMobs(Util.parseInteger(line.get(1).replace(".", "").replace(",", ""), this.getClass()));
      }

      if(this.addon.addonUtil().swordMobs() != 0) {
        if(this.addon.addonUtil().mobKills() == 0) {
          this.addon.addonUtil().mobKills(this.addon.addonUtil().swordMobs());
        } else {
          int sessionKills = this.addon.addonUtil().swordMobs() - this.addon.addonUtil().mobKills();
          if(sessionKills >= 0) {
            this.addon.addonUtil().sessionKills(sessionKills);
          }
        }
      }

      if(this.addon.addonUtil().swordRanking() != 0 && this.addon.addonUtil().savedSwordRanking() == 0) {
        this.addon.addonUtil().savedSwordRanking(this.addon.addonUtil().swordRanking());
      }

    }

    /*
    Lore-List:
    Visualisation: https://jsonlint.com/

    {"italic":false,"text":""},
    {"italic":false,"color":"aqua","text":"Statistiken:"},
    {"italic":false,"extra":[{"color":"gray","text":"Ranking: "},{"color":"gold","text":"Platz 215 "},{"color":"gray","text":"(Getötete Mobs)"}],"text":""},
    {"italic":false,"extra":[{"color":"gray","text":"Getötete Mobs: "},{"color":"yellow","text":"6.243"}],"text":""}
     */

  }

}
