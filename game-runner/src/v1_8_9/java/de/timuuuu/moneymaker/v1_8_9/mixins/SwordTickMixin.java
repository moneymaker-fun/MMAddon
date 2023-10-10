package de.timuuuu.moneymaker.v1_8_9.mixins;

import de.timuuuu.moneymaker.event.SwordTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.volt.annotation.Insert;
import net.labymod.api.volt.callback.InsertInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class SwordTickMixin {

  @Insert(
      method = {"runTick()V"},
      at = @At("HEAD")
  )
  private void moneymaker$tick(InsertInfo ci) {
    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    if(player != null) {
      ItemStack itemStack = player.inventory.getStackInSlot(0);
      if(itemStack != null) {
        if(itemStack.getTagCompound() != null) {
          String name = itemStack.getDisplayName();
          NBTTagCompound compoundTag = itemStack.getTagCompound().getCompoundTag("display");
          List<String> loreList = new ArrayList<>();
          NBTTagList listTag = compoundTag.getTagList("Lore", 8);
          for(int i = 0; i != listTag.tagCount(); i++) {
            loreList.add(listTag.getStringTagAt(i));
          }
          Laby.fireEvent(new SwordTickEvent(name, loreList, "1.8"));
        }
      }
    }
  }

}
