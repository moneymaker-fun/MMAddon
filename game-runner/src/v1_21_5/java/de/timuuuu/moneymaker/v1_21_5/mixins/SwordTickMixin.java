package de.timuuuu.moneymaker.v1_21_5.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.EventUtil.Item;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import de.timuuuu.moneymaker.event.HotbarItemTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class SwordTickMixin {

  @Inject(
      method = {"tick()V"},
      at = @At("HEAD")
  )
  private void moneymaker$tick(CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    Player player = Minecraft.getInstance().player;
    if(player == null) return;

    ItemStack swordItem = player.getInventory().getItem(Item.SWORD.slotNumber());
    if(swordItem != ItemStack.EMPTY) {
      if(swordItem.get(DataComponents.CUSTOM_NAME) == null) return;
      String name = swordItem.get(DataComponents.CUSTOM_NAME).getString();
      List<String> loreList = new ArrayList<>();
      if(swordItem.get(DataComponents.LORE) == null) return;
      ItemLore itemLore = swordItem.get(DataComponents.LORE);
      for(int i = 0; i != itemLore.lines().size(); i++) {
        loreList.add(itemLore.lines().get(i).getString());
      }
      Laby.fireEvent(new HotbarItemTickEvent(Item.SWORD, name, loreList, TextVersion.RAW));
    }

    ItemStack pickaxeItem = player.getInventory().getItem(Item.PICKAXE.slotNumber());
    if(pickaxeItem != ItemStack.EMPTY) {
      if(pickaxeItem.get(DataComponents.CUSTOM_NAME) == null) return;
      String name = pickaxeItem.get(DataComponents.CUSTOM_NAME).getString();
      List<String> loreList = new ArrayList<>();
      if(pickaxeItem.get(DataComponents.LORE) == null) return;
      ItemLore itemLore = pickaxeItem.get(DataComponents.LORE);
      for(int i = 0; i != itemLore.lines().size(); i++) {
        loreList.add(itemLore.lines().get(i).getString());
      }
      Laby.fireEvent(new HotbarItemTickEvent(Item.PICKAXE, name, loreList, TextVersion.RAW));
    }

  }

}
