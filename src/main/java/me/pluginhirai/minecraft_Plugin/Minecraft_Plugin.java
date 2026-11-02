package me.pluginhirai.minecraft_Plugin;

import me.pluginhirai.minecraft_Plugin.command.DamageCommand;
import me.pluginhirai.minecraft_Plugin.command.GreetCommand;
import me.pluginhirai.minecraft_Plugin.command.HealCommand;
import me.pluginhirai.minecraft_Plugin.randomteleport.command.RandomTeleportCommand;
import me.pluginhirai.minecraft_Plugin.listener.JoinListener;
import me.pluginhirai.minecraft_Plugin.listener.QuitListener;
import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
import me.pluginhirai.minecraft_Plugin.randomteleport.cooldown.CooldownManager;
import me.pluginhirai.minecraft_Plugin.randomteleport.effect.RandomTeleportEffects;
import me.pluginhirai.minecraft_Plugin.randomteleport.gui.RtpMenu;
import me.pluginhirai.minecraft_Plugin.randomteleport.guiconfig.RtpGuiSettings;
import me.pluginhirai.minecraft_Plugin.randomteleport.listener.RtpMenuListener;
import me.pluginhirai.minecraft_Plugin.randomteleport.listener.RtpMoveListener;
import me.pluginhirai.minecraft_Plugin.randomteleport.listener.RtpQuitListener;
import me.pluginhirai.minecraft_Plugin.randomteleport.location.*;
import me.pluginhirai.minecraft_Plugin.randomteleport.service.RandomTeleportService;
import me.pluginhirai.minecraft_Plugin.randomteleport.warmup.WarmupManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public final class Minecraft_Plugin extends JavaPlugin {

    private WarmupManager warmupManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("=================================");
        getLogger().info("MINECRAFT_PLUGIN HAS STARTED!");
        getLogger().info("=================================");


        // Greet Command
        getCommand("greet").setExecutor(new GreetCommand());
        // Heal Command
        getCommand("heal").setExecutor(new HealCommand());
        // Damage Command
        getCommand("damage").setExecutor(new DamageCommand());
        // Rtp Command
        saveDefaultConfig();
            saveResource("gui.yml", false);
        // Get gui.yml
        File guiFile = new File(getDataFolder(), "gui.yml");
        FileConfiguration guiConfig = YamlConfiguration.loadConfiguration(guiFile);

        InputStream defaultStream = getResource("gui.yml");
        if (defaultStream != null) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            boolean changed = false;
            for (String key : defaults.getKeys(true)) {
                if (!defaults.isConfigurationSection(key) && !guiConfig.contains(key)) {
                    guiConfig.set(key, defaults.get(key));
                    changed = true;
                }
            }
            if (changed) {
                try {
                    guiConfig.save(guiFile);
                } catch (IOException e) {
                    getLogger().warning("Could not update gui.yml " + e.getMessage());
                }
            }
        }

        RtpSettings settings = new RtpSettings(this);
        SafetyChecker safetyChecker = new SafetyChecker(settings);
        OverworldStrategy overworldStrategy = new OverworldStrategy(this);
        NetherStrategy netherStrategy = new NetherStrategy(this, safetyChecker);
        EndStrategy endStrategy = new EndStrategy(this);
        LocationFinder locationFinder = new LocationFinder(settings, safetyChecker, overworldStrategy, netherStrategy, endStrategy);
        CooldownManager cooldownManager = new CooldownManager(settings);
        RandomTeleportEffects rtpEffects = new RandomTeleportEffects(settings);
        this.warmupManager = new WarmupManager(this, settings, rtpEffects);
        RandomTeleportService rtpService = new RandomTeleportService(
                settings,locationFinder,
                warmupManager, cooldownManager,
                rtpEffects);
        RtpGuiSettings guiSettings = new RtpGuiSettings(guiConfig);
        RtpMenu rtpMenu = new RtpMenu(guiSettings);
        RandomTeleportCommand rtpCommand = new RandomTeleportCommand(settings, rtpMenu);
        getCommand("rtp").setExecutor(rtpCommand);
        getServer().getPluginManager().registerEvents(new RtpMenuListener(guiSettings, rtpService), this);
        getServer().getPluginManager().registerEvents(new RtpMoveListener(warmupManager, rtpService), this);
        getServer().getPluginManager().registerEvents(new RtpQuitListener(warmupManager), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getLogger().info("Plugin Enabled");
        getLogger().info("gui.yml path: " + guiFile.getAbsolutePath());
        getLogger().info("exists: " + guiFile.exists());
        getLogger().info("keys: " + guiConfig.getKeys(true));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logicWarmupManager warmupManager;
        if (warmupManager != null)  {
            warmupManager.cancelAll();
        }
        getLogger().info("Plugin Disabled");
    }

}
