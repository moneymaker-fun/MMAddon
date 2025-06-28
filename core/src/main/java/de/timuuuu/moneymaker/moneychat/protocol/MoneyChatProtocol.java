package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketAddonStatistics;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketDisconnect;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionRequest;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionResponse;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketHelloPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketHelloPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketLoginComplete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketReport;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserMute;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserRankUpdate;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserUnmute;
import net.labymod.api.util.logging.Logging;
import java.util.HashMap;
import java.util.Map;

public class MoneyChatProtocol {

  private final Logging LOGGER = Logging.getLogger();

    private final Map<Integer, Class<? extends MoneyPacket>> packets = new HashMap<>();

    // Add Player Status Retrieve Packet

    public MoneyChatProtocol() {
      register(0, MoneyPacketHelloPing.class);
      register(1, MoneyPacketHelloPong.class);
      register(2, MoneyPacketLoginComplete.class);
      register(3, MoneyPacketPing.class);
      register(4, MoneyPacketPong.class);

      //register(5, PacketHandshake.class); // C -> S (idk ob es gebraucht wird)
      //register(6, PacketLogin.class); // C -> S
      register(7, MoneyPacketEncryptionRequest.class); // S -> C
      register(8, MoneyPacketEncryptionResponse.class); // C -> S
      register(9, PacketMessage.class); // S <-> C
      register(10, PacketMessageDelete.class); // S -> C
      register(11, PacketClearChat.class); // C <-> S
      register(12, PacketPlayerStatus.class); // C <-> S
      register(13, PacketReport.class); // C -> S
      register(14, PacketUserMute.class); // S -> C
      register(15, PacketUserUnmute.class); // S -> C
      register(16, PacketUserRankUpdate.class); // S -> C
      register(17, PacketAddonStatistics.class); // C -> S

      register(18, MoneyPacketDisconnect.class); // C <-> S
    }

    private void register(int id, Class<? extends MoneyPacket> clazz) {
        try {
            LOGGER.debug("Registering packet {} as id {}", clazz.getName(), id);
            packets.put(id, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MoneyPacket getPacket(int id) throws Exception {
        if (!packets.containsKey(id)) {
            throw new IllegalArgumentException("No packet with id " + id);
        } else {
            return this.packets.get(id).getConstructor().newInstance();
        }
    }

    public int getPacketId(MoneyPacket packet) {
        for (Map.Entry<Integer, Class<? extends MoneyPacket>> entry : packets.entrySet()) {
            Class<? extends MoneyPacket> value = entry.getValue();
            if (value.isInstance(packet)) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("Packet not found");
    }

}
