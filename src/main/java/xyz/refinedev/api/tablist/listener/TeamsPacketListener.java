package xyz.refinedev.api.tablist.listener;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.util.GlitchFixEvent;
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
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO && event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE && event.getPacketType() != PacketType.Play.Server.TEAMS) {
            return;
        }

        ServerManager serverManager = packetEvents.getServerManager();
        boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);

        Player player = (Player) event.getPlayer();
        TablistHandler tablistHandler = TablistHandler.getInstance();
        if (player == null || (tablistHandler.isIgnore1_7() && PacketUtils.isLegacyClient(player))) return;

        /*if (event.getPacketType() == PacketType.Play.Server.TEAMS) {
            WrapperPlayServerTeams teams = new WrapperPlayServerTeams(event);
            if (teams.getTeamMode() != WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) return;

            if (!teams.getTeamName().equals("rtab")) {
                teams.setTeamMode(WrapperPlayServerTeams.TeamMode.ADD_ENTITIES);
                teams.setTeamName("tab");

                Optional<WrapperPlayServerTeams.ScoreBoardTeamInfo> teamInfo = teams.getTeamInfo();
                if (teamInfo.isPresent()) {
                    WrapperPlayServerTeams.ScoreBoardTeamInfo info = teamInfo.get();
                    info.setDisplayName(Component.text("rtab"));
                }
            }
        } else */if (isClientNew && event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
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
        if (player == null) return;

        Player online = Bukkit.getPlayer(userProfile.getUUID());
        if (online == null) {
            return;
        }

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("rtab");

        if (team == null) {
            team = scoreboard.registerNewTeam("rtab");
        }

        if (!team.hasEntry(online.getName())) {
            team.addEntry(online.getName());
        }

        GlitchFixEvent glitchFixEvent = new GlitchFixEvent(player);
        if (TablistHandler.getInstance().getPlugin().isEnabled()) {
            Bukkit.getScheduler().runTask(TablistHandler.getInstance().getPlugin(), () -> Bukkit.getPluginManager().callEvent(glitchFixEvent));
        }
    }
}
