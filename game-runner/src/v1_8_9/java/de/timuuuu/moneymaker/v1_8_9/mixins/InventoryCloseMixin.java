package de.timuuuu.moneymaker.v1_8_9.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.InventoryCloseEvent;
import net.labymod.api.Laby;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ContainerChest.class})
public abstract class InventoryCloseMixin {

  @Shadow
  public abstract IInventory getLowerChestInventory();

  @Inject(
      method = {"onContainerClosed"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireInventoryClose(CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    Laby.fireEvent(new InventoryCloseEvent(getLowerChestInventory().getName()));
  }

}
