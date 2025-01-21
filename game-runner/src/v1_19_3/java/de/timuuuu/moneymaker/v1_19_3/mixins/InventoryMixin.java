package de.timuuuu.moneymaker.v1_19_3.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import de.timuuuu.moneymaker.event.InventoryClickEvent;
import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
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

@Mixin({AbstractContainerScreen.class})
public class InventoryMixin {

  @Inject(
      method = {"renderSlot"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryRender(PoseStack $$0, Slot slot, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    CompoundTag compoundTag = slot.getItem().getOrCreateTagElement("display");
    String name = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
    List<String> loreList = new ArrayList<>();
    ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
    for(int i = 0; i != listTag.size(); i++) {
      loreList.add(listTag.getString(i));
    }
    Laby.fireEvent(new InventoryRenderSlotEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString(), slot.getContainerSlot(), name, loreList, TextVersion.JSON));
  }

  @Inject(
      method = {"slotClicked"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClick(Slot clickedSlot, int $$1, int $$2, ClickType $$3, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(clickedSlot == null) return;
    CompoundTag compoundTag = clickedSlot.getItem().getOrCreateTagElement("display");
    String itemName = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
    List<String> loreList = new ArrayList<>();
    ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
    for(int i = 0; i != listTag.size(); i++) {
      loreList.add(listTag.getString(i));
    }
    Laby.fireEvent(new InventoryClickEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString(), clickedSlot.getContainerSlot(), itemName, loreList, TextVersion.JSON));
  }

  @Inject(
      method = {"removed"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClose(CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    Laby.fireEvent(new InventoryCloseEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString()));
  }

}
