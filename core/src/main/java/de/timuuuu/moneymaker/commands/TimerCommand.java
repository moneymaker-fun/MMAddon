package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.MoneyTimer;
import de.timuuuu.moneymaker.utils.Util;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;

public class TimerCommand extends Command {

  private int maxNameLength = 32;
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
      if(AddonSettings.playingOn.contains("MoneyMaker")) {
        if(arguments.length >= 2) {
          try {
            int minutes = Integer.parseInt(arguments[0]);
            StringBuilder builder = new StringBuilder();
            for(int i = 1; i != arguments.length; i++) {
              builder.append(arguments[i]).append(" ");
            }
            String timerName = builder.toString().trim();
            if(timerName.length() <= maxNameLength) {
              if(!Util.timers.containsKey(timerName)) {
                Util.timers.put(timerName, new MoneyTimer(timerName, minutes).start());
                this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.timer.created", TextColor.color(170, 170, 170),
                    Component.text(timerName, TextColor.color(255, 255, 85)), Component.text(minutes, TextColor.color(255, 255, 85))
                )));
                this.addon.startActivity.reloadScreen();
                if(Util.timers.size() > warningAmount & !warningSent) {
                  warningSent = true;
                  this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.timer.performance", TextColor.color(255, 85, 85))));
                }
              } else {
                this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.timer.already-exists", TextColor.color(255, 85, 85))));
              }
            } else {
              this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.timer.max-length", TextColor.color(255, 85, 85),
                  Component.text(maxNameLength))));
            }
          } catch (NumberFormatException ignored) {
            this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.timer.no-number", TextColor.color(255, 85, 85))));
          }
        } else {
          this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.timer.usage", TextColor.color(255, 85, 85))));
        }
      } else {
        this.displayMessage(Component.translatable("moneymaker.command.notConnected", TextColor.color(255, 85, 85)));
      }
    }
    return true;
  }
}
