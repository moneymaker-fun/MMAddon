package de.timuuuu.moneymaker.moneychat.protocol;

public abstract class Packet {

  public abstract void read(PacketBuffer packetBuffer);

  public abstract void write(PacketBuffer packetBuffer);

  public abstract void handle(PacketHandler packetHandler);

}
