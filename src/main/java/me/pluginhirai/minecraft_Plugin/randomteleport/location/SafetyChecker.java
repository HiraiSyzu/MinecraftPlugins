package me.pluginhirai.minecraft_Plugin.randomteleport.location;

import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SafetyChecker {
    private final RtpSettings settings;

    public SafetyChecker(RtpSettings settings) {
        this.settings = settings;
    }
    public boolean isLocationSafe(Block groundBlock) {
        if (groundBlock.getWorld().getEnvironment() == World.Environment.NETHER && groundBlock.getY() >= 120) {
            return false;
        }
        Block headBlock = groundBlock.getRelative(0, 2, 0); // Pass the value of headBlock to groundBlock y + 2 is set to avoid suffocation
        Block footBlock = groundBlock.getRelative(0, 1, 0); // Pass the value of footBlock to groundBlock y + 1 so player can spawn above block

        if(settings.getUnsafeMaterials().contains(groundBlock.getType())) {
            return false; // Ground isn't safe
        }
        if (!headBlock.isReplaceable() || footBlock.getType().isSolid()) {
            return false; // player legs would spawn above solid block
        }
        if (!footBlock.isReplaceable() || headBlock.getType().isSolid()) {
            return false; // Player would suffocate
        }
        return true;
    }
}
