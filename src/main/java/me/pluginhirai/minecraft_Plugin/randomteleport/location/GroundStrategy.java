package me.pluginhirai.minecraft_Plugin.randomteleport.location;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GroundStrategy {
    CompletableFuture<Optional<Block>> findGroundBlock(
            World world, int x, int z
    );
}
