package de.timuuuu.moneymaker.events;

import net.labymod.api.event.Event;

public class ProfileSwitchEvent implements Event {

  private String oldProfile;
  private String newProfile;

  public ProfileSwitchEvent(String oldProfile, String newProfile) {
    this.oldProfile = oldProfile;
    this.newProfile = newProfile;
  }

  public String oldProfile() {
    return oldProfile;
  }

  public String newProfile() {
    return newProfile;
  }

}
