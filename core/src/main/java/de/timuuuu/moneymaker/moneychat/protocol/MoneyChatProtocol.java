package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import net.labymod.api.util.logging.Logging;
import java.util.HashMap;
import java.util.Map;

public class MoneyChatProtocol {

  private final Logging LOGGER = Logging.getLogger();

    private final Map<Integer, Class<? extends Packet>> packets = new HashMap<>();

    // Add Player Status Retrieve Packet

    public MoneyChatProtocol() {
        register(1, PacketHandshake.class); // C -> S (idk ob es gebraucht wird)
        register(2, PacketLogin.class); // C -> S
        register(3, PacketEncryptionRequest.class); // S -> C
        register(4, PacketEncryptionResponse.class); // C -> S
        register(11, PacketMessage.class); // S <-> C
        register(12, PacketMessageDelete.class); // S -> C
        register(13, PacketClearChat.class); // C <-> S
        register(14, PacketPlayerStatus.class); // C <-> S
        register(21, PacketReport.class); // C -> S
        register(22, PacketUserMute.class); // S -> C
        register(23, PacketUserUnmute.class); // S -> C
        register(24, PacketUserRankUpdate.class); // S -> C
        register(25, PacketAddonStatistics.class); // C -> S
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
