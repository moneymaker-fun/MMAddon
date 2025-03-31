package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.chat.ChatUtil;
import de.timuuuu.moneymaker.event.ArmorStandRenderEvent;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Util;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.concurrent.task.Task;

public class EntityRenderListener {

  private MoneyMakerAddon addon;

  public EntityRenderListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private int checkCount = 0;

  @Subscribe
  public void onArmorStandRender(ArmorStandRenderEvent event) {
    if(!this.addon.addonUtil().inMine()) return;
    Entity entity = event.entity();

    checkCount++;
    if(checkCount >= this.addon.addonSettings().CHECK_RENDER()) {
      checkCount = 0;

      String entityName = ChatUtil.stripColor(event.customName());

      if(entityName.contains("Kosten: ") || entityName.contains("Price: ")) {
        String costs = entityName.replace("Kosten: ","").replace("Price: ", "");

        if(AddonSettings.workerCoordinates.get("x").contains(entity.position().getX()) || AddonSettings.workerCoordinates.get("z").contains(entity.position().getZ())) {
          if(!this.addon.addonUtil().nextWorkerCost().equals(costs)) {
            this.addon.addonUtil().nextWorkerCost(costs);
          }
        }

        if(AddonSettings.debrisCoordinates.get("x").contains(entity.position().getX()) || AddonSettings.debrisCoordinates.get("z").contains(entity.position().getZ())) {
          if(!this.addon.addonUtil().debrisCost().equals(costs)) {
            this.addon.addonUtil().debrisCost(costs);
          }
        }
      }

      if((entityName.contains("Minen-Arbeitsplätze") || entityName.contains("mining workplaces")) & entityName.contains("/")) {
        int count = Util.parseInteger(entityName.split("/")[0], this.getClass());
        if(this.addon.addonUtil().workerCount() != count) {
          this.addon.addonUtil().workerCount(count);
        }
      }

      if(entityName.contains("Geröll entfernen ") || entityName.contains("Remove debris ")) {
        String time = entityName.replace("Geröll entfernen ", "").replace("Remove debris ", "");

        if((time.contains(" Stunde") && time.contains(" Minute")) || (time.contains(" hour") && time.contains(" minute"))) {
          time = time.replace(" Stunden", "").replace(" Stunde", "").replace(" hours", "").replace(" hours", "");
          time = time.replace(" Minuten", "").replace(" Minute", "").replace(" minutes", "").replace(" minute", "");
          time = time.replace(" ", ":");
          time = time + ":00";
          if(this.addon.addonUtil().debrisTime() == 0 & !timerRunning) {
            this.addon.addonUtil().debrisTime(Util.timeToInt(time, true));
            startDebrisTask();
          }
        } else {
          if(this.addon.addonUtil().debrisTime() == 0 & !timerRunning) {
            this.addon.addonUtil().debrisTime(Util.timeToInt(time, false));
            startDebrisTask();
          }
        }

      }

    }

  }

  private boolean timerRunning = false;
  private Task debrisTask;

  private void startDebrisTask() {
    this.timerRunning = true;
    this.debrisTask = Task.builder(() -> {
      this.addon.addonUtil().debrisTime(this.addon.addonUtil().debrisTime() -1);
      if(this.addon.addonUtil().debrisTime() <= 0) {
        debrisTask.cancel();
        timerRunning = false;
      }
    }).repeat(1, TimeUnit.SECONDS).build();
    this.debrisTask.execute();
  }

  public void stopDebrisTask() {
    if(this.debrisTask == null) return;
    this.debrisTask.cancel();
    this.timerRunning = false;
    this.addon.addonUtil().debrisTime(0);
  }

}
