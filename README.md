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
    public ChunkHandler getChunkHandler(Chunk chunk) {
        return Chunks.getInstance().getChunkHandler(chunk);
    }
    public boolean isClaimed(Chunk chunk) {
        return Chunks.getInstance().isClaimed(chunk);
    }
    public OfflinePlayer getOwner(Chunk chunk) {
        return Chunks.getInstance().getOwner(chunk);
    }
    public static YourPlugin getInstance() {
        return instance;
    }
}
```
