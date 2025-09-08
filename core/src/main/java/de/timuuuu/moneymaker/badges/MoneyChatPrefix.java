package de.timuuuu.moneymaker.badges;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import net.labymod.api.client.chat.prefix.ChatPrefix;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.configuration.labymod.chat.AdvancedChatMessage;
import net.labymod.api.configuration.labymod.chat.ChatTab;
import net.labymod.api.mojang.GameProfile;
import net.labymod.api.util.math.MathHelper;

public class MoneyChatPrefix implements ChatPrefix {

  private MoneyMakerAddon addon;

  public MoneyChatPrefix(MoneyMakerAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(ScreenContext context, float x, float y, AdvancedChatMessage advancedChatMessage, RenderableComponent[] renderableComponents, int index, int subIndex, int lineHeight, float textOffset, double scale, int alpha) {
      if(index == 0 && subIndex == 0) {
        GameProfile gameProfile = this.getProfileFromMessage(advancedChatMessage);
        if(gameProfile == null) return;
        if(!AddonUtil.playerStatus.containsKey(gameProfile.getUniqueId())) return;
        if(AddonUtil.playerStatus.get(gameProfile.getUniqueId()) == null) return;
        MoneyPlayer moneyPlayer = AddonUtil.playerStatus.get(gameProfile.getUniqueId());
        double headSize = 8.0 * scale;
        int margin = 1;
        moneyPlayer.group().getIcon().render(context.stack(), x + (float) margin, y + textOffset, (float) headSize);
      }
  }

  @Override
  public boolean isVisible(ChatTab chatTab, AdvancedChatMessage advancedChatMessage) {
    if(!this.addon.configuration().badgeConfiguration.chatIcon().get()) return false;
    if(!this.addon.addonUtil().connectedToMoneyMaker()) return false;
    if(this.getProfileFromMessage(advancedChatMessage) == null) return false;
    return AddonUtil.playerStatus.containsKey(this.getProfileFromMessage(advancedChatMessage).getUniqueId());
  }

  @Override
  public int getWidth(AdvancedChatMessage advancedChatMessage, double scale) {
    double headSize = 8.0 * scale;
    int margin = 1;
    return MathHelper.ceil(headSize + (double)margin);
  }

  private GameProfile getProfileFromMessage(AdvancedChatMessage chatMessage) {
    if(chatMessage.chatMessage().getSenderProfile() != null) return chatMessage.chatMessage().getSenderProfile();
    String message = chatMessage.chatMessage().getOriginalPlainText();
    if(!message.startsWith("(")) return null;
    if(!message.contains(":")) return null;
    String rawName = message.split(":")[0].split("\\)")[1];
    String userName = rawName.replace(" ", "");
    if(this.addon.labyAPI().minecraft().getClientPacketListener() == null) return null;
    NetworkPlayerInfo info = this.addon.labyAPI().minecraft().getClientPacketListener().getNetworkPlayerInfo(userName);
    if(info == null) return null;
    return info.profile();
  }

}
