package de.timuuuu.moneymaker.utils;

public enum EventChatMessages {

  EVENT_EASTER_DE_1("[MoneyMaker] Du besitzt nun "),
  EVENT_EASTER_DE_2(" Ostereier"),
  EVENT_EASTER_EN_1("[MoneyMaker] You now have "),
  EVENT_EASTER_EN_2(" easter eggs"),

  EVENT_EASTER_BOOSTER_DE("[MoneyMaker] Du hast einen Booster im Wert von"),
  EVENT_EASTER_BOOSTER_EN("[MoneyMaker] You have earned a");

  private final String message;

  EventChatMessages(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }

  public boolean startWith(String text) {
    return text.startsWith(this.message);
  }

  public boolean contains(String text) {
    return text.contains(this.message);
  }

  public boolean equals(String text) {
    return text.equals(this.message);
  }

}
