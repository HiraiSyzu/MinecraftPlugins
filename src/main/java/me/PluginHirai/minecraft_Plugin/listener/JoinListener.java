package me.PluginHirai.minecraft_Plugin.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(Component.text("Welcome to Hirai Server"));

        // Check if playerhasPlayedBefore()
        if (!player.hasPlayedBefore()) {
            givePlayerStarterKit(player);
        }
    }
    public void givePlayerStarterKit(Player player) {
        List<ItemStack> starterKit = List.of(
                new ItemStack(Material.IRON_SWORD),
                new ItemStack(Material.IRON_PICKAXE),
                new ItemStack(Material.IRON_AXE),
                new ItemStack(Material.IRON_SHOVEL),
                new ItemStack(Material.IRON_HOE),

                // Armors
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_BOOTS),

                // Misc
                new ItemStack(Material.COOKED_BEEF, 64),
                new ItemStack(Material.TORCH, 64)
        );
            // Give every item to player
        for (ItemStack item : starterKit) {
            player.getInventory().addItem(item);
        }
    }
}
