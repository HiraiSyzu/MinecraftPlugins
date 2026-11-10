package me.pluginhirai.minecraft_Plugin.randomteleport.gui;

import me.pluginhirai.minecraft_Plugin.randomteleport.guiconfig.RtpGuiSettings;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RtpMenuHolder implements InventoryHolder {
    private final RtpGuiSettings settings;
    private final Inventory inventory;
    public RtpMenuHolder(RtpGuiSettings settings) {
        this.settings = settings;
        this.inventory = Bukkit.createInventory(this, settings.getMenuRows() * 9,
                MiniMessage.miniMessage().deserialize(settings.getMenuTitle()));

    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
