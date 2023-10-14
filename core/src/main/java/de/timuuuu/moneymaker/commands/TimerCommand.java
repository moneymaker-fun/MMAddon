package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.chat.command.Command;

public class TimerCommand extends Command {

  private int warningAmount = 10;
  private boolean warningSent = false;

  private MoneyMakerAddon addon;

  public TimerCommand(MoneyMakerAddon addon) {
    super("mm-timer");
    this.addon = addon;
  }

  // /mm-timer <Time in minutes> <Name>
  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(prefix.equalsIgnoreCase("mm-timer")) {
      if(arguments.length >= 2) {
        try {
          int minutes = Integer.parseInt(arguments[0]);
          StringBuilder builder = new StringBuilder();
          for(int i = 1; i != arguments.length; i++) {
            builder.append(arguments[i]).append(" ");
          }
          String timerName = builder.toString().trim();
          if(timerName.length() <= 32) {
            if(!Util.timers.containsKey(timerName)) {
              Util.timers.put(timerName, new MoneyTimer(timerName, minutes).start());
              this.displayMessage(AddonSettings.prefix + "§7Der Timer §e" + timerName + " §7mit §e" + minutes + " Minuten §7wurde erstellt.");
              this.addon.startActivity.reloadScreen();
              if(Util.timers.size() > warningAmount & !warningSent) {
                warningSent = true;
                this.displayMessage(AddonSettings.prefix + "§7Bitte beachte, dass je mehr Timer du erstellt es zu Performance Verlusten kommen kann.");
              }
            } else {
              this.displayMessage(AddonSettings.prefix + "§cEs existiert bereits ein Timer mit diesem Namen.");
            }
          } else {
            this.displayMessage(AddonSettings.prefix + "§cDer Timer-Name darf nur 32 Zeichen lang sein.");
          }
        } catch (NumberFormatException ignored) {
          this.displayMessage(AddonSettings.prefix + "§c<Zeit in Minuten> muss eine Zahl sein.");
        }
      } else {
        this.displayMessage(AddonSettings.prefix + "§cBitte nutze /mm-timer <Zeit in Minuten> <Name des Timers>");
      }
    }
    return true;
  }
}
