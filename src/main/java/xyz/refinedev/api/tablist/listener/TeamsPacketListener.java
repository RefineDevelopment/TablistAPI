package xyz.refinedev.api.tablist.listener;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;

import lombok.RequiredArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.util.PacketUtils;

import java.util.EnumSet;

/**
 * <p>
 * This class is essentially used to fix plugins adding/removing
 * entities via the player info packet for various reasons like Disguise or
 * Skin change. This intercepts those packets and makes them compatible with our tab.
 * </p>
 * <br>
 * <p>
 * This Project is property of Refine Development.<br>
 * Copyright © 2023, All Rights Reserved.<br>
 * Redistribution of this Project is not allowed.<br>
 * </p>
 *
 * @author DevScifi/DevDrizzy
 * @version TablistAPI
 * @since 10/15/2023
 */

@RequiredArgsConstructor
public class TeamsPacketListener extends PacketListenerAbstract {

    private final PacketEventsAPI<?> packetEvents;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO && event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            return;
        }

        ServerManager serverManager = packetEvents.getServerManager();
        boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        Player player = (Player) event.getPlayer();

        if (isClientNew && event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            WrapperPlayServerPlayerInfoUpdate infoUpdate = new WrapperPlayServerPlayerInfoUpdate(event);

            EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> action = infoUpdate.getActions();
            if (!action.contains(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER)) return;

            for ( WrapperPlayServerPlayerInfoUpdate.PlayerInfo info : infoUpdate.getEntries() ) {
                UserProfile userProfile = info.getGameProfile();
                if (userProfile == null) continue;

                this.preventGlitch(player, userProfile);
            }
        } else if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(event);
            WrapperPlayServerPlayerInfo.Action action = infoPacket.getAction();
            if (action != WrapperPlayServerPlayerInfo.Action.ADD_PLAYER) return;

            for ( WrapperPlayServerPlayerInfo.PlayerData data : infoPacket.getPlayerDataList() ) {
                UserProfile userProfile = data.getUserProfile();
                if (userProfile == null) continue;

                this.preventGlitch(player, userProfile);
            }
        }
    }


    /**
     * Prevents our tablist from glitching out and breaking
     *
     * @param player      {@link Player} Player
     * @param userProfile {@link UserProfile} Profile
     */
    private void preventGlitch(Player player, UserProfile userProfile) {
        ServerManager serverManager = packetEvents.getServerManager();
        boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        Player online = Bukkit.getPlayer(userProfile.getUUID());
        if (online == null) {
            return;
        }

        if (PacketUtils.isLegacyClient(player)) {
            Runnable wrapper = () -> {
                try {
                    PacketWrapper<?> removePacket;
                    if (isClientNew) {
                        removePacket = new WrapperPlayServerPlayerInfoRemove(userProfile.getUUID());
                    } else {
                        removePacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
                                new WrapperPlayServerPlayerInfo.PlayerData(null, userProfile, GameMode.SURVIVAL, -1));
                    }
                    PacketUtils.sendPacket(player, removePacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Bukkit.getScheduler().runTask(TablistHandler.getInstance().getPlugin(), wrapper);
            return;
        }

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("tab");

        if (team == null) {
            team = scoreboard.registerNewTeam("tab");
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                team.addEntry(otherPlayer.getName());
            }
        }

        team.addEntry(online.getName());
    }
}
