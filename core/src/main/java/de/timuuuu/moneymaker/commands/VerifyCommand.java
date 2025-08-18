package de.timuuuu.moneymaker.commands;

import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketAddonMessage;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;

public class VerifyCommand extends Command {

  private MoneyMakerAddon addon;

  public VerifyCommand(MoneyMakerAddon addon) {
    super("mm-verify");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(prefix.equalsIgnoreCase("mm-verify")) {
      if(this.addon.configuration().enabled().get()) {

        if(this.addon.addonUtil().connectedToMoneyMaker()) {
          if(arguments.length == 1) {
            if(this.addon.moneyChatClient().isAuthenticated()) {
              if(arguments[0].equalsIgnoreCase("discord")) {
                JsonObject payload = new JsonObject();
                payload.addProperty("uuid", this.labyAPI.getUniqueId().toString());
                payload.addProperty("username", this.labyAPI.getName());
                this.addon.moneyChatClient().sendPacket(new MoneyPacketAddonMessage("discord_link", payload));
              } else if(arguments[0].equalsIgnoreCase("website")) {
                JsonObject payload = new JsonObject();
                payload.addProperty("uuid", this.labyAPI.getUniqueId().toString());
                payload.addProperty("username", this.labyAPI.getName());
                this.addon.moneyChatClient().sendPacket(new MoneyPacketAddonMessage("website_register", payload));
              } else {
                this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.verification.commandUsage", TextColor.color(255, 85, 85))));
              }
            } else {
              this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.verification.request.chatNotConnected", TextColor.color(255, 85, 85))));
            }
          } else {
            this.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.verification.commandUsage", TextColor.color(255, 85, 85))));
          }
        } else {
          this.displayMessage(this.addon.prefix.copy().append(
              Component.translatable("moneymaker.command.notConnected", TextColor.color(255, 85, 85))));
        }

      }
    }
    return true;
  }


}
