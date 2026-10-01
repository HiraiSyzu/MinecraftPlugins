package me.pluginhirai.minecraft_Plugin.randomteleport.command;
import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.EnumSet;

public class RandomTeleportCommand implements CommandExecutor {
    private static final int MAX_RADIUS = 5000;
    private final Minecraft_Plugin plugin;

    // Constructor
    public RandomTeleportCommand(Minecraft_Plugin plugin) {
        this.plugin = plugin;
    }
    private static final EnumSet<Material> UNSAFE_GROUND_MATERIALS = EnumSet.of(
            // List of Unsafe materials
            Material.WATER,
            Material.AIR,
            Material.LAVA,
            Material.CACTUS,
            Material.POWDER_SNOW
    );
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        randomTeleportPlayer(sender);
        return true;
    }
    private void randomTeleportPlayer(CommandSender sender) {
        if ((sender instanceof Player player)) {
            boolean safeLocation = false;
            World world = player.getWorld();

            while (!safeLocation) {

                Block groundBlock = getRandomGroundBlock(world);
                // Location Safety Checker
                if (isLocationSafe(groundBlock)) {
                    Location locationTeleport = createTeleportLocation(groundBlock);
                    player.teleportAsync(locationTeleport); // if Location is safe -  teleport the player
                    safeLocation = true; // if true set safeLocation true
                }
                /* Debug
                player.sendMessage("x: " + x);
                player.sendMessage("Z: " + z);
                player.sendMessage("Y: " + ground.getY());
                player.sendMessage("Ground: " + ground.getType());

                player.sendMessage("Ground: " + groundBlock.getType());
                player.sendMessage("Head: " + headBlock.getType());
                player.sendMessage("Foot: " + footBlock.getType());
                 */
            }
        }
    }
    private boolean isLocationSafe(Block groundBlock) {
        Block headBlock = groundBlock.getRelative(0, 2, 0); // Pass the value of headBlock to groundBlock y + 2 is set to avoid suffocation
        Block footBlock = groundBlock.getRelative(0, 1, 0); // Pass the value of footBlock to groundBlock y + 1 so player can spawn above block
        if (UNSAFE_GROUND_MATERIALS.contains(groundBlock.getType())) {
            return false; // Ground isn't safe
        }
        if (!headBlock.isReplaceable() || footBlock.getType().isSolid()) {
            return false; // player legs would spawn inside solid block
        }
        if (!footBlock.isReplaceable() || headBlock.getType().isSolid()) {
            return false; // Player would suffocate
        }
        return true; // Passed all checks
    }
    private Location createTeleportLocation(Block groundBlock) {
        return groundBlock.getLocation().add(0.5, 1, 0.5);
    }
    private Block getRandomGroundBlock(World world) {
        int maxRadius = plugin.getConfig().getInt("max-radius"); // Get the max-radius value in plugin.yml

        int x = ThreadLocalRandom.current().nextInt(-maxRadius, maxRadius); // Set x cords to -5000 by 5000 blocks
        // z cords
        int z = ThreadLocalRandom.current().nextInt(-maxRadius, maxRadius); // Set z cords by -5000 by 5000 blocks
        return world.getHighestBlockAt(x, z);
    }
}
