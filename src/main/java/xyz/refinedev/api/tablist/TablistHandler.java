package xyz.refinedev.api.tablist;

import com.github.retrooper.packetevents.PacketEventsAPI;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

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
     * Main tablist listener
     */
    private TabListener listener;
    /**
     * This thread handles all the operations surrounding
     * ticking and updating the NameTags
     */
    private TablistThread thread;
    private PacketEventsAPI<?> packetEvents;
    private TeamsPacketListener teamsPacketListener;
    private final boolean debug;
    @Setter private boolean hook, ignore1_7;
    private long ticks = 20L;

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
        this.listener = new TabListener(this);

        this.teamsPacketListener = listener;
        this.packetEvents.getEventManager().registerListener(listener);
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);

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
            this.ticks = 20L;
        } else {
            this.ticks = ticks;
        }

        if (Bukkit.getMaxPlayers() < 60) {
            log.fatal("[{}] Max Players is below 60, this will cause issues for players on 1.7 and below!", plugin.getName());
        }

        this.thread = new TablistThread(this);
        this.thread.start();
    }

    public void unload() {
        if (this.listener != null) {
            HandlerList.unregisterAll(this.listener);
            this.listener = null;
        }

        // Destroy player scoreboards.
        for ( Map.Entry<UUID, TabLayout> entry : this.layoutMapping.entrySet()) {
            UUID uuid = entry.getKey();
            entry.getValue().cleanup();

            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                continue;
            }

            // Destroy main tablist team
            Team team = player.getScoreboard().getTeam("ztab");
            if (team != null) {
                team.unregister();
            }

            this.layoutMapping.remove(uuid);
        }

        this.thread.terminate();
        this.thread.interrupt();
        this.thread = null;
    }
}