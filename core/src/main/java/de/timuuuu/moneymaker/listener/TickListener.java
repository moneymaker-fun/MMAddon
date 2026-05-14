package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.EventUtil.Item;
import de.timuuuu.moneymaker.utils.ChatUtil;
import de.timuuuu.moneymaker.event.HotbarItemTickEvent;
import de.timuuuu.moneymaker.events.CaveLevelChangeEvent;
import de.timuuuu.moneymaker.utils.AddonUtil.FarmingCave;
import de.timuuuu.moneymaker.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.util.StringUtil;

public class TickListener {

  private MoneyMakerAddon addon;

  public TickListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private int swordTickCount = 0;
  private int pickaxeTickCount = 0;
  private int generalTickCount = 0;

  @Subscribe
  public void onGameTick(GameTickEvent event) {
    if(!this.addon.addonUtil().inFarming()) return;
    if(this.addon.labyAPI().minecraft().getClientPlayer() == null) return;
    generalTickCount++;
    if(generalTickCount >= this.addon.addonSettings().CHECK_TICK()) {
      generalTickCount = 0;

      double playerY = Objects.requireNonNull(this.addon.labyAPI().minecraft().getClientPlayer()).position().getY();
      FarmingCave currentCave = this.addon.addonUtil().farmingCave();

      // Gold Ebene
      if(playerY > FarmingCave.GOLD.minY()) {
        if(currentCave != FarmingCave.GOLD) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, FarmingCave.GOLD));
        }

      // Kohle Ebene
      } else if (playerY <= FarmingCave.COAL.maxY() && playerY > FarmingCave.COAL.minY()) {
        if(currentCave != FarmingCave.COAL) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, FarmingCave.COAL));
        }

      // Eisen Ebene
      } else if(playerY <= FarmingCave.IRON.maxY()) {
        if(currentCave != FarmingCave.IRON) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, FarmingCave.IRON));
        }

      // Unknown
      } else {
        if(currentCave != FarmingCave.UNKNOWN) {
          Laby.fireEvent(new CaveLevelChangeEvent(currentCave, FarmingCave.UNKNOWN));
        }
      }

    }
  }

  // 1.8.9: [0:"",1:"§bStatistiken:",2:"§7Ranking: §6§6Top 43 % §7(Getötete Mobs)",3:"§7Getötete Mobs: §e118"]
  // 1.21.4 and newer: [empty, literal{Statistiken:}[style={color=aqua,!italic}], empty[siblings=[literal{Ranking: }[style={color=gray,!italic}], literal{Top 43 % }[style={color=gold,!italic}], literal{(Getötete Mobs)}[style={color=gray,!italic}]]], empty[siblings=[literal{Getötete Mobs: }[style={color=gray,!italic}], literal{118}[style={color=yellow,!italic}]]]]
  @Subscribe
  public void onHotbarItemTick(HotbarItemTickEvent event) {
    if(!this.addon.addonUtil().inFarming()) return;

    if(event.item() == Item.SWORD) {
      swordTickCount++;
      if(swordTickCount >= this.addon.addonSettings().CHECK_TICK()) {
        swordTickCount = 0;

        String rankingLine = null;
        String mobsLine = null;

        if(MinecraftVersions.V1_8_9.isCurrent()) {
          if(event.getLoreList() != null) {
            Matcher rankingMatcher = Pattern
                .compile(this.addon.chatMessageLoader().message("item.ranking") + "(.*?)\"")
                .matcher(event.getLoreList().toString());

            if (rankingMatcher.find()) {
              rankingLine = rankingMatcher.group(1);
            }

            Matcher mobsMatcher = Pattern
                .compile(this.addon.chatMessageLoader().message("item.killedMobs") + "(.*?)\"")
                .matcher(event.getLoreList().toString());

            if (mobsMatcher.find()) {
              mobsLine = mobsMatcher.group(1);
            }
          }
        } else {
          if(event.getLoreList() != null && event.getLoreList() instanceof ArrayList<?> loreList) {
            if(loreList.size() < 4) return;

            Object rankingLore = loreList.get(2);
            if(rankingLore == null) return;
            Component rankingComponent = this.addon.labyAPI().minecraft().componentMapper().fromMinecraftComponent(rankingLore);
            if(rankingComponent == null) return;
            List<Component> rankingChildren = rankingComponent.getChildren();
            if(rankingChildren == null || rankingChildren.isEmpty()) return;
            TextComponent rankingTextComponent = (TextComponent) rankingChildren.get(1);
            rankingLine = rankingTextComponent.getText();

            Object mobsLore = loreList.get(3);
            if(mobsLore == null) return;
            Component mobsComponent = this.addon.labyAPI().minecraft().componentMapper().fromMinecraftComponent(mobsLore);
            if(mobsComponent == null) return;
            List<Component> mobsChildren = mobsComponent.getChildren();
            if(mobsChildren == null || mobsChildren.isEmpty()) return;
            TextComponent mobsTextComponent = (TextComponent) mobsChildren.get(1);
            mobsLine = mobsTextComponent.getText();
          }
        }

        rankingLine = ChatUtil.stripColor(rankingLine);
        mobsLine = ChatUtil.stripColor(mobsLine);

        if(rankingLine != null) {
          rankingLine = rankingLine
              .replaceAll("§.", "")
              .replace(addon.chatMessageLoader().message("item.place"), "")
              .replaceAll("\\s*\\([^)]*\\)", "")
              .trim();
          if(StringUtil.isNumeric(rankingLine)) {
            this.addon.addonUtil().swordRanking(Util.parseInteger(rankingLine
                .replace(".", "").replace(",", ""), this.getClass()));
          }
        }

        if(mobsLine != null) {
          this.addon.addonUtil().swordMobs(Util.parseInteger(mobsLine.replace(".", "").replace(",", ""), this.getClass()));
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
    }

    if(event.item() == Item.PICKAXE) {
      pickaxeTickCount++;
      if(pickaxeTickCount >= this.addon.addonSettings().CHECK_TICK()) {
        pickaxeTickCount = 0;

        //if(event.getLoreList().size() < 10) return;
        //if(event.getLoreList().get(9) == null) return;
        String chanceLine = ""; //event.getLoreList().get(9);

      /*
      §bStatistiken: [Statistiken:]
      §7Ranking: §6§6Platz 9.472 §7(Getötete Mobs) [Ranking: Platz 9.472 (Getötete Mobs)]
      §7Getötete Mobs: §e103 [Getötete Mobs: 103]
      */
        chanceLine = ChatUtil.stripColor(chanceLine);

        if(chanceLine.startsWith(this.addon.chatMessageLoader().message("item.boosterChance"))) {
          if(chanceLine.split(" ")[3] != null) {
            String chance = chanceLine.split(" ")[3];
            this.addon.addonUtil().pickaxeBoosterChance(chance.contains("%") ? chance : chance + "%");
          }
        }
      }
    }

  }

}
