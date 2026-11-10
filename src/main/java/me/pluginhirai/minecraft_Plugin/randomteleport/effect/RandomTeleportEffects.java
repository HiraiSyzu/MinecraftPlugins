package me.pluginhirai.minecraft_Plugin.randomteleport.effect;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

public class RandomTeleportEffects {
    private final RtpSettings settings;

    public RandomTeleportEffects(RtpSettings settings) {
        this.settings = settings;
    }
    public void playWarmup(Player player) {
            // Load countdown sound
            player.playSound(player.getLocation(),
                    settings.getWarmupTeleportSound(),
                    2.0f,
                    1.0f
            );
            // import warmupParticle
            spawnParticle(player, settings.getWarmupTeleportParticle()); // count serves as the visibility of particle
    }
    public void playSuccess(Player player) {
       player.playSound(player.getLocation(),
               settings.getSuccessfulTeleportSound(),
               2.5f,
               1.0f
       );
       //import successfulParticle
        spawnParticle(player, settings.getSuccessfulTeleportParticle());
    }
    private void spawnParticle(Player player, String particleKey) {
        NamespacedKey key = NamespacedKey.fromString(particleKey);

        if (key == null) {
            return;
        }
        Particle particle = Registry.PARTICLE_TYPE.get(key);
        if (particle == null) {
            return;
        }
        player.getWorld().spawnParticle(particle, player.getLocation().add(0, 1, 0),
                35,
                0.5, 0.5, 0.5,
                0.0
        );
    }
}
