package me.pluginhirai.minecraft_Plugin.randomteleport.warmup;
import me.pluginhirai.minecraft_Plugin.Minecraft_Plugin;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import me.pluginhirai.minecraft_Plugin.randomteleport.effect.RandomTeleportEffects;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class WarmupManager {
    private final Minecraft_Plugin plugin;
    private final RtpSettings settings;
    private final RandomTeleportEffects rtpEffects;
    private final Set<UUID> pendingTeleport = new HashSet<>();
    private final Map<UUID, BukkitTask> teleportTask = new HashMap<>();

    public WarmupManager(Minecraft_Plugin plugin, RtpSettings settings, RandomTeleportEffects rtpEffects) {
        this.plugin = plugin;
        this.settings = settings;
        this.rtpEffects = rtpEffects;
    }
    public void startWarmup(Player player, Runnable onComplete) {
        UUID uuid = player.getUniqueId();
        BukkitRunnable warmupTask = new BukkitRunnable() {
            int seconds = settings.getCountdownSeconds();
            @Override
            public void run() {
                if (!pendingTeleport.contains(uuid)) {
                    this.cancel();
                    return;
                }
                // Check if player is online
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }
                if (seconds > 0) {
                    rtpEffects.playWarmup(player);
                    // Send countdownMessage every second
                    String messageCountdownFormat = settings.getCountdownTeleportMessage().replace("%countdown_time%", String.valueOf(seconds));
                    player.sendActionBar(MiniMessage.miniMessage().deserialize(messageCountdownFormat));
                    seconds--;
                } else {
                    pendingTeleport.remove(uuid);
                    onComplete.run();
                    this.cancel();
                }
            }
        };
        pendingTeleport.add(uuid);
        BukkitTask task = warmupTask.runTaskTimer(plugin, 0, 20L);
        teleportTask.put(uuid, task);
    }
    public void markPending(UUID uuid) {
        pendingTeleport.add(uuid);
    }
    public boolean isPending(UUID uuid) {
        return pendingTeleport.contains(uuid);
    }
    public void clearPending(UUID uuid) {
        pendingTeleport.remove(uuid);
    }
    public void cancelAll() {
        for (BukkitTask task: teleportTask.values()) {
            task.cancel();
        }
        teleportTask.clear();
        pendingTeleport.clear();
    }

    public boolean cancel(Player player) {
        UUID uuid = player.getUniqueId();
        BukkitTask task = teleportTask.remove(uuid);
        if (!pendingTeleport.contains(uuid)) {
            return false;
        }
        pendingTeleport.remove(uuid);
        // Cancel countdown
        if (task != null) {
            task.cancel();
        }
        return true;
    }
}
