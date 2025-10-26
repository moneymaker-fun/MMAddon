package de.timuuuu.moneymaker.event;

public class EventUtil {

  public enum TextVersion {
    RAW, JSON
  }

  public enum Item {
    SWORD(0),
    PICKAXE(1);

    private final int slotNumber;

    Item(int slotNumber) {
      this.slotNumber = slotNumber;
    }

    public int slotNumber() {
      return slotNumber;
    }

  }

}
