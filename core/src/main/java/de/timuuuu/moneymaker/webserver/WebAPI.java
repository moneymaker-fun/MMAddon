package de.timuuuu.moneymaker.webserver;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.MoneyChatMessage;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.HashMap;

public class WebAPI {

  public static void postMessage(MoneyChatMessage chatMessage) {
    HashMap<String, String> body = new HashMap<>();
    body.put("request", "sendChatMessage");
    body.put("uuid", chatMessage.uuid().toString());
    body.put("userName", chatMessage.userName());
    body.put("time", chatMessage.time());
    body.put("message", chatMessage.message());

    Request.ofString()
        .url("http://api.terramc.net/chat")
        .method(Method.POST)
        .body(body)
        .execute(response -> {});
  }

  public static void postAddonStatistics(MoneyMakerAddon addon, boolean insert) {
    HashMap<String, String> body = new HashMap<>();
    if(insert) {
      body.put("request", "insertAddonStatistics");
      body.put("uuid", addon.labyAPI().getUniqueId().toString());
      body.put("userName", addon.labyAPI().getName());
      body.put("addonVersion", addon.addonInfo().getVersion());
      body.put("gameVersion", addon.labyAPI().minecraft().getVersion());
    } else {
      body.put("request", "deleteAddonStatistics");
      body.put("uuid", addon.labyAPI().getUniqueId().toString());
    }

    Request.ofString()
        .url("http://api.terramc.net/chat")
        .method(Method.POST)
        .body(body)
        .execute(response -> {});
  }

}
