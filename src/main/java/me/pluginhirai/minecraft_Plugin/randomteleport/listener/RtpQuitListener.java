package me.pluginhirai.minecraft_Plugin.randomteleport.listener;
import me.pluginhirai.minecraft_Plugin.randomteleport.warmup.WarmupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RtpQuitListener implements Listener {
    private final WarmupManager warmupManager;
    public RtpQuitListener(WarmupManager warmupManager) {
        this.warmupManager = warmupManager;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        warmupManager.cancel(event.getPlayer());
    }
}
