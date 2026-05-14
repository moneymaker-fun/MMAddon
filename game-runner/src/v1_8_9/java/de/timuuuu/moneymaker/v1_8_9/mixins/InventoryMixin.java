package de.timuuuu.moneymaker.v1_8_9.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.InventoryClickEvent;
import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import net.labymod.api.Laby;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiContainer.class})
public class InventoryMixin {

  @Inject(
      method = {"drawSlot"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryRender(Slot lvt_1_1_, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(lvt_1_1_ == null) return;
    if(lvt_1_1_.getStack() == null) return;
    if(lvt_1_1_.getStack().getTagCompound() == null) return;
    Laby.fireEvent(new InventoryRenderSlotEvent(lvt_1_1_.inventory.getName(), lvt_1_1_.slotNumber, lvt_1_1_.getStack().getDisplayName()));
  }

  @Inject(
      method = {"handleMouseClick"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClick(Slot lvt_1_1_, int lvt_2_1_, int lvt_3_1_, int lvt_4_1_, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(lvt_1_1_ == null) return;
    if(lvt_1_1_.getStack() == null) return;
    if(lvt_1_1_.getStack().getTagCompound() == null) return;
    Laby.fireEvent(new InventoryClickEvent(lvt_1_1_.inventory.getName(), lvt_1_1_.slotNumber, lvt_1_1_.getStack().getDisplayName()));
  }

}
