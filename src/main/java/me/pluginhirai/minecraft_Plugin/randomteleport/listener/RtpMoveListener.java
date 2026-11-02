package me.pluginhirai.minecraft_Plugin.randomteleport.listener;
import me.pluginhirai.minecraft_Plugin.randomteleport.service.RandomTeleportService;
import me.pluginhirai.minecraft_Plugin.randomteleport.warmup.WarmupManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RtpMoveListener implements Listener {
    private final WarmupManager warmupManager;
    private final RandomTeleportService rtpService;

public RtpMoveListener(WarmupManager warmupManager, RandomTeleportService rtpService) {
    this.warmupManager = warmupManager;
    this.rtpService = rtpService;
}
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Cancel Rtp if player moved
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        if (!warmupManager.isPending(player.getUniqueId())) {
            return;
        }
        if (warmupManager.cancel(player)) {
            rtpService.onWarmupCancelled(player);
        }

    }
}
