package de.timuuuu.moneymaker.listener;

import de.timuuuu.moneymaker.utils.AddonSettings;
import de.timuuuu.moneymaker.utils.Booster;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

public class ChatReceiveListener {
  @Subscribe(Priority.LATEST)
  public void onChatReceive(ChatReceiveEvent event) {
    String plain = event.chatMessage().getOriginalPlainText();
    if (plain.contains("[MoneyMaker] +") && plain.contains("Booster (")) {
      int boost = Integer.parseInt(plain.split(" ")[1].substring(1));
      Booster.sessionBoost.addAndGet(boost);
      int time = Integer.parseInt(plain.split(" ")[4].substring(1));
      if (plain.contains("Stunde")) time *= 60;
      Booster.insertBooster(boost,time);
    }
  }
}