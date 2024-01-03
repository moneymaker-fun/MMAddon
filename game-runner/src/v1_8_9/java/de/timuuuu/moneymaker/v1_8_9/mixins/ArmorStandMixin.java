package de.timuuuu.moneymaker.v1_8_9.mixins;

import de.timuuuu.moneymaker.event.ArmorStandRenderEvent;
import net.labymod.api.Laby;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Render.class})
public class ArmorStandMixin {

  @Inject(
      method = {"doRender"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireArmorStandRender(Entity p_doRender_1_, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_, CallbackInfo ci) {
    if(p_doRender_1_ instanceof EntityArmorStand) {
      if(p_doRender_1_.hasCustomName() && p_doRender_1_.getCustomNameTag() != null) {
        String customName = p_doRender_1_.getCustomNameTag();
        Laby.fireEvent(new ArmorStandRenderEvent((net.labymod.api.client.entity.Entity) p_doRender_1_, customName));
      }
    }
  }

}
