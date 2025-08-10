package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class MoneyPacketLogin extends MoneyPacket {

  private String username;
  private UUID uuid;

  public MoneyPacketLogin(String username, UUID uuid) {
    this.username = username;
    this.uuid = uuid;
  }

  public MoneyPacketLogin() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    username = packetBuffer.readString();
    uuid = packetBuffer.readUUID();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeString(username);
    packetBuffer.writeUUID(uuid);
  }

  @Override
  public void handle(MoneyPacketHandler packet) {}

}
