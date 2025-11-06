package de.timuuuu.moneymaker.snapshot;

import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class MoneyPlayerSnapshotFactory extends LabySnapshotFactory<Player, MoneyPlayerSnapshot> {

  public MoneyPlayerSnapshotFactory() {
    super(MoneyMakerKeys.MONEY_PLAYER);
  }

  @Override
  protected MoneyPlayerSnapshot create(Player player, Extras extras) {
    return new MoneyPlayerSnapshot(player, extras);
  }

}
