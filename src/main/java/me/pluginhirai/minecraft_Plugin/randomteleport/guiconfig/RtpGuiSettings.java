package me.pluginhirai.minecraft_Plugin.randomteleport.guiconfig;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class RtpGuiSettings {
private final int menuRows;
private final String menuTitle;
private final int overworldSlot;
private final Material overworldMaterial;
private final int netherSlot;
private final Material netherMaterial;
private final int endSlot;
private final Material endMaterial;

    public RtpGuiSettings(FileConfiguration config) {
        this.menuRows = config.getInt("rtp-menu.rows");
        this.menuTitle = config.getString("rtp-menu.title");
        this.overworldSlot = config.getInt("rtp-menu.overworld.slot");
        this.overworldMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.overworld.material")));
        this.netherSlot = config.getInt("rtp-menu.nether.slot");
        this.netherMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.nether.material")));
        this.endSlot = config.getInt("rtp-menu.end.slot");
        this.endMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.end.material")));
    }
    public int getMenuRows() {
        return menuRows;
    }
    public String getMenuTitle() {
        return menuTitle;
    }

    public int getOverworldSlot() {
        return overworldSlot;
    }
    public Material getOverworldMaterial() {
        return overworldMaterial;
    }
    public int getNetherSlot() {
        return netherSlot;
    }
    public Material getNetherMaterial() {
        return netherMaterial;
    }
    public int getEndSlot() {
        return endSlot;
    }
    public Material getEndMaterial() {
        return endMaterial;
    }
}
