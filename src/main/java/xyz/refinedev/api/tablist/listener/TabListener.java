package xyz.refinedev.api.tablist.listener;

import lombok.RequiredArgsConstructor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.Team;

import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.setup.TabLayout;
import xyz.refinedev.api.tablist.util.PacketUtils;

@RequiredArgsConstructor
public class TabListener implements Listener {

    private final TablistHandler instance;


    @EventHandler(priority =  EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (instance.isIgnore1_7() && PacketUtils.isLegacyClient(player)) return;

        TabLayout layout = new TabLayout(player);

        layout.create();
        layout.setHeaderAndFooter();

        this.instance.getLayoutMapping().put(player.getUniqueId(), layout);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Team team = player.getScoreboard().getTeam("tab");
        if (team != null) {
            team.removeEntry(player.getName());
            team.unregister();
        }

        this.instance.getSkinCache().removeCache(player);
        this.instance.getLayoutMapping().remove(player.getUniqueId());
    }
}
