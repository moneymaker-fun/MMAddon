package de.timuuuu.moneymaker.v1_20_1.mixins;

import de.timuuuu.moneymaker.event.InventoryClickEvent;
import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import net.labymod.api.Laby;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

@Mixin({AbstractContainerScreen.class})
public class InventoryMixin {

  @Inject(
      method = {"renderSlot"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryRender(GuiGraphics $$0, Slot slot, CallbackInfo ci) {
    CompoundTag compoundTag = slot.getItem().getOrCreateTagElement("display");
    String name = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
    List<String> loreList = new ArrayList<>();
    ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
    for(int i = 0; i != listTag.size(); i++) {
      loreList.add(listTag.getString(i));
    }
    AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
    Laby.fireEvent(new InventoryRenderSlotEvent(screen.getTitle().getString(), slot.getContainerSlot(), name, loreList, "1.20"));
  }

  @Inject(
      method = {"slotClicked"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClick(Slot clickedSlot, int $$1, int $$2, ClickType $$3, CallbackInfo ci) {
    if(clickedSlot == null) return;
    CompoundTag compoundTag = clickedSlot.getItem().getOrCreateTagElement("display");
    String itemName = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
    List<String> loreList = new ArrayList<>();
    ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
    for(int i = 0; i != listTag.size(); i++) {
      loreList.add(listTag.getString(i));
    }
    AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
    Laby.fireEvent(new InventoryClickEvent(screen.getTitle().getString(), clickedSlot.getContainerSlot(), itemName, loreList, "1.20"));
  }

  @Inject(
      method = {"removed"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClose(CallbackInfo ci) {
    AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
    Laby.fireEvent(new InventoryCloseEvent(screen.getTitle().getString()));
  }

}
