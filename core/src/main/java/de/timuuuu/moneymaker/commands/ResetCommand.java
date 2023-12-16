package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.settings.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
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

        if(AddonSettings.playingOn.contains("MoneyMaker")) {
          if(args.length == 1) {
            if(args[0].equalsIgnoreCase("booster")) {

              Booster.activatedBoost.set(0);
              Booster.sessionBoost.set(0);
              Booster.sessionBoosters.set(0);
              Booster.latestFoundBoosters().clear();
              Booster.boosterList().clear();
              this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.reset.booster", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("arbeiter") || args[0].equalsIgnoreCase("miners")) {

              AddonSettings.workerCount = 0;
              AddonSettings.workerNotifySent = false;
              AddonSettings.nextWorkerCost = "X";
              this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.reset.miners", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("geröll") || args[0].equalsIgnoreCase("debris")) {

              AddonSettings.debrisTime = 0;
              AddonSettings.debrisCost = "X";
              AddonSettings.debrisNotifySent = false;
              this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.reset.debris", TextColor.color(85, 255, 85))));

            } else if(args[0].equalsIgnoreCase("all")) {
              AddonSettings.resetValues(false);
              Booster.activatedBoost.set(0);
              Booster.sessionBoost.set(0);
              Booster.latestFoundBoosters().clear();
              Booster.boosterList().clear();
              this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.reset.all", TextColor.color(85, 255, 85))));

            }
          } else {
            this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.reset.usage", TextColor.color(255, 85, 85))));
          }
        } else {
          this.displayMessage(Component.translatable("moneymaker.command.notConnected", TextColor.color(255, 85, 85)));
        }

      }
    }
    return true;
  }

}
