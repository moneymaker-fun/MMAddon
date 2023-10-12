package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.ChatUtil;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.entity.EntityRenderEvent;

public class EntityRenderListener {

  private MoneyMakerAddon addon;

  public EntityRenderListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onRender(EntityRenderEvent event) {
    Entity entity = event.entity();
    if(!entity.toString().contains("ArmorStand")) return;
    if(!AddonSettings.playingOn.contains("MoneyMaker")) return;

    String entityName = ChatUtil.stripColor(event.entity().toString().split("'")[1]);
    if(entityName.equals("Armor Stand")) return;

    if(entityName.contains("Kosten: ")) {
      String costs = entityName.replace("Kosten: ","");

      if(entity.getPosX() == 2.5D || entity.getPosX() == 1001.5D || entity.getPosZ() == -1.5D || entity.getPosZ() == 6.5D) {
        if(!AddonSettings.nextWorkerCost.equals(costs)) {
          AddonSettings.nextWorkerCost = costs;
        }
      }

      if(entity.getPosX() == 5.5D || entity.getPosX() == 1004.5D || entity.getPosZ() == -5.5D || entity.getPosZ() == 1.5) {
        if(!AddonSettings.debrisCost.equals(costs)) {
          AddonSettings.debrisCost = costs;
        }
      }
    }

    if(entityName.contains("Minen-Arbeitsplätze") & entityName.contains("/")) {
      int count = Integer.parseInt(entityName.split("/")[0]);
      if(AddonSettings.workerCount != count) {
        AddonSettings.workerCount = count;
      }
    }

    if(entityName.contains("Geröll entfernen ")) {
      String time = entityName.replace("Geröll entfernen ", "");
      if(AddonSettings.debrisTimerString.equals("X")) {
        AddonSettings.debrisTimerString = time;
      }
    }

  }

}
