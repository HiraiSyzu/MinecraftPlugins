package me.pluginhirai.minecraft_Plugin.randomteleport.listener;
import me.pluginhirai.minecraft_Plugin.randomteleport.command.RandomTeleportCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.UUID;

public class RtpMoveListener implements Listener {
    private final RandomTeleportCommand rtpCommand;

public RtpMoveListener(RandomTeleportCommand rtpCommand) {
    this.rtpCommand = rtpCommand;
}
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Cancel Rtp if player moved
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // Access map inside command class
        if (!rtpCommand.getPendingTeleport().contains(uuid)) {
            return;
        }

        if (event.getTo() == null) {
            return;
        }
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        rtpCommand.cancelMovementTeleport(event.getPlayer());
    }

}
