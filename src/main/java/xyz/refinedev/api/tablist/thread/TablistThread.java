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
public class TablistThread extends Thread {

    private final TablistHandler handler;
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            if (!handler.getPlugin().isEnabled()) {
                this.terminate();
                return;
            }
            this.tick();

            try {
                Thread.sleep(handler.getTicks() * 50L);
            } catch (InterruptedException e) {
                //
            }
        }
    }

    public void terminate() {
        this.running = false;
    }

    private void tick() {
        if (!handler.getPlugin().isEnabled()) return;

        for ( TabLayout layout : handler.getLayoutMapping().values() ) {
            layout.refresh();
        }
    }
}
