package de.timuuuu.moneymaker.managers;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.thirdparty.discord.DiscordActivity.Asset;
import net.labymod.api.thirdparty.discord.DiscordActivity.Builder;
import net.labymod.api.util.I18n;
import net.labymod.api.util.concurrent.task.Task;

public class DiscordAPI {

  private MoneyMakerAddon addon;

  private DiscordActivity saved = null;

  private boolean busy;
  private Task updaterTask;

  public DiscordAPI(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private String line1 = "";
  private String line2 = "";

  public void setSaved() {
    if(this.saved != null) return;
    this.saved = this.addon.labyAPI().thirdPartyService().discord().getDisplayedActivity();
  }

  public void removeSaved() {
    this.saved = null;
  }

  public void removeCustom() {
    if(this.saved != null) {
      this.addon.labyAPI().thirdPartyService().discord().displayActivity(this.saved);
      return;
    }
    this.addon.labyAPI().thirdPartyService().discord().displayDefaultActivity(false);
  }

  public void update() {
    if(this.busy) return;
    if(!AddonSettings.playingOn.contains("MoneyMaker")) return;
    if(!this.addon.configuration().discordRichPresence().get()) return;

    this.busy = true;

    DiscordActivity current = this.addon.labyAPI().thirdPartyService().discord()
        .getDisplayedActivity();

    Builder builder = DiscordActivity.builder(this);

    if(current != null) {
      builder.start(current.getStartTime());
    }

    builder.details(this.line1);
    builder.state(this.line2);

    builder.largeAsset(Asset.of(
        "http://cdn.terramc.net/moneymaker/miner.png",
        "MoneyMaker Addon v" + this.addon.addonInfo().getVersion()));

    this.addon.labyAPI().thirdPartyService().discord().displayActivity(builder.build());
    this.busy = false;
  }

  private AtomicInteger count = new AtomicInteger(0);

  public void startUpdater() {
    if(this.updaterTask != null) return;
    this.updaterTask = Task.builder(() -> {

      if(AddonSettings.playingOn.contains("MoneyMaker")) {
        count.getAndAdd(1);
        if(AddonSettings.playingOn.contains("Mine")) {
          this.line1 = I18n.translate("moneymaker.discordPresence.mine.currently");
          if(count.get() == 1) {
            this.line2 = I18n.translate("moneymaker.discordPresence.mine.balance") + (AddonSettings.balance.equals("X") ? "?" : AddonSettings.balance);
          }
          if(count.get() == 2) {
            this.line2 = I18n.translate("moneymaker.discordPresence.mine.workers") + AddonSettings.workerCount;
          }
          if(count.get() == 3) {
            count.set(0);
            this.line2 = I18n.translate("moneymaker.discordPresence.mine.ranking") + AddonSettings.rank;
          }
        }

        if(AddonSettings.playingOn.contains("Farming")) {
          this.line1 = I18n.translate("moneymaker.discordPresence.farming.currently");
          if(count.get() == 1) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.blocks") + AddonSettings.currentBrokenBlocks;
          }
          if(count.get() == 2) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.boosters") + Booster.sessionBoost.get() + "%";
          }
          if(count.get() == 3) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.pickaxe.rank") + AddonSettings.pickaxeRanking;
          }
          if(count.get() == 4) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.pickaxe.level") + AddonSettings.pickaxeLevel;
          }
          if(count.get() == 5) {
            count.set(0);
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.ranking") + AddonSettings.rank;
          }
        }

      }
      this.update();
    }).repeat(10, TimeUnit.SECONDS).build();
    this.updaterTask.execute();
  }

  public void cancelUpdater() {
    if(this.updaterTask == null) return;
    this.updaterTask.cancel();
  }

}
