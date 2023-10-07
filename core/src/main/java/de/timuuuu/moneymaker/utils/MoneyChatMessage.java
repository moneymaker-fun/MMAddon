package de.timuuuu.moneymaker.utils;

import java.util.UUID;

public class MoneyChatMessage {

  private String time;
  private UUID uuid;
  private String userName;
  private String message;

  public MoneyChatMessage(String time, UUID uuid, String userName, String message) {
    this.time = time;
    this.uuid = uuid;
    this.userName = userName;
    this.message = message;
  }

  public String time() {
    return time;
  }

  public void time(String time) {
    this.time = time;
  }

  public UUID uuid() {
    return uuid;
  }

  public void uuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String userName() {
    return userName;
  }

  public void userName(String userName) {
    this.userName = userName;
  }

  public String message() {
    return message;
  }

  public void message(String message) {
    this.message = message;
  }
}
