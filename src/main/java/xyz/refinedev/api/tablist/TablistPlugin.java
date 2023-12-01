package xyz.refinedev.api.tablist;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.refinedev.api.tablist.adapter.impl.ExampleAdapter;
import xyz.refinedev.api.tablist.listener.TeamsPacketListener;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @since 8/21/2023
 * @version TablistAPI
 */
@Getter
public class TablistPlugin extends JavaPlugin {

    private TablistHandler tablistHandler;
    private PacketEventsAPI<?> packetEventsAPI;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));

        this.packetEventsAPI = PacketEvents.getAPI();
        this.packetEventsAPI.getSettings().bStats(false).checkForUpdates(false);

        this.packetEventsAPI.load();
    }

    @Override
    public void onEnable() {
        this.packetEventsAPI.init();

        this.tablistHandler = new TablistHandler(this);
        this.tablistHandler.init(this.packetEventsAPI, new TeamsPacketListener(this.packetEventsAPI));
        this.tablistHandler.registerAdapter(new ExampleAdapter(), 20L);
    }

    @Override
    public void onDisable() {
        this.tablistHandler.unload();
    }
}
