package xyz.refinedev.api.tablist.listener;

import lombok.RequiredArgsConstructor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.setup.TabLayout;

@RequiredArgsConstructor
public class TabListener implements Listener {

    private final TablistHandler instance;


    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        TabLayout layout = new TabLayout(player);

        layout.create();
        layout.setHeaderAndFooter();

        this.instance.getLayoutMapping().put(player.getUniqueId(), layout);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.instance.getSkinCache().removeCache(player);
        this.instance.getLayoutMapping().remove(player.getUniqueId());
    }
}
