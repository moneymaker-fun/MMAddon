package de.timuuuu.moneymaker.v1_21_5.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.timuuuu.moneymaker.MoneyMakerAddon;
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
      method = {"render*"},
      at = {@At("HEAD")}
  )
  private void moneymaker$fireArmorStandRender(Entity $$0, double $$1, double $$2, double $$3, float $$4,
      PoseStack $$5, MultiBufferSource $$6, int $$7, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if($$0.getType() == EntityType.ARMOR_STAND) {
      if($$0.hasCustomName() && $$0.getCustomName() != null) {
        String customName = $$0.getCustomName().getString();
        Laby.fireEvent(new ArmorStandRenderEvent((net.labymod.api.client.entity.Entity) $$0, customName));
      }
    }
  }

}
