package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.group.Group;
import de.timuuuu.moneymaker.group.GroupService;
import de.timuuuu.moneymaker.moneychat.util.MoneyChatMessage;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacket;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.MoneyPacketHandler;
import java.util.UUID;

public class PacketMessage extends MoneyPacket {

  private MoneyChatMessage message;

  public PacketMessage(MoneyChatMessage message) {
    this.message = message;
  }

  public PacketMessage() {}

  @Override
  public void read(MoneyPacketBuffer packetBuffer) {
    String messageId = packetBuffer.readString();
    UUID uuid = packetBuffer.readUUID();
    String username = packetBuffer.readString();
    String message = packetBuffer.readString();
    Group group = GroupService.getGroup(packetBuffer.readString());
    String time = packetBuffer.readString();
    String addonVersion = packetBuffer.readString();
    String minecraftVersion = packetBuffer.readString();
    if(group.isStaff()) {
      message = message.replace("&", "§");
    }
    this.message = new MoneyChatMessage(messageId, uuid, username, message, group, false, time, addonVersion, minecraftVersion);
  }

  @Override
  public void write(MoneyPacketBuffer packetBuffer) {
    packetBuffer.writeString(this.message.messageId());
    packetBuffer.writeUUID(this.message.uuid());
    packetBuffer.writeString(this.message.userName());
    packetBuffer.writeString(this.message.message());
    packetBuffer.writeString(this.message.group().getName());
    packetBuffer.writeString(this.message.timeStamp());
    packetBuffer.writeString(this.message.addonVersion());
    packetBuffer.writeString(this.message.minecraftVersion());
  }

  @Override
  public void handle(MoneyPacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public MoneyChatMessage message() {
    return message;
  }

}
