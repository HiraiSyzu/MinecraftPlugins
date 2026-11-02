package me.pluginhirai.minecraft_Plugin.randomteleport.location;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class LocationFinder {
    private final RtpSettings settings;
    private final SafetyChecker safetyChecker;
    private final OverworldStrategy overworldStrategy;
    private final NetherStrategy netherStrategy;
    private final EndStrategy endStrategy;
    public LocationFinder(RtpSettings settings, SafetyChecker safetyChecker, OverworldStrategy overworldStrategy, NetherStrategy netherStrategy, EndStrategy endStrategy) {
        this.settings = settings;
        this.safetyChecker = safetyChecker;
        this.overworldStrategy = overworldStrategy;
        this.netherStrategy = netherStrategy;
        this.endStrategy = endStrategy;
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
        int x = ThreadLocalRandom.current().nextInt(-settings.getWorldSettings(world.getEnvironment()).getMaxRadius(),
                settings.getWorldSettings(world.getEnvironment()).getMaxRadius() + 1); // Set x cords to -5000 by 5000 blocks
        // z cords
        int z = ThreadLocalRandom.current().nextInt(-settings.getWorldSettings(world.getEnvironment()).getMaxRadius(),
                settings.getWorldSettings(world.getEnvironment()).getMaxRadius() + 1); // Set z cords by -5000 by 5000 blocks
        return getStrategy(world).findGroundBlock(world, x, z).thenCompose(optionalBlock -> {
            if (optionalBlock.isEmpty()) {
                return findSafeLocation(world, attempt + 1);
            }
            Block block = optionalBlock.get();
            if (safetyChecker.isLocationSafe(block)) {
                return CompletableFuture.completedFuture(Optional.of(createTeleportLocation(block)));
            }
            return findSafeLocation(world, attempt + 1);
        });
    }
    private GroundStrategy getStrategy(World world) {
        if (world.getEnvironment() == World.Environment.NETHER) {
            return netherStrategy;
        } else if (world.getEnvironment() == World.Environment.THE_END){
            return endStrategy;
        } else {
            return overworldStrategy;
        }
    }
}
