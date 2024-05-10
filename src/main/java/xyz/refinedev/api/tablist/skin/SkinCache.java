package xyz.refinedev.api.tablist.skin;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.log4j.Log4j2;

import org.bukkit.entity.Player;
import xyz.refinedev.api.tablist.util.Skin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 9/2/2023
 */

@Log4j2
public class SkinCache {

    public static final CachedSkin DEFAULT = new CachedSkin("Default", Skin.DEFAULT_SKIN.getValue(), Skin.DEFAULT_SKIN.getSignature());;

    private static final String ASHCON_URL = "https://api.ashcon.app/mojang/v2/user/%s";
    private static final String MOJANG_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    private final Map<String, CachedSkin> skinCache = new ConcurrentHashMap<>();

    /**
     * Load and cache this {@link Player}'s Skin
     *
     * @param player {@link Player}
     */
    public void registerCache(Player player) {
        CompletableFuture<CachedSkin> skinFuture = CompletableFuture.supplyAsync(() -> {
            try {
                WrappedGameProfile wrappedGameProfile = WrappedGameProfile.fromPlayer(player);
                WrappedSignedProperty prop = wrappedGameProfile.getProperties().get("textures").iterator().next();
                String value = prop.getValue();
                String signature = prop.getSignature();

                return new CachedSkin(player.getName(), value, signature);
            } catch (Exception e) {
                return this.fetchSkin(player, false);
            }
        });

        skinFuture.whenComplete((skin, action) -> {
            if (skin != null) {
                this.skinCache.put(player.getName(), skin);
            }
        });
    }

    /**
     * Get a {@link Player}'s Skin
     *
     * @param player {@link Player}
     * @return       {@link }
     */
    public CachedSkin getSkin(Player player) {
        CachedSkin skin = this.skinCache.get(player.getName());
        if (skin == null) {
            this.registerCache(player);
            return DEFAULT;
        }
        return skin;
    }

    /**
     * Remove this player's skin cache
     *
     * @param player {@link Player} Player
     */
    public void removeCache(Player player) {
        this.skinCache.remove(player.getName());
    }

    /**
     * Fetch a player's skin from the most optimal server
     *
     * @param player      {@link Player} Player
     * @param alternative {@link Boolean} Alternative API
     * @return            {@link CachedSkin} Skin
     */
    public CachedSkin fetchSkin(Player player, boolean alternative) {
        String name = player.getName();
        UUID uuid = player.getUniqueId();

        try {
            return alternative ? fetchSkinName(name) : fetchSkinUUID(name, uuid);
        } catch (NullPointerException | IOException e) {
            if (!alternative) {
                return fetchSkin(player, true);
            }

            return DEFAULT;
        }
    }

    /**
     * Fetches the given player's skin from mojang's session server
     *
     * @param uuid {@link UUID} Player's UUID
     * @return     {@link CachedSkin} Skin
     */
    public CachedSkin fetchSkinUUID(String name, UUID uuid) throws IOException{
        String uuidStr = uuid.toString();

        URL url = new URL(String.format(MOJANG_URL, uuidStr));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() != 200) {
            throw new IOException();
        } else {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder sb = new StringBuilder();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }

                JsonElement element = JsonParser.parseString(sb.toString());
                if (!element.isJsonObject()) return null;

                JsonObject object = element.getAsJsonObject();
                JsonArray jsonProperties = object.get("properties").getAsJsonArray();
                JsonObject property = jsonProperties.get(0).getAsJsonObject();

                String value = property.get("value").getAsString();
                String signature = property.get("signature").getAsString();

                return new CachedSkin(name, value, signature);
            }
        }
    }

    /**
     * Fetches the given player's skin from Ashcon's skin API session server
     *
     * @param name {@link String} Player name
     * @return     {@link CachedSkin} Skin
     */
    public CachedSkin fetchSkinName(String name) throws IOException {
        URL url = new URL(String.format(ASHCON_URL, name));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() != 200) {
            return new CachedSkin(name, Skin.DEFAULT_SKIN.getValue(), Skin.DEFAULT_SKIN.getSignature());
        } else {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder sb = new StringBuilder();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }

                JsonElement element = JsonParser.parseString(sb.toString());
                if (!element.isJsonObject()) return null;

                JsonObject object = element.getAsJsonObject();
                JsonObject textures = object.get("textures").getAsJsonObject();
                JsonObject raw = textures.get("raw").getAsJsonObject();

                String value = raw.get("value").getAsString();
                String signature = raw.get("signature").getAsString();

                return new CachedSkin(name, value, signature);
            }
        }
    }
}
