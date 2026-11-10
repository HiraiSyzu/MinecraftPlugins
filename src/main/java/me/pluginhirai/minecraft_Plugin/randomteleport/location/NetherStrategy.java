package me.pluginhirai.minecraft_Plugin.randomteleport.location;
import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class NetherStrategy implements GroundStrategy{
    private static final int START_HEIGHT = 120;
    private static final int STOP_HEIGHT = 32;
    private final Minecraft_Plugin plugin;
    private final SafetyChecker safetyChecker;
    public NetherStrategy(Minecraft_Plugin plugin, SafetyChecker safetyChecker) {
        this.plugin = plugin;
        this.safetyChecker = safetyChecker;
    }
    @Override
    public CompletableFuture<Optional<Block>> findGroundBlock(World world, int x, int z) {
        return world.getChunkAtAsync(x >> 4, z >> 4).thenApplyAsync(ignored ->  {
            for (int y = START_HEIGHT; y >= STOP_HEIGHT; y--) {
                Block groundBlock = world.getBlockAt(x, y, z);
                    if (groundBlock.getType().isSolid() && safetyChecker.isLocationSafe(groundBlock)) {
                    return Optional.of(groundBlock);
                }
            }
            return Optional.<Block>empty();
        }, Bukkit.getScheduler().getMainThreadExecutor(plugin));
    }

}
