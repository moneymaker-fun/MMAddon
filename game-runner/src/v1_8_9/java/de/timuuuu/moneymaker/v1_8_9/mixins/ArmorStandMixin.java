package de.timuuuu.moneymaker.v1_8_9.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.ArmorStandRenderEvent;
import net.labymod.api.Laby;
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
  private void moneymaker$fireArmorStandRender(Entity lvt_1_1_, double lvt_2_1_, double lvt_4_1_, double lvt_6_1_, float lvt_8_1_, float lvt_9_1_, CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    if(lvt_1_1_ instanceof EntityArmorStand) {
      if(lvt_1_1_.hasCustomName() && lvt_1_1_.getCustomNameTag() != null) {
        String customName = lvt_1_1_.getCustomNameTag();
        Laby.fireEvent(new ArmorStandRenderEvent((net.labymod.api.client.entity.Entity) lvt_1_1_, customName));
      }
    }
  }

}
