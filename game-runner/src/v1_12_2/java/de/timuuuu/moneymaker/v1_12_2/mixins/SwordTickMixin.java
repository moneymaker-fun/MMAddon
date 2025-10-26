package de.timuuuu.moneymaker.v1_12_2.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.EventUtil.Item;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import de.timuuuu.moneymaker.event.HotbarItemTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class SwordTickMixin {

  @Inject(
      method = {"runTick()V"},
      at = @At("HEAD")
  )
  private void moneymaker$tick(CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    EntityPlayer player = Minecraft.getMinecraft().player;
    if(player != null) {

      ItemStack swordItem = player.inventory.getStackInSlot(Item.SWORD.slotNumber());
      if(swordItem != null) {
        if(swordItem.getTagCompound() != null) {
          NBTTagCompound compoundTag = swordItem.getTagCompound().getCompoundTag("display");
          String name = swordItem.getDisplayName();
          List<String> loreList = new ArrayList<>();
          NBTTagList listTag = compoundTag.getTagList("Lore", 8);
          for(int i = 0; i != listTag.tagCount(); i++) {
            loreList.add(listTag.getStringTagAt(i));
          }
          Laby.fireEvent(new HotbarItemTickEvent(Item.SWORD, name, loreList, TextVersion.RAW));
        }
      }

      ItemStack pickaxeItem = player.inventory.getStackInSlot(Item.PICKAXE.slotNumber());
      if(pickaxeItem != null) {
        if(pickaxeItem.getTagCompound() != null) {
          NBTTagCompound compoundTag = pickaxeItem.getTagCompound().getCompoundTag("display");
          String name = pickaxeItem.getDisplayName();
          List<String> loreList = new ArrayList<>();
          NBTTagList listTag = compoundTag.getTagList("Lore", 8);
          for(int i = 0; i != listTag.tagCount(); i++) {
            loreList.add(listTag.getStringTagAt(i));
          }
          Laby.fireEvent(new HotbarItemTickEvent(Item.PICKAXE, name, loreList, TextVersion.RAW));
        }
      }

    }
  }

}
