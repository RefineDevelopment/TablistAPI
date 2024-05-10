package xyz.refinedev.api.tablist.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * <p>
 * This Project is property of Refine Development.<br>
 * Copyright © 2023, All Rights Reserved.<br>
 * Redistribution of this Project is not allowed.<br>
 * </p>
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 12/3/2023
 */

@Getter
@RequiredArgsConstructor
public class GlitchFixEvent extends BaseEvent {

    private final Player player;

}
