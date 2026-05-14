package de.timuuuu.moneymaker.v26_1_2.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.ArmorStandRenderEvent;
import net.labymod.api.Laby;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ArmorStandRenderer.class})
public class ArmorStandMixin {

  @Inject(
      method = {"extractRenderState(Lnet/minecraft/world/entity/decoration/ArmorStand;Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;F)V"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireArmorStandRender(ArmorStand entity, ArmorStandRenderState state, float partialTicks, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(entity.hasCustomName() && entity.getCustomName() != null) {
      String customName = entity.getCustomName().getString();
      Laby.fireEvent(new ArmorStandRenderEvent((net.labymod.api.client.entity.Entity) entity, customName));
    }
  }

}
