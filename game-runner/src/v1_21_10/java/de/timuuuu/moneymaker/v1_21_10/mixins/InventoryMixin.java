package de.timuuuu.moneymaker.v1_21_10.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.InventoryClickEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import net.labymod.api.Laby;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
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
  private void moneymaker$fireInventoryRender(GuiGraphics $$0, Slot $$1, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if($$1.getItem().get(DataComponents.CUSTOM_NAME) == null) return;
    String name = $$1.getItem().get(DataComponents.CUSTOM_NAME).getString();
    Laby.fireEvent(new InventoryRenderSlotEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString(), $$1.getContainerSlot(), name));
  }

  @Inject(
      method = {"slotClicked"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClick(Slot $$0, int $$1, int $$2, ClickType $$3, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if($$0 == null) return;
    if($$0.getItem().get(DataComponents.CUSTOM_NAME) == null) return;
    String name = $$0.getItem().get(DataComponents.CUSTOM_NAME).getString();
    Laby.fireEvent(new InventoryClickEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString(), $$0.getContainerSlot(), name));
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
