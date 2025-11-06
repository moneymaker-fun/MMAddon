package de.timuuuu.moneymaker.snapshot;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.render.state.entity.EntitySnapshotProcessor;
import net.labymod.api.client.render.state.entity.EntitySnapshotRegistry;
import net.labymod.api.laby3d.renderer.snapshot.ExtrasWriter;
import net.labymod.api.service.annotation.AutoService;

@AutoService(EntitySnapshotProcessor.class)
public class MoneyPlayerSnapshotProcessor extends EntitySnapshotProcessor<Player> {

  public MoneyPlayerSnapshotProcessor(EntitySnapshotRegistry registry) {
    super(registry);
  }

  @Override
  public boolean supports(Entity entity) {
    return entity instanceof Player;
  }

  @Override
  public void process(Player player, float partialTicks, ExtrasWriter entityWriter) {
    this.registry().captureSnapshot(entityWriter, MoneyMakerKeys.MONEY_PLAYER, player);
  }

}
