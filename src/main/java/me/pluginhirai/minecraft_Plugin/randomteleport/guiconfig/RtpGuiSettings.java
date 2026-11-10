package me.pluginhirai.minecraft_Plugin.randomteleport.guiconfig;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;

public class RtpGuiSettings {
private final int menuRows;
private final String menuTitle;
private final int overworldSlot;
private final Material overworldMaterial;
private final String overworldWorld;
private final int netherSlot;
private final Material netherMaterial;
private final String netherWorld;
private final int endSlot;
private final Material endMaterial;
private final String endWorld;
private final Material fillerMaterial;
private final String overworldWorldName;
private final String overworldDescriptionLore;
private final String overworldActionLore;
private final String netherWorldName;
private final String netherDescriptionLore;
private final String netherActionLore;
private final String endWorldName;
private final String endDescriptionLore;
private final String endActionLore;
private final boolean netherEnabled;
private final boolean endEnabled;
private final Material disabledMaterial;
private final String disabledName;
private final String disabledDescriptionLore;
private final String worldDisabledMessage;

    public RtpGuiSettings(FileConfiguration config) {
        this.menuRows = config.getInt("rtp-menu.rows", 3);
        this.menuTitle = config.getString("rtp-menu.title", "RtpMenu");
        this.overworldSlot = config.getInt("rtp-menu.overworld.slot");
        this.overworldMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.overworld.material", "GRASS_BLOCK")));
        this.overworldWorld = config.getString("rtp-menu.overworld.world");
        this.netherSlot = config.getInt("rtp-menu.nether.slot");
        this.netherMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.nether.material", "NETHERRACK")));
        this.netherWorld = config.getString("rtp-menu.nether.world");
        this.endSlot = config.getInt("rtp-menu.end.slot");
        this.endMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.end.material", "END_STONE")));
        this.endWorld = config.getString("rtp-menu.end.world");
        this.fillerMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.filler-material", "GRAY_STAINED_GLASS_PANE")));
        this.overworldWorldName = config.getString("rtp-menu.overworld.name");
        this.overworldDescriptionLore = config.getString("rtp-menu.overworld.lore.description-lore");
        this.overworldActionLore = config.getString("rtp-menu.overworld.lore.action-lore");
        this.netherWorldName = config.getString("rtp-menu.nether.name");
        this.netherDescriptionLore = config.getString("rtp-menu.nether.lore.description-lore");
        this.netherActionLore = config.getString("rtp-menu.nether.lore.action-lore");
        this.endWorldName = config.getString("rtp-menu.end.name");
        this.endDescriptionLore = config.getString("rtp-menu.end.lore.description-lore");
        this.endActionLore = config.getString("rtp-menu.end.lore.action-lore");
        this.netherEnabled = config.getBoolean("rtp-menu.nether.enabled");
        this.endEnabled = config.getBoolean("rtp-menu.end.enabled");
        this.disabledMaterial = Material.matchMaterial(Objects.requireNonNull(config.getString("rtp-menu.disabled.material", "BARRIER")));
        this.disabledName = config.getString("rtp-menu.disabled.name");
        this.disabledDescriptionLore = config.getString("rtp-menu.disabled.description-lore");
        this.worldDisabledMessage = config.getString("rtp-menu.messages.world-disabled");
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
    public String getOverworldWorld() {
        return overworldWorld;
    }
    public int getNetherSlot() {
        return netherSlot;
    }
    public Material getNetherMaterial() {
        return netherMaterial;
    }
    public String getNetherWorld() {
        return netherWorld;
    }
    public int getEndSlot() {
        return endSlot;
    }
    public Material getEndMaterial() {
        return endMaterial;
    }
    public String getEndWorld() {
        return endWorld;
    }
    public Material getFillerMaterial() {
        return fillerMaterial;
    }
    public String getOverworldWorldName() {
        return overworldWorldName;
    }
    public String getOverworldDescriptionLore() {
        return overworldDescriptionLore;
    }
    public String getOverworldActionLore() {
        return overworldActionLore;
    }
    public String getNetherWorldName() {
        return netherWorldName;
    }
    public String getNetherDescriptionLore() {
        return netherDescriptionLore;
    }
    public String getNetherActionLore() {
        return netherActionLore;
    }
    public String getEndWorldName() {
        return endWorldName;
    }
    public String getEndDescriptionLore() {
        return endDescriptionLore;
    }
    public String getEndActionLore(){
        return endActionLore;
    }
    public boolean isNetherEnabled() {
        return netherEnabled;
    }
    public boolean isEndEnabled() {
        return endEnabled;
    }
    public Material getDisabledMaterial() {
        return disabledMaterial;
    }
    public String getDisabledName() {
        return disabledName;
    }
    public String getDisabledDescriptionLore() {
        return disabledDescriptionLore;
    }
    public String getWorldDisabledMessage() {
        return worldDisabledMessage;
    }
}
