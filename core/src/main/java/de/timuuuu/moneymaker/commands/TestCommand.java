package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;

public class TestCommand extends Command {

  private MoneyMakerAddon addon;

  public TestCommand(MoneyMakerAddon addon) {
    super("mm-test");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] args) {
    if(prefix.equalsIgnoreCase("mm-test")) {
      if(Util.isAdmin(this.labyAPI.getUniqueId())) {
        if(this.addon.configuration().enabled().get()) {
          if(this.addon.addonUtil().connectedToMoneyMaker()) {

            if(args.length == 1) {
              if(args[0].equalsIgnoreCase("debug-debris")) {
                this.addon.addonUtil().debrisTime(20*60);
                this.addon.entityRenderListener().startDebrisTask();
              } else if(args[0].equalsIgnoreCase("stop-debris-task")) {
                this.addon.entityRenderListener().stopDebrisTask();
              } else if(args[0].equalsIgnoreCase("get-booster")) {
                this.displayMessage("[MoneyMaker] eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                this.displayMessage("[MoneyMaker]");
                this.displayMessage("[MoneyMaker] Glückwunsch! Du hast einen Booster gefunden:");
                this.displayMessage("[MoneyMaker] +50 % Booster (15 Minuten)");
                this.displayMessage("[MoneyMaker]");
                this.displayMessage("[MoneyMaker] eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
              } else {
                this.displayMessage("Invalid Argument. (debug-debris, stop-debris-task)");
              }
            } else {
              this.displayMessage("Insufficient Arguments.");
            }

          } else {
            this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.command.notConnected", TextColor.color(255, 85, 85))));
          }
        }
      }
    }
    return true;
  }

}
