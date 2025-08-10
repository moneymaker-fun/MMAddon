package de.timuuuu.moneymaker.moneychat.protocol;

public abstract class MoneyPacket {

  public abstract void read(MoneyPacketBuffer packetBuffer);

  public abstract void write(MoneyPacketBuffer packetBuffer);

  public abstract void handle(MoneyPacketHandler packetHandler);

}
