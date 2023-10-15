package de.timuuuu.moneymaker.v1_12_2.mixins;

import de.timuuuu.moneymaker.event.SwordTickEvent;
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
    EntityPlayer player = Minecraft.getMinecraft().player;
    if(player != null) {
      ItemStack itemStack = player.inventory.getStackInSlot(0);
      if(itemStack != null) {
        if(itemStack.getTagCompound() != null) {
          NBTTagCompound compoundTag = itemStack.getTagCompound().getCompoundTag("display");
          String name = itemStack.getDisplayName();
          List<String> loreList = new ArrayList<>();
          NBTTagList listTag = compoundTag.getTagList("Lore", 8);
          for(int i = 0; i != listTag.tagCount(); i++) {
            loreList.add(listTag.getStringTagAt(i));
          }
          Laby.fireEvent(new SwordTickEvent(name, loreList, "1.12"));
        }
      }
    }
  }

}
