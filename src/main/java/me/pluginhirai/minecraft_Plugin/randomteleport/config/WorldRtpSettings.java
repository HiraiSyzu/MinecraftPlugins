package me.pluginhirai.minecraft_Plugin.randomteleport.config;
public class WorldRtpSettings {
    private final int maxRadius;
    private final int minY;
    private final int maxY;

    public WorldRtpSettings(int maxRadius, int minY, int maxY) {
        this.maxRadius = maxRadius;
        this.minY = minY;
        this.maxY = maxY;
    }
    public int getMaxRadius() {
        return maxRadius;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}
