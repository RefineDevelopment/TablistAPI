package xyz.refinedev.api.tablist.setup;

import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import xyz.refinedev.api.tablist.util.Skin;

/**
 * This Project is property of Refine Development © 2021 - 2023
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * @version TablistAPI
 * @since 9/15/2023
 */

@Getter @Setter
@RequiredArgsConstructor
public class TabEntryInfo {

    private final UserProfile profile;
    private int ping = 0;
    private Skin skin = Skin.DEFAULT_SKIN;
    private String prefix = "", suffix = "";
    private WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo = null;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TabEntryInfo)) return false;
        if (o != this) return false;;

        return ((TabEntryInfo)o).getProfile().equals(this.profile);
    }

    @Override
    public int hashCode() {
       return this.profile.getUUID().hashCode() + 645;
    }
}
