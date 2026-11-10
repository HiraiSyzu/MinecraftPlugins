package me.pluginhirai.minecraft_Plugin.randomteleport.cooldown;

import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final RtpSettings settings;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public CooldownManager(RtpSettings settings) {
        this.settings = settings;
    }
    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    public boolean isOnCooldown(Player player) {
        Long lastUsed = cooldowns.get(player.getUniqueId());
        if (lastUsed == null) {
            return false;
        }
        long cooldownMillis = settings.getCooldownSeconds() * 1000L;
        long elapsedMillis = System.currentTimeMillis() - lastUsed;
        return elapsedMillis < cooldownMillis;
    }
    public long getRemainingSeconds(Player player) {
        Long lastUsed = cooldowns.get(player.getUniqueId());
        if (lastUsed == null) {
            return 0;
        }
        long cooldownMillis = settings.getCooldownSeconds() * 1000L;
        long elapsedMillis = System.currentTimeMillis() - lastUsed;
        return Math.max(0, (cooldownMillis - elapsedMillis) / 1000);
    }
    public void clear(UUID uuid) {
        cooldowns.remove(uuid);
    }
}
