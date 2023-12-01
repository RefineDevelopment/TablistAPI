package xyz.refinedev.api.tablist.adapter;

import org.bukkit.entity.Player;
import xyz.refinedev.api.tablist.setup.TabEntry;

import java.util.List;

public interface TabAdapter {

    /**
     * Get the tab header for a player.
     *
     * @param player the player
     * @return string
     */
    String getHeader(Player player);

    /**
     * Get the tab player for a player.
     *
     * @param player the player
     * @return string
     */
    String getFooter(Player player);

    /**
     * Get the tab lines for a player.
     *
     * @param player the player
     * @return list of entries
     */
    List<TabEntry> getLines(Player player);
}
