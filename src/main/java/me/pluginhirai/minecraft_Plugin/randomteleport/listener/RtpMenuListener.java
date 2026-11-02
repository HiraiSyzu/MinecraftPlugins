package me.pluginhirai.minecraft_Plugin.randomteleport.listener;

import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import me.pluginhirai.minecraft_Plugin.randomteleport.gui.RtpMenuHolder;
import me.pluginhirai.minecraft_Plugin.randomteleport.guiconfig.RtpGuiSettings;
import me.pluginhirai.minecraft_Plugin.randomteleport.service.RandomTeleportService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class RtpMenuListener implements Listener {
    private final RtpGuiSettings guiSettings;
    private final RandomTeleportService rtpService;
    public RtpMenuListener(RtpGuiSettings guiSettings, RandomTeleportService rtpService) {
        this.guiSettings = guiSettings;
        this.rtpService  = rtpService;
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        // Get Holder
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof RtpMenuHolder)) {
            return;
        }
        event.setCancelled(true); // canceled

        // Check if Clicker is a Player
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (event.getRawSlot() >= event.getInventory().getSize()) {
            return;
        }
        int slot = event.getRawSlot();
        String worldName;
        if (slot == guiSettings.getOverworldSlot()) {
            worldName = guiSettings.getOverworldWorld();
        } else if (slot == guiSettings.getNetherSlot()) {
            if (!guiSettings.isNetherEnabled()) {
                player.sendMessage(guiSettings.getWorldDisabledMessage());
                return;
            }
            worldName = guiSettings.getNetherWorld  ();
        } else if (slot == guiSettings.getEndSlot()) {
            if (!guiSettings.isEndEnabled()) {
                player.sendMessage(guiSettings.getWorldDisabledMessage());
                return;
            }
            worldName = guiSettings.getEndWorld();
        }else {
            return;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("That World isn't Accessible");
            return;
        }
        player.closeInventory();
        rtpService.requestTeleport(player, world);
    }
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof RtpMenuHolder) {
            event.setCancelled(true);
        }
    }
}
