package de.timuuuu.moneymaker.moneychat.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class DiscordLinkMessageListener implements MessageListener {

  private final MoneyMakerAddon addon;

  public DiscordLinkMessageListener(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void listen(String message) {
    JsonElement element = addon.gson().fromJson(message, JsonElement.class);
    if(element.isJsonObject()) {
      JsonObject object = element.getAsJsonObject();
      String status = object.get("status").getAsString();
      if(status.equals("failed") && object.has("reason")) {
        String reason = object.get("reason").getAsString();
        if(this.addon.tokenVerificationActivity().isScreenOpened()) {
          this.addon.tokenVerificationActivity().setError(reason);
          return;
        }
        this.addon.displayMessage(this.addon.prefix.copy().append(Component.translatable("moneymaker.verification.request.failed", TextColor.color(255, 85, 85), Component.text(reason, NamedTextColor.RED))));
      }
    }
  }

}
