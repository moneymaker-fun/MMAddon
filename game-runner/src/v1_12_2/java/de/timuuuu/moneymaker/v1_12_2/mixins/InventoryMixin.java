package de.timuuuu.moneymaker.v1_12_2.mixins;

import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiContainer.class})
public class InventoryMixin {

  @Shadow
  public Container inventorySlots;

  @Inject(
      method = {"drawSlot"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryRender(Slot slot, CallbackInfo ci) {
    if(slot.getStack() != null) {
      if(slot.getStack().getTagCompound() != null) {
        NBTTagCompound compoundTag = slot.getStack().getTagCompound().getCompoundTag("display");
        String name = slot.getStack().getDisplayName();
        List<String> loreList = new ArrayList<>();
        NBTTagList listTag = compoundTag.getTagList("Lore", 8);
        for(int i = 0; i != listTag.tagCount(); i++) {
          loreList.add(listTag.getStringTagAt(i));
        }
        Laby.fireEvent(new InventoryRenderSlotEvent(slot.inventory.getName(), slot.slotNumber, name, loreList, "1.12"));
      }
    }
  }

  @Inject(
      method = {"onGuiClosed"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClose(CallbackInfo ci) {
    String inventoryName = "Unknown Inventory";
    if(inventorySlots instanceof IInventory) {
      inventoryName = ((IInventory) inventorySlots).getName();
    }
    Laby.fireEvent(new InventoryCloseEvent(inventoryName));
  }

}
