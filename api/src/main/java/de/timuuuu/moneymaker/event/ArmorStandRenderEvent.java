package de.timuuuu.moneymaker.event;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.event.Event;

public class ArmorStandRenderEvent implements Event {

  private Entity entity;
  private String customName;

  public ArmorStandRenderEvent(Entity entity, String customName) {
    this.entity = entity;
    this.customName = customName;
  }

  public Entity entity() {
    return entity;
  }

  public String customName() {
    return customName;
  }

}
