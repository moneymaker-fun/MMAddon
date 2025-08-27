package de.timuuuu.moneymaker.moneychat.protocol;

import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketAddonMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPing;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketPong;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketAddonStatistics;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketClearChat;
import de.timuuuu.moneymaker.moneychat.protocol.packets.MoneyPacketDisconnect;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketLeaderboard;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketVerificationToken;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionRequest;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketEncryptionResponse;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketLogin;
import de.timuuuu.moneymaker.moneychat.protocol.packets.auth.MoneyPacketLoginComplete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessage;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketMessageDelete;
import de.timuuuu.moneymaker.moneychat.protocol.packets.PacketPlayerStatus;
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

    public MoneyChatProtocol() {
      register(0, MoneyPacketLogin.class); // C -> S
      register(1, MoneyPacketEncryptionRequest.class); // S -> C
      register(2, MoneyPacketEncryptionResponse.class); // C -> S
      register(3, MoneyPacketLoginComplete.class); // S -> C
      register(4, PacketMessage.class); // S <-> C
      register(5, PacketMessageDelete.class); // S -> C
      register(6, PacketClearChat.class); // C <-> S
      register(7, PacketPlayerStatus.class); // C <-> S
      register(8, PacketReport.class); // C -> S
      register(9, PacketUserMute.class); // S -> C
      register(10, PacketUserUnmute.class); // S -> C
      register(11, PacketUserRankUpdate.class); // S -> C
      register(12, PacketAddonStatistics.class); // C -> S
      register(13, MoneyPacketDisconnect.class); // C <-> S

      register(14, MoneyPacketPing.class); // S -> C
      register(15, MoneyPacketPong.class); // C -> S

      register(16, PacketLeaderboard.class); // C -> S

      register(20, MoneyPacketAddonMessage.class); // C <-> S

      register(30, PacketVerificationToken.class); // S -> C
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
            throw new RuntimeException("Packet with id " + id + " is not registered.");
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

        throw new RuntimeException("Packet " + packet + " is not registered.");
    }

}
