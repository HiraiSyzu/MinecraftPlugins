package me.pluginhirai.minecraft_Plugin.randomteleport.command;
import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class RandomTeleportCommand implements CommandExecutor {
    private final Map<UUID, Long> commandCooldown = new HashMap<>();
    private final int cooldownSeconds;
    private final int maxRadius;
    private final String cooldownMessage;
    private final String successMessage;
    private final Set<String> unsafeMaterials;
    // Constructor
    public RandomTeleportCommand(Minecraft_Plugin plugin) {
        this.cooldownSeconds = plugin.getConfig().getInt("cooldown-seconds"); // rtp-cooldown
        this.maxRadius = plugin.getConfig().getInt("max-radius"); // rtp radius +-5000 blocks
        this.cooldownMessage = plugin.getConfig().getString("messages.cooldown");
        this.successMessage = plugin.getConfig().getString("messages.success");
        this.unsafeMaterials = new HashSet<>(plugin.getConfig().getStringList("UNSAFE_MATERIALS"));
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        randomTeleportPlayer(sender);
        return true;
    }
    private void randomTeleportPlayer(CommandSender sender) {
        if (sender instanceof Player player) {
            if (isOnCooldown(player)) {
                return;
            }
            World world = player.getWorld(); //
            while (true) {

                Block groundBlock = getRandomGroundBlock(world);
                // Location Safety Checker
                if (isLocationSafe(groundBlock, player)) {
                    Location locationTeleport = createTeleportLocation(groundBlock);
                    player.teleportAsync(locationTeleport).thenAccept(success -> { // if Location is safe - teleport the player

                        // debug message - success message
                        // player.sendMessage("Debug: " + success);
                        if (success) { // if success teleport player
                            setCooldown(player); // if player teleported - set cooldown
                            player.sendMessage(MiniMessage.miniMessage().deserialize(successMessage)); // Success tp message
                        }
                    });
                    break;
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
    private boolean isLocationSafe(Block groundBlock, Player player) {
        Block headBlock = groundBlock.getRelative(0, 2, 0); // Pass the value of headBlock to groundBlock y + 2 is set to avoid suffocation
        Block footBlock = groundBlock.getRelative(0, 1, 0); // Pass the value of footBlock to groundBlock y + 1 so player can spawn above block
            // Debug
        player.sendMessage("Ground =" + groundBlock.getType().name());
        if (unsafeMaterials.contains(groundBlock.getType().name())) {
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
        // Debug
        // plugin.getLogger().info("Debug Max is currently: " + maxRadius);
        int x = ThreadLocalRandom.current().nextInt(-maxRadius, maxRadius + 1); // Set x cords to -5000 by 5000 blocks
        // z cords
        int z = ThreadLocalRandom.current().nextInt(-maxRadius, maxRadius + 1); // Set z cords by -5000 by 5000 blocks
        return world.getHighestBlockAt(x, z);
    }
    private boolean isOnCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (commandCooldown.containsKey(uuid)) {
            long lastUsed = commandCooldown.get(uuid);
            long cooldownMillisecond = cooldownSeconds * 1000L;

            if ((currentTime - lastUsed) < cooldownMillisecond) {
                long remainingTime = cooldownSeconds - ((currentTime - lastUsed) / 1000);
                String messageCooldownFormat = cooldownMessage.replace("%remaining_time%", String.valueOf(remainingTime));
                player.sendMessage(MiniMessage.miniMessage().deserialize(messageCooldownFormat));
                return true;
            }
        }
        return false;
    }
    private void setCooldown(Player player) {
        commandCooldown.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
