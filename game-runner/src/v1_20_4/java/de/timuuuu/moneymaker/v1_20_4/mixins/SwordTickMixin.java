package de.timuuuu.moneymaker.v1_20_4.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.EventUtil.Item;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import de.timuuuu.moneymaker.event.HotbarItemTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    if(player != null) {

      ItemStack swordItem = player.getInventory().getItem(Item.SWORD.slotNumber());
      if(swordItem != ItemStack.EMPTY) {
        CompoundTag compoundTag = swordItem.getOrCreateTagElement("display");
        String name = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
        List<String> loreList = new ArrayList<>();
        ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
        for(int i = 0; i != listTag.size(); i++) {
          loreList.add(listTag.getString(i));
        }
        Laby.fireEvent(new HotbarItemTickEvent(Item.SWORD, name, loreList, TextVersion.JSON));
      }

      ItemStack pickaxeItem = player.getInventory().getItem(Item.PICKAXE.slotNumber());
      if(pickaxeItem != ItemStack.EMPTY) {
        CompoundTag compoundTag = pickaxeItem.getOrCreateTagElement("display");
        String name = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
        List<String> loreList = new ArrayList<>();
        ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
        for(int i = 0; i != listTag.size(); i++) {
          loreList.add(listTag.getString(i));
        }
        Laby.fireEvent(new HotbarItemTickEvent(Item.PICKAXE, name, loreList, TextVersion.JSON));
      }

    }
  }

}
