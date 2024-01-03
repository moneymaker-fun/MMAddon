package de.timuuuu.moneymaker.v1_18_2.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.timuuuu.moneymaker.event.ArmorStandRenderEvent;
import net.labymod.api.Laby;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderDispatcher.class})
public class ArmorStandMixin {

  @Inject(
      method = {"render"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireArmorStandRender(Entity param0, double param1, double param2, double param3, float param4, float param5, PoseStack param6, MultiBufferSource param7, int param8, CallbackInfo ci) {
    if(param0.getType() == EntityType.ARMOR_STAND) {
      if(param0.hasCustomName() && param0.getCustomName() != null) {
        String customName = param0.getCustomName().getString();
        Laby.fireEvent(new ArmorStandRenderEvent((net.labymod.api.client.entity.Entity) param0, customName));
      }
    }
  }

}
