package de.timuuuu.moneymaker.enums;

public enum ChatMessages {

  SB_BALANCE_DE("Kontostand"),
  SB_BALANCE_EN("Balance"),
  SB_PLACE_DE("Platz"),
  SB_PLACE_EN("Rank"),

  PREFIX("[MoneyMaker]"),
  WORKPLACE_UPGRADE_DE_1("[MoneyMaker] Du hast den Arbeitsplatz auf Level"),
  WORKPLACE_UPGRADE_DE_2("verbessert"),
  BUY_WORKER_DE_1("[MoneyMaker] Du hast einen "),
  BUY_WORKER_DE_2(" gekauft"),
  SELL_WORKER_DE_1("[MoneyMaker] Du hast den "),
  SELL_WORKER_DE_2(" verkauft"),
  TELEPORT_DE_1("[MoneyMaker] Du wurdest"),
  TELEPORT_DE_2("teleportiert"),
  BOOSTER_FOUND_DE("[MoneyMaker] Glückwunsch! Du hast einen Booster gefunden:"),
  WORKPLACE_UNLOCKED_DE("[MoneyMaker] Der Arbeitsplatz wurde erfolgreich freigeschaltet"),
  DEBRIS_REMOVE_DE_1("[MoneyMaker] Das Geröll wird in"),
  DEBRIS_REMOVE_DE_2("entfernt"),
  WORKER_EFFECT_DE("[MoneyMaker] Du hast den Effekt dieses Arbeiters aktiviert"),
  BOOSTER_INVENTORY_DE("[MoneyMaker] Dein Booster-Inventar hat das Limit von"),

  BOOSTER_ACTIVATED_DE_1("[MoneyMaker] Dein +"),
  BOOSTER_ACTIVATED_DE_2(" % Booster "),
  BOOSTER_ACTIVATED_DE_3(" wurde aktiviert"),



  WORKPLACE_UPGRADE_EN("[MoneyMaker] You have upgraded the workplace to level"),
  BUY_WORKER_EN("[MoneyMaker] You have purchased a "),
  SELL_WORKER_EN("[MoneyMaker] You have sold "),
  TELEPORT_EN("[MoneyMaker] You were teleported to"),
  BOOSTER_FOUND_EN("[MoneyMaker] Congratulations! You have found a booster:"),
  WORKPLACE_UNLOCKED_EN("[MoneyMaker] The workplace was successfully unlocked"),
  DEBRIS_REMOVE_EN("[MoneyMaker] Debris will be removed in"),
  WORKER_EFFECT_EN("[MoneyMaker] You have activated the effect of this worker"),
  BOOSTER_INVENTORY_EN("[MoneyMaker] Your booster inventory has reached the limit of"),
  BOOSTER_ACTIVATED_EN_1("[MoneyMaker] Your +"),
  BOOSTER_ACTIVATED_EN_2("% booster "),
  BOOSTER_ACTIVATED_EN_3(" was activated"),

  PARTING_LINE("[MoneyMaker] eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

  private final String message;

  ChatMessages(String message) {
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
