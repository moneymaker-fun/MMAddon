package de.timuuuu.moneymaker.utils;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.boosters.Booster;
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

  private final String minerUrl = "https://cdn.terramc.net/moneymaker/miner.png";
  private final String loreUrl = "https://cdn.terramc.net/moneymaker/lore.png";

  public DiscordAPI(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  private String imageUrl = loreUrl;
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
    if(!this.addon.configuration().enabled().get()) return;
    if(!this.addon.configuration().discordConfiguration.enabled().get()) return;
    if(!(this.addon.addonUtil().inMine() || this.addon.addonUtil().inFarming())) return;

    this.busy = true;

    DiscordActivity current = this.addon.labyAPI().thirdPartyService().discord()
        .getDisplayedActivity();

    Builder builder = DiscordActivity.builder(this);

    if(current != null) {
      builder.start(current.getStartTime());
    }

    if(this.addon.configuration().discordConfiguration.showLocation().get()) {
      builder.details(this.line1);
    } else {
      builder.details("MoneyMaker Addon v" + this.addon.addonInfo().getVersion() + (this.addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment() ? " DEV" : ""));
    }
    if(this.addon.configuration().discordConfiguration.showStats().get()) {
      builder.state(this.line2);
    } else {
      builder.state("by MisterCore & Seelenverwandter");
    }

    builder.largeAsset(Asset.of(
        this.imageUrl,
        "MoneyMaker Addon v" + this.addon.addonInfo().getVersion() + (this.addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment() ? " DEV" : "")));

    this.addon.labyAPI().thirdPartyService().discord().displayActivity(builder.build());
    this.busy = false;
  }

  private AtomicInteger mineCount = new AtomicInteger(0);
  private AtomicInteger farmingCount = new AtomicInteger(0);

  public void startUpdater() {
    if(this.updaterTask != null) return;
    this.updaterTask = Task.builder(() -> {

      if (this.addon.addonUtil().inMine()) {
        mineCount.getAndAdd(1);
        if (!imageUrl.equals(loreUrl)) {
          imageUrl = loreUrl;
        }
        this.line1 = I18n.translate("moneymaker.discordPresence.mine.currently");
        if (mineCount.get() == 1) {
          this.line2 = I18n.translate("moneymaker.discordPresence.mine.balance") + (this.addon.addonUtil().balance().equals("X") ? "?" : this.addon.addonUtil().balance());
        }
        if (mineCount.get() == 2) {
          this.line2 = I18n.translate("moneymaker.discordPresence.mine.workers") + this.addon.addonUtil().workerCount();
        }
        if (mineCount.get() >= 3) {
          mineCount.set(0);
          this.line2 = I18n.translate("moneymaker.discordPresence.mine.ranking") + this.addon.addonUtil().rank();
        }
      }

      if (this.addon.addonUtil().inFarming()) {
        farmingCount.getAndAdd(1);
        if (!imageUrl.equals(minerUrl)) {
          imageUrl = minerUrl;
        }
        this.line1 = I18n.translate("moneymaker.discordPresence.farming.currently") + " - " + I18n.translate(this.addon.addonUtil().miningCave().translation());
        if (farmingCount.get() == 1) {
          this.line2 = I18n.translate("moneymaker.discordPresence.farming.blocks") + Util.format(this.addon.addonUtil().currentBrokenBlocks());
        }
        if (farmingCount.get() == 2) {
          this.line2 = I18n.translate("moneymaker.discordPresence.farming.sessionBlocks") + Util.format(this.addon.addonUtil().sessionBlocks());
        }
        if (farmingCount.get() == 3) {
          this.line2 = I18n.translate("moneymaker.discordPresence.farming.boosters") + Util.format(Booster.sessionBoosters.get()) + " (" + Util.format(Booster.sessionBoost.get()) + "%)";
        }
        if (farmingCount.get() == 4) {
          this.line2 = I18n.translate("moneymaker.discordPresence.farming.pickaxe.rank") + Util.format(this.addon.addonUtil().pickaxeRanking());
        }
        if (farmingCount.get() == 5) {
          this.line2 = I18n.translate("moneymaker.discordPresence.farming.pickaxe.level") + Util.format(this.addon.addonUtil().pickaxeLevel());
        }
        if (farmingCount.get() >= 6) {
          farmingCount.set(0);
          this.line2 = I18n.translate("moneymaker.discordPresence.farming.ranking") + Util.format(this.addon.addonUtil().rank());
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
