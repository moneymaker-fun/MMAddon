package de.timuuuu.moneymaker.snapshot;

import de.timuuuu.moneymaker.utils.AddonUtil;
import de.timuuuu.moneymaker.utils.MoneyPlayer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import org.jetbrains.annotations.Nullable;

public class MoneyPlayerSnapshot extends AbstractLabySnapshot {

  private MoneyPlayer moneyPlayer;

  public MoneyPlayerSnapshot(Player player, Extras extras) {
    super(extras);
    this.moneyPlayer = AddonUtil.playerStatus.get(player.getUniqueId());
  }

  public @Nullable MoneyPlayer getMoneyPlayer() {
    return moneyPlayer;
  }

}
