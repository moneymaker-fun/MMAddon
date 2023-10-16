package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;

public class ResetCommand extends Command {

  public ResetCommand() {
    super("mm-reset");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(prefix.equalsIgnoreCase("mm-reset")) {
      AddonSettings.resetValues(false);
      this.displayMessage(Component.text(AddonSettings.prefix).append(Component.translatable("moneymaker.command.reset", TextColor.color(85, 255, 85))));
    }
    return true;
  }

}
