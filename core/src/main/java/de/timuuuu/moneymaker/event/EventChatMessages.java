package de.timuuuu.moneymaker.event;

public enum EventChatMessages {

  // Valentine

  // Easter

  NOW_DE("[MoneyMaker] Du besitzt nun "),
  NOW_EN("[MoneyMaker] You now have "),

  BOOSTER_DE("[MoneyMaker] Du hast einen Booster im Wert von"),
  BOOSTER_EN("[MoneyMaker] You have earned a"),


  VALENTINE_DE(" Blumen"),
  VALENTINE_EN(" flowers"),

  EASTER_DE(" Ostereier"),
  EASTER_EN(" easter eggs"),

  // Summer

  SUMMER_DE(" Früchte"),
  SUMMER_EN(" fruits"),

  // Halloween

  HALLOWEEN_DE(" Süßigkeiten"),
  HALLOWEEN_EN(" candies"),

  CHRISTMAS_DE(" Geschenke"),
  CHRISTMAS_EN(" gifts");

  // Christmas

    /*

    easter-egg-plural

    %prefix% §aYou have found an §eeaster egg§a!
    %prefix% §aYou now have §e{0} easter eggs

    %prefix% §aDu hast ein §eOsterei §agefunden!
    %prefix% §aDu besitzt nun §e{0} Ostereier


    caribbean-fruit-plural

    %prefix% §aYou have found a §efruit§a!
    %prefix% §aYou now have §e{0} fruits

    %prefix% §aDu hast eine §eFrucht §agefunden!
    %prefix% §aDu besitzt nun §e{0} Früchte

    halloween-candy-plural

    %prefix% §aYou have found a §ecandy§a!
    %prefix% §aYou now have §e{0} candies

    %prefix% §aDu hast eine §eSüßigkeit §agefunden!
    %prefix% §aDu besitzt nun Â§e{0} Süßigkeiten

    valentine-flower-plural

    %prefix% §aYou have found a §eflower§a!
    %prefix% §aYou now have §e{0} flowers

    %prefix% §aDu hast eine §eBlume §agefunden!
    %prefix% §aDu besitzt nun §e{0} Blumen

    summer-fruit-plural

    %prefix% §aYou have found a §efruit§a!
    %prefix% §aYou now have §e{0} fruits

    %prefix% §aDu hast eine §eFrucht §agefunden!
    %prefix% §aDu besitzt nun §e{0} Früchte

    christmas-fruit-plural

    %prefix% §aYou have found a §egift§a!
    %prefix% §aYou now have §e{0} gifts

     */

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
