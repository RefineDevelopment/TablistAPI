package xyz.refinedev.api.tablist.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @since 9/2/2023
 * @version TablistAPI
 */

@Getter @Setter
@RequiredArgsConstructor
public class CachedSkin {


    private final String name;
    private final String value;
    private final String signature;


    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CachedSkin)) return false;

        CachedSkin skin = (CachedSkin) obj;
        return skin.getName().equals(this.name)
                && skin.getValue().equals(this.value)
                && skin.getSignature().equals(this.signature);
    }
}
