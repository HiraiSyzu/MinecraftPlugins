package me.pluginhirai.minecraft_Plugin.randomteleport.service;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import me.pluginhirai.minecraft_Plugin.randomteleport.cooldown.CooldownManager;
import me.pluginhirai.minecraft_Plugin.randomteleport.effect.RandomTeleportEffects;
import me.pluginhirai.minecraft_Plugin.randomteleport.location.LocationFinder;
import me.pluginhirai.minecraft_Plugin.randomteleport.warmup.WarmupManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RandomTeleportService {
    private final RtpSettings settings;
    private final LocationFinder locationFinder;
    private final WarmupManager warmupManager;
    private final CooldownManager cooldownManager;
    private final RandomTeleportEffects rtpEffects;
    public RandomTeleportService(RtpSettings settings, LocationFinder locationFinder,
                                 WarmupManager warmupManager, CooldownManager cooldownManager, RandomTeleportEffects rtpEffects) {
        this.settings = settings;
        this.locationFinder = locationFinder;
        this.warmupManager = warmupManager;
        this.cooldownManager = cooldownManager;
        this.rtpEffects = rtpEffects;
    }
    public void requestTeleport(Player player) {
        UUID uuid = player.getUniqueId();
        if (warmupManager.isPending(uuid)) {
            sendMessage(player, settings.getPendingTeleportMessage());
            return;
        }
        if (!player.hasPermission("minecraft_plugin.rtp.bypass-cooldown") && cooldownManager.isOnCooldown(player)) {
            long remainingTime = cooldownManager.getRemainingSeconds(player);
            sendMessage(player, settings.getCooldownTeleportMessage().replace("%remaining_time%", String.valueOf(remainingTime)));
            return;
        }
        warmupManager.markPending(uuid);
        sendActionBar(player, settings.getWarmupTeleportMessage());
        if (!player.isOnline()) {
            warmupManager.clearPending(uuid);
            return;
        }
        locationFinder.findSafeLocation(player.getWorld()).thenAccept(result -> {
            if (result.isEmpty()) {
                warmupManager.clearPending(uuid);
                sendMessage(player, settings.getNoSafeLocationMessage());
                return;
            }
            if (!warmupManager.isPending(uuid)) {
                return;
            }
            warmupManager.startWarmup(player,() -> completeTeleport(player, result.get()));
        }).exceptionally(ex -> {
            warmupManager.clearPending(uuid);
            return null;
        });
    }

    private void completeTeleport(Player player, Location location) {
        player.teleportAsync(location).thenAccept(success -> {
            if (!success) {
                warmupManager.clearPending(player.getUniqueId());
                return;
            }
            rtpEffects.playSuccess(player);
            cooldownManager.setCooldown(player);
            warmupManager.clearPending(player.getUniqueId());
            sendActionBar(player, settings.getSuccessTeleportMessage());
        }).exceptionally(ex -> {
            warmupManager.clearPending(player.getUniqueId());
            return null;
        });
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private void sendActionBar(Player player, String message) {
        player.sendActionBar(MiniMessage.miniMessage().deserialize(message));
    }
    public void onWarmupCancelled(Player player) {
        cooldownManager.setCooldown(player);
        sendMessage(player, settings.getPlayerCancelMovementTeleportMessage());
    }
}
