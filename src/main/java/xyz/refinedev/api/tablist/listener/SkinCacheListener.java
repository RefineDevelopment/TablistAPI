package xyz.refinedev.api.tablist.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.skin.SkinCache;

/**
 * <p>
 * This Project is property of Refine Development.<br>
 * Copyright © 2023, All Rights Reserved.<br>
 * Redistribution of this Project is not allowed.<br>
 * </p>
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 10/18/2023
 */

@RequiredArgsConstructor
public class SkinCacheListener implements Listener {

    private final TablistHandler instance;

    @EventHandler
    public void onLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        SkinCache cache = instance.getSkinCache();
        cache.registerCache(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.instance.getSkinCache().removeCache(player);
    }

}
