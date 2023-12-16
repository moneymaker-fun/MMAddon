package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.utils.Util;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.entity.EntityRenderEvent;
import net.labymod.api.util.concurrent.task.Task;

public class EntityRenderListener {

  private MoneyMakerAddon addon;

  public EntityRenderListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onRender(EntityRenderEvent event) {
    Entity entity = event.entity();
    if(!this.addon.configuration().enabled().get()) return;
    if(!AddonSettings.playingOn.contains("MoneyMaker")) return;

    String entityName = ChatUtil.stripColor(event.entity().toString().split("'")[1]);

    if(entityName.contains("Kosten: ") || entityName.contains("Price: ")) {
      String costs = entityName.replace("Kosten: ","").replace("Price: ", "");

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

    if((entityName.contains("Minen-Arbeitsplätze") || entityName.contains("mining workplaces")) & entityName.contains("/")) {
      int count = Integer.parseInt(entityName.split("/")[0]);
      if(AddonSettings.workerCount != count) {
        AddonSettings.workerCount = count;
      }
    }

    if(entityName.contains("Geröll entfernen ") || entityName.contains("Remove debris ")) {
      String time = entityName.replace("Geröll entfernen ", "").replace("Remove debris ", "");

      if((time.contains(" Stunde") && time.contains(" Minute")) || (time.contains(" hour") && time.contains(" minute"))) {
        time = time.replace(" Stunden", "").replace(" Stunde", "").replace(" hours", "").replace(" hours", "");
        time = time.replace(" Minuten", "").replace(" Minute", "").replace(" minutes", "").replace(" minute", "");
        time = time.replace(" ", ":");
        time = time + ":00";
        if(AddonSettings.debrisTime == 0 & !timerRunning) {
          AddonSettings.debrisTime = Util.timeToInt(time, true);
          startTask();
        }
      } else {
        if(AddonSettings.debrisTime == 0 & !timerRunning) {
          AddonSettings.debrisTime = Util.timeToInt(time, false);
          startTask();
        }
      }

    }

  }

  private boolean timerRunning = false;
  private Task debrisTask;

  private void startTask() {
    timerRunning = true;
    debrisTask = Task.builder(() -> {
      AddonSettings.debrisTime--;
      if(AddonSettings.debrisTime <= 0) {
        debrisTask.cancel();
        timerRunning = false;
      }
    }).repeat(1, TimeUnit.SECONDS).build();
    debrisTask.execute();
  }

}
