package de.timuuuu.moneymaker.utils;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class MoneyTextures {

  public static class Common {

    public static final Icon MINER = Icon.texture(ResourceLocation.create("moneymaker", "textures/miner.png"));
    public static final Icon ICON = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon.png"));
    public static final Icon ICON_RED = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_light_red.png"));
    public static final Icon ICON_DARK_RED = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_red.png"));
    public static final Icon ICON_ORANGE = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_orange.png"));
    public static final Icon ICON_GREEN = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_green.png"));
    public static final Icon ICON_GRAY = Icon.texture(ResourceLocation.create("moneymaker", "textures/icon_gray.png"));

  }

  public static class SpriteCommon {
    public static final ResourceLocation TEXTURE = ResourceLocation.create("moneymaker", "textures/common.png");

    public static final Icon EXCLAMATION_MARK = sprite64(TEXTURE, 0, 0);
    public static final Icon BUG = sprite64(TEXTURE, 1, 0);
    public static final Icon DISCORD = sprite64(TEXTURE, 2, 0);
    public static final Icon SERVER = Icon.sprite16(ResourceLocation.create("moneymaker", "themes/vanilla/textures/settings/hud/hud.png"), 1, 2);

    public static final Icon CLOSE = sprite64(TEXTURE, 0, 1);
    public static final Icon LABY_ICON = sprite64(TEXTURE, 1, 1);

    public static final Icon HUD_COIN = sprite64(TEXTURE, 0, 3);
    public static final Icon HUD_GOLD_ORE = sprite64(TEXTURE, 1, 3);

    public static final Icon HUD_EASTER_EGG = sprite64(TEXTURE, 0, 4);
    public static final Icon HUD_FLOWER = sprite64(TEXTURE, 1, 4);
    public static final Icon HUD_FRUITS = sprite64(TEXTURE, 2, 4);
    public static final Icon HUD_CANDIES = sprite64(TEXTURE, 3, 4);
    public static final Icon HUD_GIFT = sprite64(TEXTURE, 4, 4);

    public static final Icon BOOSTER_LIME = sprite64(TEXTURE, 0, 7);
    public static final Icon BOOSTER_BLUE = sprite64(TEXTURE, 1, 7);
    public static final Icon BOOSTER_YELLOW = sprite64(TEXTURE, 2, 7);
    public static final Icon BOOSTER_PURPLE = sprite64(TEXTURE,3, 7);
    public static final Icon BOOSTER_GRAY = sprite64(TEXTURE,4, 7);

  }

  private static Icon sprite64(ResourceLocation resourceLocation, int slotX, int slotY) {
    return Icon.sprite(resourceLocation, slotX << 6, slotY << 6, 64, 64, 512, 512);
  }

}
