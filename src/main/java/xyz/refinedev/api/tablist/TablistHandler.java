package xyz.refinedev.api.tablist;

import com.github.retrooper.packetevents.PacketEventsAPI;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.refinedev.api.tablist.adapter.TabAdapter;
import xyz.refinedev.api.tablist.adapter.impl.ExampleAdapter;
import xyz.refinedev.api.tablist.listener.SkinCacheListener;
import xyz.refinedev.api.tablist.listener.TabListener;
import xyz.refinedev.api.tablist.listener.TeamsPacketListener;
import xyz.refinedev.api.tablist.setup.TabLayout;
import xyz.refinedev.api.tablist.skin.SkinCache;
import xyz.refinedev.api.tablist.thread.TablistThread;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Log4j2
public class TablistHandler {

    /**
     * Static instance of this Tablist Handler
     */
    @Getter private static TablistHandler instance;
    /**
     * This caches each player's {@link TabLayout} as this API is per player.
     *            Player UUID -> TabLayout
     */
    private final Map<UUID, TabLayout> layoutMapping = new ConcurrentHashMap<>();
    /**
     * The plugin registering this Tablist Handler
     */
    private final JavaPlugin plugin;
    /**
     * Our custom Skin Cache that stores every online player's Skin
     */
    private SkinCache skinCache;
    /**
     * Tablist Adapter of this instance
     */
    private TabAdapter adapter;
    /**
     * This thread handles all the operations surrounding
     * ticking and updating the NameTags
     */
    private TablistThread thread;
    private PacketEventsAPI<?> packetEvents;
    private final boolean debug;

    public TablistHandler(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        this.debug = Boolean.getBoolean("BDebug");
    }

    /**
     * Set up the PacketEvents instance of this Tablist Handler.
     * We let the plugin initialize and handle the PacketEvents instance.
     */
    public void init(PacketEventsAPI<?> packetEventsAPI, TeamsPacketListener listener) {
        this.packetEvents = packetEventsAPI;
        this.adapter = new ExampleAdapter();

        this.packetEvents.getEventManager().registerListener(listener);
        Bukkit.getPluginManager().registerEvents(new TabListener(this), plugin);

        this.setupSkinCache();
    }

    public void setupSkinCache() {
        this.skinCache = new SkinCache();
        Bukkit.getPluginManager().registerEvents(new SkinCacheListener(this), plugin);
    }

    public void registerAdapter(TabAdapter tabAdapter, long ticks) {
        this.adapter = tabAdapter == null ? new ExampleAdapter() : tabAdapter;

        if (ticks < 20L) {
            log.info("[{}] Provided refresh tick rate for Tablist is too low, reverting to 20 ticks!", plugin.getName());
            ticks = 20L;
        }

        if (Bukkit.getMaxPlayers() < 60) {
            log.fatal("[{}] Max Players is below 60, this will cause issues for players on 1.7 and below!", plugin.getName());
        }

        this.thread = new TablistThread(this);
        this.thread.runTaskTimerAsynchronously(plugin, 0L, ticks);
    }

    public void unload() {
        this.thread.cancel();
    }
}