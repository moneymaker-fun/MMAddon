package de.timuuuu.moneymaker.v1_20_4.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.SwordTickEvent;
import java.util.ArrayList;
import java.util.List;
import de.timuuuu.moneymaker.event.SwordTickEvent.TextVersions;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class SwordTickMixin {

  @Inject(
      method = {"tick()V"},
      at = @At("HEAD")
  )
  private void moneymaker$tick(CallbackInfo ci) {
    if(!MoneyMakerAddon.instance().addonUtil().connectedToMoneyMaker()) return;
    Player player = Minecraft.getInstance().player;
    if(player != null) {
      ItemStack itemStack = player.getInventory().getItem(0);
      if(itemStack != ItemStack.EMPTY) {
        CompoundTag compoundTag = itemStack.getOrCreateTagElement("display");
        String name = compoundTag.getString(ItemStack.TAG_DISPLAY_NAME);
        List<String> loreList = new ArrayList<>();
        ListTag listTag = compoundTag.getList(ItemStack.TAG_LORE, 8);
        for(int i = 0; i != listTag.size(); i++) {
          loreList.add(listTag.getString(i));
        }
        Laby.fireEvent(new SwordTickEvent(name, loreList, TextVersions.JSON));
      }
    }
  }

}
