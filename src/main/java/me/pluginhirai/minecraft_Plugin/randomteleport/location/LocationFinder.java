package me.pluginhirai.minecraft_Plugin.randomteleport.location;

import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class LocationFinder {
    private final Minecraft_Plugin plugin;
    private final RtpSettings settings;
    private final SafetyChecker safetyChecker;

    public LocationFinder(Minecraft_Plugin plugin, RtpSettings settings, SafetyChecker safetyChecker) {
        this.plugin = plugin;
        this.settings = settings;
        this.safetyChecker = safetyChecker;
    }
    private CompletableFuture<Block> getRandomGroundBlockAsync(World world) {
        // Debug
        // plugin.getLogger().info("Debug Max is currently: " + maxRadius);
        int x = ThreadLocalRandom.current().nextInt(-settings.getMaxRadius(), settings.getMaxRadius() + 1); // Set x cords to -5000 by 5000 blocks
        // z cords
        int z = ThreadLocalRandom.current().nextInt(-settings.getMaxRadius(), settings.getMaxRadius() + 1); // Set z cords by -5000 by 5000 blocks
        return world.getChunkAtAsync(x >> 4, z >> 4).thenApplyAsync(ignored ->  {
            return world.getHighestBlockAt(x, z);
        }, Bukkit.getScheduler().getMainThreadExecutor(plugin));
    }
    private Location createTeleportLocation(Block groundBlock) {
        return groundBlock.getLocation().add(0.5, 1, 0.5);
    }
    public CompletableFuture<Optional<Location>> findSafeLocation(World world) {
        return findSafeLocation(world, 0);
    }
    private CompletableFuture<Optional<Location>> findSafeLocation(World world, int attempt) {
        if (attempt >= settings.getMaxTeleportAttempts()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return getRandomGroundBlockAsync(world).thenCompose(block -> {
            if (safetyChecker.isLocationSafe(block)) {
                return CompletableFuture.completedFuture(Optional.of(createTeleportLocation(block)));
            }
            return findSafeLocation(world, attempt + 1);
        });
    }
}
