package de.timuuuu.moneymaker.moneychat.protocol.packets;

import de.timuuuu.moneymaker.chat.MoneyChatMessage;
import de.timuuuu.moneymaker.enums.MoneyRank;
import de.timuuuu.moneymaker.moneychat.protocol.Packet;
import de.timuuuu.moneymaker.moneychat.protocol.PacketBuffer;
import de.timuuuu.moneymaker.moneychat.protocol.PacketHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PacketMessage extends Packet {

  private MoneyChatMessage message;

  public PacketMessage(MoneyChatMessage message) {
    this.message = message;
  }

  @Override
  public void read(PacketBuffer packetBuffer) {
    String messageId = packetBuffer.readString();
    UUID uuid = packetBuffer.readUUID();
    String username = packetBuffer.readString();
    String message = packetBuffer.readString();
    MoneyRank rank = MoneyRank.byName(packetBuffer.readString());
    String time = new SimpleDateFormat("dd.MM HH:mm").format(new Date());
    String addonVersion = packetBuffer.readString();
    String minecraftVersion = packetBuffer.readString();
    if(rank.isStaff()) {
      message = message.replace("&", "§");
    }
    this.message = new MoneyChatMessage(messageId, uuid, username, message, rank, false, time, addonVersion, minecraftVersion);
  }

  @Override
  public void write(PacketBuffer packetBuffer) {
    packetBuffer.writeString(this.message.messageId());
    packetBuffer.writeUUID(this.message.uuid());
    packetBuffer.writeString(this.message.userName());
    packetBuffer.writeString(this.message.message());
    packetBuffer.writeString(this.message.rank().getName());
    packetBuffer.writeString(this.message.timeStamp());
    packetBuffer.writeString(this.message.addonVersion());
    packetBuffer.writeString(this.message.minecraftVersion());
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public MoneyChatMessage message() {
    return message;
  }

}
