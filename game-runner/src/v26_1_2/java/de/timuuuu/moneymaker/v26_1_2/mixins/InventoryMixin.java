package de.timuuuu.moneymaker.v26_1_2.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.EventUtil.TextVersion;
import de.timuuuu.moneymaker.event.InventoryClickEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import de.timuuuu.moneymaker.event.InventoryRenderSlotEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.component.ItemLore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractContainerScreen.class})
public class InventoryMixin {

  @Inject(
      method = {"extractSlot"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryRender(GuiGraphicsExtractor $$0, Slot slot, int $$2, int $$3,
      CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(slot.getItem().get(DataComponents.CUSTOM_NAME) == null) return;
    String name = slot.getItem().get(DataComponents.CUSTOM_NAME).getString();
    List<String> loreList = new ArrayList<>();
    if(slot.getItem().get(DataComponents.LORE) == null) return;
    ItemLore itemLore = slot.getItem().get(DataComponents.LORE);
    for(int i = 0; i != itemLore.lines().size(); i++) {
      loreList.add(itemLore.lines().get(i).getString());
    }
    Laby.fireEvent(new InventoryRenderSlotEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString(), slot.getContainerSlot(), name, loreList, TextVersion.RAW));
  }

  @Inject(
      method = {"slotClicked"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClick(Slot slot, int slotId, int buttonNum,
      ContainerInput containerInput, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(slot == null) return;
    if(slot.getItem().get(DataComponents.CUSTOM_NAME) == null) return;
    String name = slot.getItem().get(DataComponents.CUSTOM_NAME).getString();
    List<String> loreList = new ArrayList<>();
    if(slot.getItem().get(DataComponents.LORE) == null) return;
    ItemLore itemLore = slot.getItem().get(DataComponents.LORE);
    for(int i = 0; i != itemLore.lines().size(); i++) {
      loreList.add(itemLore.lines().get(i).getString());
    }
    Laby.fireEvent(new InventoryClickEvent(((AbstractContainerScreen<?>) (Object) this).getTitle().getString(), slot.getContainerSlot(), name, loreList, TextVersion.RAW));
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
