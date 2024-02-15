package de.timuuuu.moneymaker.boosters;

import de.timuuuu.moneymaker.utils.MoneyTextures.SpriteCommon;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BoosterUtil {

  private static List<Booster> lime = new ArrayList<>();
  private static List<Booster> yellow = new ArrayList<>();
  private static List<Booster> blue = new ArrayList<>();
  private static List<Booster> purple = new ArrayList<>();

  public static TextColor getColor(Booster booster) {
    AtomicReference<TextColor> color = new AtomicReference<>(NamedTextColor.GRAY);
    lime.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        color.set(NamedTextColor.GREEN);
      }
    });
    yellow.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        color.set(NamedTextColor.YELLOW);
      }
    });
    blue.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        color.set(NamedTextColor.BLUE);
      }
    });
    purple.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        color.set(NamedTextColor.DARK_PURPLE);
      }
    });
    return color.get();
  }

  public static Icon getIcon(Booster booster) {
    AtomicReference<Icon> icon = new AtomicReference<>(SpriteCommon.BOOSTER_GRAY);
    lime.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        icon.set(SpriteCommon.BOOSTER_LIME);
      }
    });
    yellow.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        icon.set(SpriteCommon.BOOSTER_YELLOW);
      }
    });
    blue.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        icon.set(SpriteCommon.BOOSTER_BLUE);
      }
    });
    purple.forEach(saved -> {
      if(booster.boost() == saved.boost() && booster.time() == saved.time()) {
        icon.set(SpriteCommon.BOOSTER_PURPLE);
      }
    });
    return icon.get();
  }

  public static void loadData() {

    lime.addAll(Arrays.asList(
       new Booster(50, 15),
        new Booster(50, 20),
        new Booster(50, 30),
        new Booster(50, 45),
        new Booster(50, 90),

        new Booster(40, 15),
        new Booster(40, 20),
        new Booster(40, 30),
        new Booster(40, 45),
        new Booster(40, 60),
        new Booster(40, 120),

        new Booster(30, 15),
        new Booster(30, 30),
        new Booster(30, 45),
        new Booster(30, 60),
        new Booster(30, 120),

        new Booster(20, 15),
        new Booster(20, 45),
        new Booster(20, 60),
        new Booster(20, 120),
        new Booster(20, 360),
        new Booster(20, 480),

        new Booster(10, 20),
        new Booster(10, 30),
        new Booster(10, 45),
        new Booster(10, 60),
        new Booster(10, 120)
    ));

    yellow.addAll(Arrays.asList(
       new Booster(10, 720),

       new Booster(60, 45),
       new Booster(60, 60),

        new Booster(150, 120),

        new Booster(200, 45),
        new Booster(200, 60),

        new Booster(250, 90),

        new Booster(300, 30),
        new Booster(300, 60)
    ));

    blue.addAll(Arrays.asList(
       new Booster(40, 180),

       new Booster(50, 180),

        new Booster(60, 20),
        new Booster(60, 30),

        new Booster(70, 10),
        new Booster(70, 20),

        new Booster(100, 15),
        new Booster(100, 30),

        new Booster(200, 15),
        new Booster(200, 30),

        new Booster(300, 5)
    ));

    purple.addAll(Arrays.asList(
       new Booster(70, 15),

       new Booster(80, 10),
       new Booster(90, 10),

        new Booster(100, 60),
        new Booster(100, 120),

        new Booster(150, 15),
        new Booster(150, 30),
        new Booster(150, 60),

        new Booster(250, 15),
        new Booster(250, 45),

        new Booster(300, 10)
    ));

  }

}
