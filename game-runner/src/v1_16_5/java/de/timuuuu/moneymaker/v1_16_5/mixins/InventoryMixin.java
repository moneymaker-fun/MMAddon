package de.timuuuu.moneymaker.v1_16_5.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.timuuuu.moneymaker.event.BoosterInventoryRenderSlotEvent;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractContainerScreen.class})
public abstract class InventoryMixin {

  @Inject(
      method = {"renderSlot"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryRender(PoseStack lvt_1_1_, Slot slot, CallbackInfo ci) {
    if(slot.getItem().getItem() == Items.PLAYER_HEAD) {
      CompoundTag compoundTag = slot.getItem().getOrCreateTagElement("display");
      String name = compoundTag.getString("Name");
      List<String> loreList = new ArrayList<>();
      ListTag listTag = compoundTag.getList("Lore", 8);
      for(int i = 0; i != listTag.size(); i++) {
        loreList.add(listTag.getString(i));
      }
      AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
      Laby.fireEvent(new BoosterInventoryRenderSlotEvent(screen.getTitle().getString(), 0, name, loreList, "1.16"));
    }
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
