package de.timuuuu.moneymaker.utils;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyTextures {

  public static class SpriteCommon {
    public static final ResourceLocation TEXTURE = ResourceLocation.create("moneymaker", "textures/common.png");

    public static final Icon EXCLAMATION_MARK;
    public static final Icon BUG;
    public static final Icon DISCORD;

    public static final Icon HUD_COIN;
    public static final Icon HUD_GOLD_ORE;

    static {
      EXCLAMATION_MARK = sprite64(TEXTURE, 0, 0);
      BUG = sprite64(TEXTURE, 1, 0);
      DISCORD = sprite64(TEXTURE, 2, 0);
      HUD_COIN = sprite64(TEXTURE, 0, 3);
      HUD_GOLD_ORE = sprite64(TEXTURE, 1, 3);
    }

  }

  private static Icon sprite64(ResourceLocation resourceLocation, int slotX, int slotY) {
    return Icon.sprite(resourceLocation, slotX << 6, slotY << 6, 64, 64, 512, 512);
  }

}
