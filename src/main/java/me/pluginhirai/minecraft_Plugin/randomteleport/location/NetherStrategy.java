/*
package me.pluginhirai.minecraft_Plugin.randomteleport.location;

import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class NetherStrategy implements GroundStrategy{
    private static final int START_HEIGHT = 120;
    private static final int STOP_HEIGHT = 32;
    private final Minecraft_Plugin plugin;
    private final RtpSettings settings;
    private final SafetyChecker safetyChecker;
    public NetherStrategy(Minecraft_Plugin plugin, RtpSettings settings, SafetyChecker safetyChecker) {
        this.plugin = plugin;
        this.settings = settings;
        this.safetyChecker = safetyChecker;
    }
    public CompletableFuture<Object> getRandomGroundBlockAsync(World world) {
        // Debug
        // plugin.getLogger().info("Debug Max is currently: " + maxRadius);
        int x = ThreadLocalRandom.current().nextInt(-settings.getWorldSettings(world.getEnvironment()).getMaxRadius(), settings.getWorldSettings(world.getEnvironment()).getMaxRadius() + 1); // Set x cords to -5000 by 5000 blocks
        // z cords
        int z = ThreadLocalRandom.current().nextInt(-settings.getWorldSettings(world.getEnvironment()).getMaxRadius(), settings.getWorldSettings(world.getEnvironment()).getMaxRadius()+ 1); // Set z cords by -5000 by 5000 blocks
        return world.getChunkAtAsync(x >> 4, z >> 4).thenApplyAsync(ignored ->  {
            for (int y = START_HEIGHT; y >= STOP_HEIGHT; y--) {
                Block groundBlock = world.getBlockAt(x, y, z);
                if (groundBlock.getType().isSolid() && safetyChecker.isLocationSafe(groundBlock)) {
                    return groundBlock;
                }
            }
            return Optional.empty();
        }, Bukkit.getScheduler().getMainThreadExecutor(plugin));
    }

}

 */
