# TablistAPI
Refine's TablistAPI | Custom

## Features
- Support for Custom Skins along with Dot Skins and Mob Skins
- Supports all spigot and client versions (1.7x to 1.20x).
- Lightweight with no performance overhead.
- Supports hex colors on supported versions.
- Periodically updating footer/header.
- 1.7 clients have a 32 char limit whilst 1.8+ clients have a 48 char limit.
- Easy to use.

## Installing
You can either shade this repository into your plugin, or run it as a plugin by itself.

1. Clone this repository
2. Enter the directory: `cd TablistAPI`
3. Build & install with Maven: `mvn clean package install`

OR
```xml
<repositories>
    <repository>
        <id>refine-public</id>
        <url>https://maven.refinedev.xyz/repository/public-repo/</url>
    </repository>
</repositories>
```
Next, add TablistAPI to your project's dependencies via Maven

Add this to your `pom.xml` `<dependencies>`:
```xml
<dependency>
  <groupId>xyz.refinedev.api</groupId>
  <artifactId>TablistAPI</artifactId>
  <version>2.2</version>
  <scope>compile</scope>
</dependency>
```

## Usage
It requires PacketEvents to be shaded in the plugin you are using this with. Please relocate Packet Events
according to their guide in their [wiki guide](https://github.com/retrooper/packetevents/wiki/Shading-PacketEvents)

I recommend registering tablist after any other scoreboard related API is registered because
those APIs sometimes assign or override the player's scoreboard, which would be problematic here.

You can initiate and register a TablistAdapter using the following code:

```java
import xyz.refinedev.api.tablist.TablistHandler;
import xyz.refinedev.api.tablist.adapter.impl.ExampleAdapter;

import com.github.retrooper.packetevents.PacketEventsAPI;

public class ExamplePlugin extends JavaPlugin {

    private TablistHandler tablistHandler;
    private PacketEventsAPI<?> packetEvents;

    @Override
    public void onEnable() {
        //this.packetEvents.init();
        this.tablistHandler = new TablistHandler(plugin);
        this.tablistHandler.init(this.packetEvents, new TeamsPacketListener(this.packetEvents));
        this.tablistHandler.registerAdapter(new ExampleAdapter(tablist), 20L);
    }
}
```

```java
import xyz.refinedev.api.tablist.adapter.TabAdapter;

public class TablistAdapter implements TabAdapter {

    /**
     * Get the tab header for a player.
     *
     * @param player the player
     * @return string
     */
    public String getHeader(Player player) { // String or you can use \n to use multiple lines
        return "Example";
    } 

    /**
     * Get the tab player for a player.
     *
     * @param player the player
     * @return string
     */
    public String getFooter(Player player) { // String or you can use \n to use multiple lines
        return "Example";
    }
    
    /**
     * Get the tab lines for a player.
     *
     * @param player the player
     * @return list of entries
     */
    public List<TabEntry> getLines(Player player) {  // Tab Entry contains the string, skin, slot and ping of the tablist slot
        List<TabEntry> entries = new ArrayList<>();

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        
        for ( int i = 0; i < 80; i++ ) {
            final int x = i % 4;
            final int y = i / 4;

            if (players.size() <= i) continue;

            Player tabPlayer = players.get(i);
            if (tabPlayer == null) continue;

            entries.add(new TabEntry(x, y, tabPlayer.getDisplayName(), tabPlayer.spigot().getPing(), Skin.getPlayer(tabPlayer)));
        }

        return entries;
    }
}
```

## Support
We don't plan on working on the API much, so don't expect support for bugs. 
If you need help with implementation, feel free to ask in our [discord](https://discord.com/invite/Q39GNJtHz2).

## Disclaimer
Feel free to use this API in any project, just give credits. You are not allowed to sell or
claim ownership of this code. The code is provided as is and is property of Refine Development.
