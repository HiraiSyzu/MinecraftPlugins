package me.pluginhirai.minecraft_Plugin.randomteleport.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RtpSettings {
    private final int cooldownSeconds;
    private final int countdownSeconds;
    private final int maxRadius;
    private final int maxTeleportAttempts;
    private final String warmupTeleportParticle;
    private final String successfulTeleportParticle;
    private final String warmupTeleportSound;
    private final String successfulTeleportSound;
    private final Set<Material> unsafeMaterials;
    private final String successTeleportMessage;
    private final String cooldownTeleportMessage;
    private final String noSafeLocationMessage;
    private final String warmupTeleportMessage;
    private final String countdownTeleportMessage;
    private final String noPermissionMessage;
    private final String playerCancelMovementTeleportMessage;
    private final String pendingTeleportMessage;

    public RtpSettings(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();

        this.cooldownSeconds = config.getInt("cooldown-seconds");
        this.countdownSeconds = config.getInt("countdown-seconds");
        this.maxRadius = config.getInt("max-radius");
        this.maxTeleportAttempts = config.getInt("max-attempts");
        this.warmupTeleportParticle = config.getString("particles.warmup-teleport-particle");
        this.successfulTeleportParticle = config.getString("particles.successful-teleport-particle");
        this.warmupTeleportSound = config.getString("sounds.warmup-teleport-sound");
        this.successfulTeleportSound = config.getString("sounds.successful-teleport-sound");
        this.unsafeMaterials = config.getStringList("unsafe-materials")
                .stream().map(Material::matchMaterial).filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Material.class)));
        this.successTeleportMessage =config.getString("messages.success");
        this.cooldownTeleportMessage =config.getString("messages.cooldown");
        this.noSafeLocationMessage = config.getString("messages.no_safe_location");
        this.warmupTeleportMessage = config.getString("messages.warmup-teleport-message");
        this.countdownTeleportMessage = config.getString("messages.countdown");
        this.noPermissionMessage = config.getString("no-permission");
        this.playerCancelMovementTeleportMessage = config.getString("messages.canceled-movement-teleport-message");
        this.pendingTeleportMessage = config.getString("messages.pending-teleport-message");
    }
    // Getters for configs
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }
    public int getCountdownSeconds() {
        return countdownSeconds;
    }
    public int getMaxRadius() {
        return maxRadius;
    }
    public int getMaxTeleportAttempts() {
        return maxTeleportAttempts;
    }
    // Getter for assets - sounds/particles
    public String getWarmupTeleportParticle() {
        return warmupTeleportParticle;
    }
    public String getSuccessfulTeleportParticle() {
        return successfulTeleportParticle;
    }
    public String getWarmupTeleportSound() {
        return warmupTeleportSound;
    }
    public String getSuccessfulTeleportSound() {
        return successfulTeleportSound;
    }
    public Set<Material> getUnsafeMaterials() {
        return unsafeMaterials;
    }
    public String getSuccessTeleportMessage() {
        return successTeleportMessage;
    }
    public String getCooldownTeleportMessage() {
        return cooldownTeleportMessage;
    }
    public String getNoSafeLocationMessage() {
        return noSafeLocationMessage;
    }
    public String getWarmupTeleportMessage() {
        return warmupTeleportMessage;
    }
    public String getCountdownTeleportMessage() {
        return countdownTeleportMessage;
    }
    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }
    public String getPlayerCancelMovementTeleportMessage() {
        return playerCancelMovementTeleportMessage;
    }
    public String getPendingTeleportMessage() {
        return pendingTeleportMessage;
    }
}
