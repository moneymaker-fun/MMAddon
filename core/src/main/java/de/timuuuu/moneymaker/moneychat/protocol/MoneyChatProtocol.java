package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketAddonStatistics;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketEncryptionRequest;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketEncryptionResponse;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketHelloPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketHelloPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketLoginComplete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketReport;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserMute;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserRankUpdate;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketUserUnmute;
import net.labymod.api.util.logging.Logging;
import java.util.HashMap;
import java.util.Map;

public class MoneyChatProtocol {

  private final Logging LOGGER = Logging.getLogger();

    private final Map<Integer, Class<? extends Packet>> packets = new HashMap<>();

    // Add Player Status Retrieve Packet

    public MoneyChatProtocol() {
      register(0, PacketHelloPing.class);
      register(1, PacketHelloPong.class);
      register(2, PacketLoginComplete.class);
      register(3, PacketPing.class);
      register(4, PacketPong.class);

      register(5, PacketHandshake.class); // C -> S (idk ob es gebraucht wird)
      register(6, PacketLogin.class); // C -> S
      register(7, PacketEncryptionRequest.class); // S -> C
      register(8, PacketEncryptionResponse.class); // C -> S
      register(9, PacketMessage.class); // S <-> C
      register(10, PacketMessageDelete.class); // S -> C
      register(11, PacketClearChat.class); // C <-> S
      register(12, PacketPlayerStatus.class); // C <-> S
      register(13, PacketReport.class); // C -> S
      register(14, PacketUserMute.class); // S -> C
      register(15, PacketUserUnmute.class); // S -> C
      register(16, PacketUserRankUpdate.class); // S -> C
      register(17, PacketAddonStatistics.class); // C -> S
    }

    private void register(int id, Class<? extends Packet> clazz) {
        try {
            LOGGER.debug("Registering packet {} as id {}", clazz.getName(), id);
            packets.put(id, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Packet getPacket(int id) throws Exception {
        if (!packets.containsKey(id)) {
            throw new IllegalArgumentException("No packet with id " + id);
        } else {
            return this.packets.get(id).getConstructor().newInstance();
        }
    }

    public int getPacketId(Packet packet) {
        for (Map.Entry<Integer, Class<? extends Packet>> entry : packets.entrySet()) {
            Class<? extends Packet> value = entry.getValue();
            if (value.isInstance(packet)) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("Packet not found");
    }

}
