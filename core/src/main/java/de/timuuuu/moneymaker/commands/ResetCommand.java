package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.boosters.Booster;
import de.timuuuu.moneymaker.listener.InventoryListener;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class ResetCommand extends Command {

  private MoneyMakerAddon addon;

  public ResetCommand(MoneyMakerAddon addon) {
    super("mm-reset");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] args) {
    if(prefix.equalsIgnoreCase("mm-reset")) {
      if(this.addon.configuration().enabled().get()) {

        if(this.addon.addonUtil().connectedToMoneyMaker()) {
          if(args.length == 1) {
            if(args[0].equalsIgnoreCase("booster")) {

              Booster.activatedBoost.set(0);
              Booster.sessionBoost.set(0);
              Booster.sessionBoosters.set(0);
              Booster.latestFoundBoosters().clear();
              Booster.boosterList().clear();
              this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.reset.booster", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("arbeiter") || args[0].equalsIgnoreCase("miners")) {

              this.addon.addonUtil().workerCount(0);
              this.addon.addonUtil().workerNotifySent(false);
              this.addon.addonUtil().nextWorkerCost("X");
              this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.reset.miners", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("geröll") || args[0].equalsIgnoreCase("debris")) {

              this.addon.addonUtil().debrisTime(0);
              this.addon.addonUtil().debrisCost("X");
              this.addon.addonUtil().debrisNotifySent(false);
              this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.reset.debris", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("all")) {
              this.addon.addonUtil().resetValues(false);
              Booster.activatedBoost.set(0);
              Booster.sessionBoost.set(0);
              Booster.latestFoundBoosters().clear();
              Booster.boosterList().clear();
              InventoryListener.clearAlreadyRendered();
              this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.reset.all", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("farming")) {
              this.addon.addonUtil().sessionBlocks(0);
              this.addon.addonUtil().sessionKills(0);
              Booster.sessionBoost.set(0);
              Booster.sessionBoosters.set(0);
              this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.notification.dataReset.farming.done", NamedTextColor.YELLOW)));
            }
          } else {
            this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.reset.usage", TextColor.color(255, 85, 85))));
          }
        } else {
          this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.notConnected", TextColor.color(255, 85, 85))));
        }

      }
    }
    return true;
  }

}
