package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketDisconnect extends MoneyPacket {

  private String reason;

  public MoneyPacketDisconnect(String reason) {
    this.reason = reason;
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.reason = packetBuffer.readString();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeString(reason);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String reason() {
    return reason;
  }

}
