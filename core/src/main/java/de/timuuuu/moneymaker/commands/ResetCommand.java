package de.timuuuu.moneymaker.commands;

import de.timuuuu.moneymaker.utils.AddonSettings;
import net.labymod.api.client.chat.command.Command;

public class ResetCommand extends Command {

  public ResetCommand() {
    super("mm-reset");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(prefix.equalsIgnoreCase("mm-reset")) {
      AddonSettings.resetValues(false);
      this.displayMessage(AddonSettings.prefix + "§aDie Daten wurden zurückgesetzt.");
    }
    return true;
  }

}
