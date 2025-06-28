package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;

public class MoneyPacketEncryptionRequest extends MoneyPacket {

  private String serverId;
  private byte[] publicKey;
  private byte[] verifyToken;

  public MoneyPacketEncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
    this.serverId = serverId;
    this.publicKey = publicKey;
    this.verifyToken = verifyToken;
  }

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    this.serverId = packetBuffer.readString();
    this.publicKey = packetBuffer.readByteArray();
    this.verifyToken = packetBuffer.readByteArray();
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeString(this.serverId);
    packetBuffer.writeByteArray(this.publicKey);
    packetBuffer.writeByteArray(this.verifyToken);
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public String getServerId() {
    return serverId;
  }

  public byte[] getPublicKey() {
    return publicKey;
  }

  public byte[] getVerifyToken() {
    return verifyToken;
  }


}
