package xyz.refinedev.api.tablist.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import xyz.refinedev.api.tablist.TablistHandler;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 9/15/2023
 */

@UtilityClass
public class PacketUtils {

    /**
     * Checks what version the player is on and should we send the header/footer
     *
     * @param player {@link Player player}
     * @return       {@link Boolean should we send header/footer}
     */
    public boolean isLegacyClient(Player player) {
        ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
        return version.isOlderThan(ClientVersion.V_1_8);
    }

    /**
     * Checks what version the player is on and should we send display name
     *
     * @param player {@link Player player}
     * @return       {@link Boolean should we send display name}
     */
    public boolean isModernClient(Player player) {
        ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
        return version.isNewerThanOrEquals(ClientVersion.V_1_16);
    }

    /**
     * Checks what version the player is on and should we send display name
     *
     * @param player {@link Player player}
     * @return       {@link Boolean should we send display name}
     */
    public boolean isBrokenClient(Player player) {
        ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
        return version.isNewerThanOrEquals(ClientVersion.V_1_18);
    }

    public void sendPacket(Player target, PacketWrapper<?> packetWrapper) {
        PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
        PlayerManager manager = packetEvents.getPlayerManager();
        manager.sendPacket(target, packetWrapper);
    }
}
