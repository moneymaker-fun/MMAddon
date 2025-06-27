package de.timuuuu.moneymaker.moneychat.protocol.packets.auth;

import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;

public class PacketEncryptionRequest extends Packet {

  private String serverId;
  private byte[] publicKey;
  private byte[] verifyToken;

  public PacketEncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
    this.serverId = serverId;
    this.publicKey = publicKey;
    this.verifyToken = verifyToken;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    this.serverId = packetBuffer.readString();
    this.publicKey = packetBuffer.readByteArray();
    this.verifyToken = packetBuffer.readByteArray();
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(this.serverId);
    packetBuffer.writeByteArray(this.publicKey);
    packetBuffer.writeByteArray(this.verifyToken);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
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
