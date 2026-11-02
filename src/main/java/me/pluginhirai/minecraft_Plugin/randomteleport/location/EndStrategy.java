package me.pluginhirai.minecraft_Plugin.randomteleport.location;

import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EndStrategy implements GroundStrategy{
    private final Minecraft_Plugin plugin;
    public EndStrategy(Minecraft_Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Optional<Block>> findGroundBlock(World world, int x, int z) {
        return world.getChunkAtAsync(x >> 4, z >> 4).thenApplyAsync(ignored ->
            Optional.of(world.getHighestBlockAt(x, z)),
                    Bukkit.getScheduler().getMainThreadExecutor(plugin)
        );
    }
}
