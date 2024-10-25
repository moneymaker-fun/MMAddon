package de.timuuuu.moneymaker.v1_21_3.mixins;

import de.timuuuu.moneymaker.MoneyMakerAddon;
import de.timuuuu.moneymaker.event.SwordTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
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
    if(player == null) return;
    ItemStack itemStack = player.getInventory().getItem(0);
    if(itemStack == ItemStack.EMPTY) return;
    if(itemStack.get(DataComponents.CUSTOM_NAME) == null) return;
    String name = itemStack.get(DataComponents.CUSTOM_NAME).getString();
    List<String> loreList = new ArrayList<>();
    if(itemStack.get(DataComponents.LORE) == null) return;
    ItemLore itemLore = itemStack.get(DataComponents.LORE);
    for(int i = 0; i != itemLore.lines().size(); i++) {
      loreList.add(itemLore.lines().get(i).getString());
    }
    Laby.fireEvent(new SwordTickEvent(name, loreList, "1.21.3"));
  }

}
