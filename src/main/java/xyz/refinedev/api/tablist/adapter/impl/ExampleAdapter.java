package xyz.refinedev.api.tablist.adapter.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import xyz.refinedev.api.tablist.adapter.TabAdapter;
import xyz.refinedev.api.tablist.setup.TabEntry;
import xyz.refinedev.api.tablist.util.Skin;
import xyz.refinedev.api.tablist.util.StringUtils;

import java.util.List;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @since 4/9/2022
 * @version TablistAPI
 */

public class ExampleAdapter implements TabAdapter {

    /**
     * Get the tab header for a player.
     *
     * @param player the player
     * @return string
     */
    @Override
    public String getHeader(Player player) {
        return "&cRefine Development";
    }

    /**
     * Get the tab player for a player.
     *
     * @param player the player
     * @return string
     */
    @Override
    public String getFooter(Player player) {
        return "&ediscord.refinedev.xyz";
    }

    /**
     * Get the tab lines for a player.
     *
     * @param player the player
     * @return list of entries
     */
    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> entries = new ObjectArrayList<>();

        for ( int i = 0; i < 4; i++ ) {
            int ping = StringUtils.MINOR_VERSION > 12 ? player.getPing() : 69;
            Skin skin = player.isSprinting() ? Skin.getPlayer(player) : Skin.YOUTUBE_SKIN;
            TabEntry tabEntry = new TabEntry(i, 0, "&#FF0000Sprinting " + player.isSprinting() + "(" + i + ")", ping, skin);
            entries.add(tabEntry);
        }

        return entries;
    }
}
