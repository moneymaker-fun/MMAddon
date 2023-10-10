package de.timuuuu.moneymaker.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.SwordTickEvent;
import de.timuuuu.moneymaker.events.MoneyChatReceiveEvent;
import de.timuuuu.moneymaker.events.MoneyPlayerStatusEvent;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import java.util.UUID;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;

public class MoneyAddonListener {

  private MoneyMakerAddon addon;

  public MoneyAddonListener(MoneyMakerAddon addon) {
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
              }
            }
          }

        } catch (JsonSyntaxException ignored) {}
      }

      if(mobsLine.contains("Getötete Mobs: ")) {
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
              }
            }
          }

        } catch (JsonSyntaxException ignored) {}
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

  @Subscribe
  public void onPlayerStatusUpdate(MoneyPlayerStatusEvent event) {
    UUID uuid = event.uuid();
    MoneyPlayer player = event.player();
    if(!player.server().equals("OFFLINE")) {
      AddonSettings.playerStatus.put(uuid, player);
    } else {
      AddonSettings.playerStatus.remove(uuid);
    }
  }

  @Subscribe
  public void onMoneyChatReceive(MoneyChatReceiveEvent event) {
    MoneyChatMessage chatMessage = event.chatMessage();
    this.addon.chatActivity.addChatMessage(chatMessage);
    if(!chatMessage.uuid().equals(this.addon.labyAPI().getUniqueId())) {
      if(this.addon.configuration().chatNotification().get()) {
        this.addon.pushNotification(
            Component.text("§aNeue Chat Nachricht"), Component.text("§e" + chatMessage.userName() + "§8: §7" + chatMessage.message()),
            Icon.head(chatMessage.uuid()));
      }
      if(this.addon.configuration().chatNotificationSound().get()) {
        this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.35F, 1.0F);
      }
    }
  }

}
