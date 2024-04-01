package de.timuuuu.moneymaker.utils;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyTextures {

  public static class Common {

    public static final Icon MINER;
    public static final Icon ICON;
    public static final Icon ICON_RED;
    public static final Icon ICON_DARK_RED;
    public static final Icon ICON_ORANGE;

    static {
      MINER = Icon.texture(ResourceLocation.create("moneymaker", "textures/miner.png"));
      ICON = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"));
      ICON_RED = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_light_red.png"));
      ICON_DARK_RED = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_red.png"));
      ICON_ORANGE = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_orange.png"));
    }

  }

  public static class SpriteCommon {
    public static final ResourceLocation TEXTURE = ResourceLocation.create("moneymaker", "textures/common.png");

    public static final Icon EXCLAMATION_MARK;
    public static final Icon BUG;
    public static final Icon DISCORD;

    public static final Icon HUD_COIN;
    public static final Icon HUD_GOLD_ORE;

    public static final Icon HUD_EASTER_EGG;
    public static final Icon HUD_FLOWER;
    public static final Icon HUD_FRUITS;
    public static final Icon HUD_CANDIES;
    public static final Icon HUD_GIFT;

    public static final Icon BOOSTER_LIME;
    public static final Icon BOOSTER_BLUE;
    public static final Icon BOOSTER_YELLOW;
    public static final Icon BOOSTER_PURPLE;
    public static final Icon BOOSTER_GRAY;

    static {
      EXCLAMATION_MARK = sprite64(TEXTURE, 0, 0);
      BUG = sprite64(TEXTURE, 1, 0);
      DISCORD = sprite64(TEXTURE, 2, 0);

      HUD_COIN = sprite64(TEXTURE, 0, 3);
      HUD_GOLD_ORE = sprite64(TEXTURE, 1, 3);

      HUD_EASTER_EGG = sprite64(TEXTURE, 0, 4);
      HUD_FLOWER = sprite64(TEXTURE, 1, 4);
      HUD_FRUITS = sprite64(TEXTURE, 2, 4);
      HUD_CANDIES = sprite64(TEXTURE, 3, 4);
      HUD_GIFT = sprite64(TEXTURE, 4, 4);

      BOOSTER_LIME = sprite64(TEXTURE, 0, 7);
      BOOSTER_BLUE = sprite64(TEXTURE, 1, 7);
      BOOSTER_YELLOW = sprite64(TEXTURE, 2, 7);
      BOOSTER_PURPLE = sprite64(TEXTURE,3, 7);
      BOOSTER_GRAY = sprite64(TEXTURE,4, 7);
    }

  }

  private static Icon sprite64(ResourceLocation resourceLocation, int slotX, int slotY) {
    return Icon.sprite(resourceLocation, slotX << 6, slotY << 6, 64, 64, 512, 512);
  }

}
