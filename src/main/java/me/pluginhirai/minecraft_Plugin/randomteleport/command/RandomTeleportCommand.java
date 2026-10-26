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
    import org.bukkit.scheduler.BukkitRunnable;
    import org.bukkit.scheduler.BukkitTask;

    import java.util.*;
    import java.util.concurrent.ThreadLocalRandom;


    public class RandomTeleportCommand implements CommandExecutor {
        private final Map<UUID, Long> commandCooldown = new HashMap<>();
        private final Set<UUID> pendingTeleport = new HashSet<>();
        private final Minecraft_Plugin plugin;
        private final int cooldownSeconds;
        private final int maxRadius;
        private final Map<UUID, BukkitTask> teleportTask = new HashMap<>();
        private final String cooldownMessage;
        private final String successMessage;
        private final String noSafeLocationMessage;
        private final Set<String> unsafeMaterials;
        private final int maxAttempts;
        private final String noPermissionMessage;
        private final String warmupTeleportMessage;
        private final int countdownSeconds;
        private final String countdownMessage;
        private final String pendingTeleportMessage;
        private final String canceledMovementTeleportMessage;
        // Constructor
        public RandomTeleportCommand(Minecraft_Plugin plugin) {
            this.plugin = plugin;
            this.cooldownSeconds = plugin.getConfig().getInt("cooldown-seconds"); // rtp-cooldown
            this.maxRadius = plugin.getConfig().getInt("max-radius"); // rtp radius +-5000 blocks
            this.cooldownMessage = plugin.getConfig().getString("messages.cooldown"); // Cooldown Message
            this.successMessage = plugin.getConfig().getString("messages.success"); // Success Message
            this.maxAttempts = plugin.getConfig().getInt("max-attempts"); // rtp logic attempts
            this.noSafeLocationMessage = plugin.getConfig().getString("messages.no_safe_location"); // No safe Location Message
            this.unsafeMaterials = new HashSet<>(plugin.getConfig().getStringList("unsafe-materials")); // List of unsafe materials
            this.noPermissionMessage = plugin.getConfig().getString("messages.no-permission");
            this.warmupTeleportMessage = plugin.getConfig().getString("messages.warmup-teleport-message");
            this.countdownSeconds = plugin.getConfig().getInt("countdown-seconds");
            this.countdownMessage = plugin.getConfig().getString("messages.countdown");
            this.pendingTeleportMessage = plugin.getConfig().getString("messages.pending-teleport-message");
            this.canceledMovementTeleportMessage = plugin.getConfig().getString("messages.canceled-movement-teleport-message");
        }
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            randomTeleportPlayer(sender);
            return true;
        }
        private void randomTeleportPlayer(CommandSender sender) {
            if (sender instanceof Player player) {
                if (!player.hasPermission("minecraft_plugin.rtp")) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(noPermissionMessage));
                    return;
                }
                if (hasPendingTeleportPlayer(player)) {
                    return;
                }
                if (isOnCooldown(player)) {
                    return;
                }
                World world = player.getWorld(); //
                boolean foundLocation = false;
                for(int attempt = 0; attempt < maxAttempts; attempt++) {
                    Block groundBlock = getRandomGroundBlock(world);
                    // Location Safety Checker
                    if (isLocationSafe(groundBlock)) {
                        foundLocation = true;
                        Location locationTeleport = createTeleportLocation(groundBlock);

                        player.sendMessage(MiniMessage.miniMessage().deserialize(warmupTeleportMessage));
                        startWarmupTask(player, locationTeleport);
                        break;
                    }
                }
                if (!foundLocation) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(noSafeLocationMessage));
                }
            } else {
                sender.sendMessage("Only player can use this command");
            }
        }
        private boolean isLocationSafe(Block groundBlock) {
            Block headBlock = groundBlock.getRelative(0, 2, 0); // Pass the value of headBlock to groundBlock y + 2 is set to avoid suffocation
            Block footBlock = groundBlock.getRelative(0, 1, 0); // Pass the value of footBlock to groundBlock y + 1 so player can spawn above block
                // Debug
            /* Debug
                    player.sendMessage("x: " + x);
                    player.sendMessage("Z: " + z);
                    player.sendMessage("Y: " + ground.getY());
                    player.sendMessage("Ground: " + ground.getType());

                    player.sendMessage("Ground: " + groundBlock.getType());
                    player.sendMessage("Head: " + headBlock.getType());
                    player.sendMessage("Foot: " + footBlock.getType());
                     */
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
            if(player.hasPermission("minecraft_plugin.rtp.bypass-cooldown")) {
                return false;
            }
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
            return  false;
        }
        private void setCooldown(Player player) {
            commandCooldown.put(player.getUniqueId(), System.currentTimeMillis()); // set cooldown on player
        }
        private void startWarmupTask(Player player, Location targetLocation) {
            UUID uuid = player.getUniqueId();

            BukkitRunnable warmupTask = new BukkitRunnable() {
            int secondsLeft = countdownSeconds;
                @Override
                public void run() {

                    if (!pendingTeleport.contains(uuid)) {
                        this.cancel();
                        return;
                    }
                    // Check if player is online during warmup
                    if (!player.isOnline()) {
                        pendingTeleport.remove(uuid);
                        this.cancel();
                        return;
                    }

                    if (secondsLeft > 0) {
                        // Send countdown message every second
                        String messageCountdownFormat = countdownMessage.replace("%countdown_time%", String.valueOf(secondsLeft));
                        player.sendActionBar(MiniMessage.miniMessage().deserialize(messageCountdownFormat));
                        secondsLeft--;
                    } else {
                        player.teleportAsync(targetLocation).thenAccept(success -> { // if Location is safe - teleport the player
                            // debug message - success message
                            // player.sendMessage("Debug: " + success);
                            if (success) { // if success teleport player
                                setCooldown(player); // if player teleported - set cooldown
                                pendingTeleport.remove(uuid);
                                teleportTask.remove(uuid);
                                player.sendActionBar(MiniMessage.miniMessage().deserialize(successMessage)); // Success tp message
                            }
                        });
                        this.cancel();
                    }
                }
            };
            pendingTeleport.add(uuid);
            BukkitTask task = warmupTask.runTaskTimer(plugin, 0L, 20L);
            teleportTask.put(uuid, task);
        }
        private boolean hasPendingTeleportPlayer(Player player) {
            UUID uuid = player.getUniqueId();
            if (pendingTeleport.contains(uuid)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(pendingTeleportMessage));
                return true;
            }
            return false;
        }
        public Set<UUID> getPendingTeleport() {
            return pendingTeleport;
        }
        public void cancelMovementTeleport(Player player) {
            player.sendMessage("Debug Cancel");
            UUID uuid = player.getUniqueId();

            // Remove pending
            if (!pendingTeleport.contains(uuid)) {
                player.sendMessage("Not PENDING");
                return;
            }
            player.sendMessage("IS PENDING");
            pendingTeleport.remove(uuid);

            // Cancel countdown
            BukkitTask task = teleportTask.remove(uuid);
            if (task != null) {
                player.sendMessage("TASK FOUND");
                task.cancel();
            } else {
                player.sendMessage("Task Not found");
            }
            setCooldown(player);
            player.sendActionBar(MiniMessage.miniMessage().deserialize(canceledMovementTeleportMessage));
        }
    }
