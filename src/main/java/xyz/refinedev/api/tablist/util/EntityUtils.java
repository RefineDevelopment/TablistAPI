package xyz.refinedev.api.tablist.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.EntityType;

import java.util.EnumMap;
import java.util.Map;

/**
 * This Project is the property of Refine Development © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * Created at 4/15/2021
 * @version Bolt
 */

@UtilityClass
public class EntityUtils {

    private final Map<EntityType, String> displayNames = new EnumMap<>(EntityType.class);
    private int currentFakeEntityId = -1;

    public int getFakeEntityId() {
        return currentFakeEntityId--;
    }

    static {
        displayNames.put(EntityType.ARROW, "Arrow");
        displayNames.put(EntityType.BAT, "Bat");
        displayNames.put(EntityType.BLAZE, "Blaze");
        displayNames.put(EntityType.BOAT, "Boat");
        displayNames.put(EntityType.CAVE_SPIDER, "Cave Spider");
        displayNames.put(EntityType.CHICKEN, "Chicken");
        displayNames.put(EntityType.COW, "Cow");
        displayNames.put(EntityType.CREEPER, "Creeper");
        displayNames.put(EntityType.DROPPED_ITEM, "Item");
        displayNames.put(EntityType.EGG, "Egg");
        displayNames.put(EntityType.ENDER_CRYSTAL, "Ender Crystal");
        displayNames.put(EntityType.ENDER_DRAGON, "Ender Dragon");
        displayNames.put(EntityType.ENDER_PEARL, "Ender Pearl");
        displayNames.put(EntityType.ENDER_SIGNAL, "Ender Signal");
        displayNames.put(EntityType.ENDERMAN, "Enderman");
        displayNames.put(EntityType.EXPERIENCE_ORB, "Experience Orb");
        displayNames.put(EntityType.FALLING_BLOCK, "Falling Block");
        displayNames.put(EntityType.FIREBALL, "Fireball");
        displayNames.put(EntityType.FIREWORK, "Firework");
        displayNames.put(EntityType.FISHING_HOOK, "Fishing Rod Hook");
        displayNames.put(EntityType.GHAST, "Ghast");
        displayNames.put(EntityType.GIANT, "Giant");
        displayNames.put(EntityType.HORSE, "Horse");
        displayNames.put(EntityType.IRON_GOLEM, "Iron Golem");
        displayNames.put(EntityType.ITEM_FRAME, "Item Frame");
        displayNames.put(EntityType.LEASH_HITCH, "Lead Hitch");
        displayNames.put(EntityType.LIGHTNING, "Lightning");
        displayNames.put(EntityType.MAGMA_CUBE, "Magma Cube");
        displayNames.put(EntityType.MINECART, "Minecart");
        displayNames.put(EntityType.MINECART_CHEST, "Chest Minecart");
        displayNames.put(EntityType.MINECART_FURNACE, "Furnace Minecart");
        displayNames.put(EntityType.MINECART_HOPPER, "Hopper Minecart");
        displayNames.put(EntityType.MINECART_MOB_SPAWNER, "Spawner Minecart");
        displayNames.put(EntityType.MINECART_TNT, "TNT Minecart");
        displayNames.put(EntityType.OCELOT, "Ocelot");
        displayNames.put(EntityType.PAINTING, "Painting");
        displayNames.put(EntityType.PIG, "Pig");
        displayNames.put(EntityType.PLAYER, "Player");
        displayNames.put(EntityType.PRIMED_TNT, "TNT");
        displayNames.put(EntityType.SHEEP, "Sheep");
        displayNames.put(EntityType.SILVERFISH, "Silverfish");
        displayNames.put(EntityType.SKELETON, "Skeleton");
        displayNames.put(EntityType.SLIME, "Slime");
        displayNames.put(EntityType.SMALL_FIREBALL, "Fireball");
        displayNames.put(EntityType.SNOWBALL, "Snowball");
        displayNames.put(EntityType.SNOWMAN, "Snowman");
        displayNames.put(EntityType.SPIDER, "Spider");
        displayNames.put(EntityType.SPLASH_POTION, "Potion");
        displayNames.put(EntityType.SQUID, "Squid");
        displayNames.put(EntityType.THROWN_EXP_BOTTLE, "Experience Bottle");
        displayNames.put(EntityType.UNKNOWN, "Custom");
        displayNames.put(EntityType.VILLAGER, "Villager");
        displayNames.put(EntityType.WITCH, "Witch");
        displayNames.put(EntityType.WITHER, "Wither");
        displayNames.put(EntityType.WITHER_SKULL, "Wither Skull");
        displayNames.put(EntityType.WOLF, "Wolf");
        displayNames.put(EntityType.ZOMBIE, "Zombie");
    }

    /**
     * Gets the display name of a given entity type.
     *
     * @param type The entity type to lookup.
     * @return The display name of the provided entity type.
     */
    public String getName(EntityType type) {
        return (displayNames.get(type));
    }

    public EntityType parse(String input) {
        for (Map.Entry<EntityType, String> entry : displayNames.entrySet()) {
            if (entry.getValue().replace(" ", "").equalsIgnoreCase(input)) {
                return entry.getKey();
            }
        }

        for (EntityType type : EntityType.values()) {
            if (input.equalsIgnoreCase(type.toString())) {
                return type;
            }
        }

        return null;
    }
}
