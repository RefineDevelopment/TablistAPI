package xyz.refinedev.api.tablist.setup;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import xyz.refinedev.api.skin.CachedSkin;
import xyz.refinedev.api.tablist.util.Skin;

@Getter
@Setter
@Accessors(chain = true)
public class TabEntry {

    private final int x, y;
    private String text;
    private int ping = 0;
    private CachedSkin skin = Skin.DEFAULT_SKIN;

    public TabEntry(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public TabEntry(int x, int y, String text, int ping) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.ping = ping;
    }

    public TabEntry(int x, int y, String text, CachedSkin skin) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.skin = skin;
    }

    public TabEntry(int x, int y, String text, int ping, CachedSkin skin) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.ping = ping;
        this.skin = skin;
    }
}
