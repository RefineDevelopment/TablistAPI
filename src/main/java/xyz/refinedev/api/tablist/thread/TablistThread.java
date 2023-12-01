package xyz.refinedev.api.tablist.thread;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.setup.TabEntry;
import xyz.refinedev.api.tablist.setup.TabLayout;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 9/15/2023
 */

@Log4j2
@RequiredArgsConstructor
public class TablistThread extends BukkitRunnable {

    private final TablistHandler handler;

    @Override
    public void run() {
        if (!handler.getPlugin().isEnabled()) {
            this.cancel();
            return;
        }

        this.tick();
    }

    private void tick() {
        if (!handler.getPlugin().isEnabled()) return;

        for ( Player player : Bukkit.getOnlinePlayers() ) {
            TabLayout layout = this.handler.getLayoutMapping().get(player.getUniqueId());
            for ( TabEntry entry : handler.getAdapter().getLines(player) ) {
                final int x = entry.getX();
                final int y = entry.getY();
                final int i = y * layout.getMod() + x;

                try {
                    layout.update(i, entry.getText(), entry.getPing(), entry.getSkin());
                } catch (NullPointerException e) {
                    if (handler.getPlugin().getName().equals("Bolt") && !handler.isDebug()) {
                        continue;
                    }
                    log.fatal("[{}] There was an error updating tablist for {}", handler.getPlugin().getName(), player.getName());
                    log.error(e);
                    e.printStackTrace();
                } catch (Exception e) {
                    log.fatal("[{}] There was an error updating tablist for {}", handler.getPlugin().getName(), player.getName());
                    log.error(e);
                    e.printStackTrace();
                }
            }
        }
    }
}
