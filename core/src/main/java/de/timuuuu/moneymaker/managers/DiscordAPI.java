package de.timuuuu.moneymaker.managers;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import de.timuuuu.moneymaker.utils.Util;
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
    if(!this.addon.configuration().moneyDiscordConfiguration.enabled().get()) return;
    if(!(AddonSettings.inMine || AddonSettings.inFarming)) return;

    this.busy = true;

    DiscordActivity current = this.addon.labyAPI().thirdPartyService().discord()
        .getDisplayedActivity();

    Builder builder = DiscordActivity.builder(this);

    if(current != null) {
      builder.start(current.getStartTime());
    }

    if(this.addon.configuration().moneyDiscordConfiguration.showLocation().get()) {
      builder.details(this.line1);
    } else {
      builder.details("MoneyMaker Addon v" + this.addon.addonInfo().getVersion());
    }
    if(this.addon.configuration().moneyDiscordConfiguration.showStats().get()) {
      builder.state(this.line2);
    } else {
      builder.state("by MisterCore & Timuuuu");
    }

    builder.largeAsset(Asset.of(
        this.imageUrl,
        "MoneyMaker Addon v" + this.addon.addonInfo().getVersion()));

    this.addon.labyAPI().thirdPartyService().discord().displayActivity(builder.build());
    this.busy = false;
  }

  private AtomicInteger count = new AtomicInteger(0);

  public void startUpdater() {
    if(this.updaterTask != null) return;
    this.updaterTask = Task.builder(() -> {

      if(AddonSettings.inMine || AddonSettings.inFarming) {
        count.getAndAdd(1);
        if(AddonSettings.inMine) {
          if(!imageUrl.equals(loreUrl)) {
            imageUrl = loreUrl;
          }
          this.line1 = I18n.translate("moneymaker.discordPresence.mine.currently");
          if(count.get() == 1) {
            this.line2 = I18n.translate("moneymaker.discordPresence.mine.balance") + (AddonSettings.balance.equals("X") ? "?" : AddonSettings.balance);
          }
          if(count.get() == 2) {
            this.line2 = I18n.translate("moneymaker.discordPresence.mine.workers") + AddonSettings.workerCount;
          }
          if(count.get() >= 3) {
            count.set(0);
            this.line2 = I18n.translate("moneymaker.discordPresence.mine.ranking") + AddonSettings.rank;
          }
        }

        if(AddonSettings.inFarming) {
          if(!imageUrl.equals(minerUrl)) {
            imageUrl = minerUrl;
          }
          this.line1 = I18n.translate("moneymaker.discordPresence.farming.currently");
          if(count.get() == 1) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.blocks") + Util.format(AddonSettings.currentBrokenBlocks);
          }
          if(count.get() == 2) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.sessionBlocks") + Util.format(AddonSettings.sessionBlocks);
          }
          if(count.get() == 3) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.boosters") + Util.format(Booster.sessionBoosters.get()) + " (" + Util.format(Booster.sessionBoost.get()) + "%)";
          }
          if(count.get() == 4) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.pickaxe.rank") + Util.format(AddonSettings.pickaxeRanking);
          }
          if(count.get() == 5) {
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.pickaxe.level") + Util.format(AddonSettings.pickaxeLevel);
          }
          if(count.get() >= 6) {
            count.set(0);
            this.line2 = I18n.translate("moneymaker.discordPresence.farming.ranking") + Util.format(AddonSettings.rank);
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
