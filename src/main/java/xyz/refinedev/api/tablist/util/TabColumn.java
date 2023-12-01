package xyz.refinedev.api.tablist.util;

import lombok.experimental.UtilityClass;

/**
 * This Project is property of Refine Development © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * Created at 7/14/2021
 * @version TablistAPI
 */

@UtilityClass
public class TabColumn {

    public int getColumn(int i) {
        if (i <= 20) return 0;
        if (i <= 40) return 1;
        if (i <= 60) return 2;
        if (i <= 80) return 3;

        return 0;
    }
}
