Check Repository = https://jitpack.io/#AchyMake/Chunks
```xml
<repositories>
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.AchyMake</groupId>
        <artifactId>Chunks</artifactId>
        <version>LATEST</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
Example
```java
package org.example.yourplugin;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class YourPlugin extends JavaPlugin {
    private static YourPlugin instance;
    @Override
    public void onEnable() {
        instance = this;
    }
    @Override
    public void onDisable() {
    }
    public static YourPlugin getInstance() {
        return instance;
    }
    /**
     * @return chunks instance
     */
    public Chunks getChunks() {
        return Chunks.getInstance();
    }
    /**
     * @param chunk
     * @return true if chunk is claimed
     */
    public boolean isClaimed(Chunk chunk) {
        return getChunks().getChunkHandler().isClaimed(chunk);
    }
    /**
     * @param chunk
     * @return OfflinePlayer if claimed else null
     */
    public OfflinePlayer getOwner(Chunk chunk) {
        return getChunks().getChunkHandler().getOwner(chunk);
    }
    /**
     * @param chunk
     * @return string of OfflinePlayer name
     */
    public String getChunkName(Chunk chunk) {
        return getChunks().getChunkHandler().getName(chunk);
    }
}
```
