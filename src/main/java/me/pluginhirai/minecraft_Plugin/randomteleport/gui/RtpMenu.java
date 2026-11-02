package me.pluginhirai.minecraft_Plugin.randomteleport.gui;

import me.pluginhirai.minecraft_Plugin.randomteleport.guiconfig.RtpGuiSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RtpMenu {
    private final RtpGuiSettings settings;
    public RtpMenu(RtpGuiSettings settings) {
        this.settings = settings;
    }
    public void open(Player player) {
        RtpMenuHolder holder = new RtpMenuHolder(settings);
        Inventory inventory = holder.getInventory();
        // Overworld
        ItemStack overworldItem = createItem(settings.getOverworldMaterial(),
                settings.getOverworldWorldName(),
                settings.getOverworldDescriptionLore(),
                settings.getOverworldActionLore());
        // Nether
        ItemStack netherItem = createItem(settings.getNetherMaterial(),
                settings.getNetherWorldName(),
                settings.getNetherDescriptionLore(),
                settings.getNetherActionLore());
        // End
        ItemStack endItem = createItem(settings.getEndMaterial(),
                settings.getEndWorldName(),
                settings.getEndDescriptionLore(),
                settings.getEndActionLore());

        // used for filling empty slots (Aesthetics only)
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (slot == settings.getOverworldSlot() || slot == settings.getNetherSlot() || slot == settings.getEndSlot()) {
                continue;
            }
            inventory.setItem(slot, createFillerItem());
        }
        inventory.setItem(settings.getOverworldSlot(), overworldItem); // Overworld
        if (settings.isNetherEnabled()) {
            inventory.setItem(settings.getNetherSlot(), netherItem);
        } else {
            inventory.setItem(settings.getNetherSlot(), createDisabledItem());
        }
        if (settings.isEndEnabled()) {
            inventory.setItem(settings.getEndSlot(), endItem);
        } else {
            inventory.setItem(settings.getEndSlot(), createDisabledItem());
        }
        player.openInventory(inventory);
    }
    private ItemStack createItem(Material material, String name, String descriptionLore, String actionLore) {
        // Create itemStack
        ItemStack item = new ItemStack(material);
        // Create itemMeta
        ItemMeta meta = item.getItemMeta();
        MiniMessage miniMessage = MiniMessage.miniMessage();

        if (meta != null) {
            meta.displayName(miniMessage.deserialize("<!italic>"+ name));
            if (actionLore == null || actionLore.isBlank()) {
                meta.lore(List.of(miniMessage.deserialize("<!italic>" + descriptionLore)));
            } else {
                meta.lore(List.of(miniMessage.deserialize("<!italic>" + descriptionLore),
                        miniMessage.deserialize("<!italic>" + actionLore)));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    private ItemStack createDisabledItem() {
        return createItem(
                settings.getDisabledMaterial(),
                settings.getDisabledName(),
                settings.getDisabledDescriptionLore(),
                null
        );
    }
    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(settings.getFillerMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.empty());
            item.setItemMeta(meta);
        }
        return item;
    }
}
