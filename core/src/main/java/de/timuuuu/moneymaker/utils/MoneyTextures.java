package de.timuuuu.moneymaker.utils;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyTextures {

  public static class SpriteCommon {
    public static final ResourceLocation TEXTURE = ResourceLocation.create("moneymaker", "textures/common.png");

    public static final Icon EXCLAMATION_MARK;
    public static final Icon BUG;

    static {
      EXCLAMATION_MARK = sprite64(TEXTURE, 0, 0);
      BUG = sprite64(TEXTURE, 1, 0);
    }

  }

  private static Icon sprite64(ResourceLocation resourceLocation, int slotX, int slotY) {
    return Icon.sprite(resourceLocation, slotX << 8, slotY << 8, 64, 64, 512, 512);
  }

}
