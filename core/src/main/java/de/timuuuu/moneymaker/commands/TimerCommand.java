package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.Constants.Resources;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.concurrent.task.Task;
import java.util.concurrent.TimeUnit;

public class TimerCommand extends Command {

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
              Util.timers.put(timerName, new MoneyTimer(timerName, minutes, Task.builder(() -> {
                this.addon.labyAPI().minecraft().sounds().playSound(Resources.SOUND_MARKER_NOTIFY, 0.5F, 1.0F);
                this.addon.pushNotification(Component.text("Timer abgelaufen!"), Component.text("§7Dein Timer mit dem Namen §e" + timerName + " §7ist abgelaufen."));
                this.addon.startActivity.reloadScreen();
                Util.timers.remove(timerName);
              }).delay(minutes, TimeUnit.MINUTES).build()));
              Util.timers.get(timerName).task().execute();
              this.displayMessage(AddonSettings.prefix + "§7Der Timer §e" + timerName + " §7mit §e" + minutes + " Minuten §7wurde erstellt.");
              this.addon.startActivity.reloadScreen();
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