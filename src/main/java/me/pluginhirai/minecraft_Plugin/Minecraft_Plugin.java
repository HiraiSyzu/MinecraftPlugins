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
import me.pluginhirai.minecraft_Plugin.randomteleport.listener.RtpMoveListener;
import me.pluginhirai.minecraft_Plugin.randomteleport.listener.RtpQuitListener;
import me.pluginhirai.minecraft_Plugin.randomteleport.location.*;
import me.pluginhirai.minecraft_Plugin.randomteleport.service.RandomTeleportService;
import me.pluginhirai.minecraft_Plugin.randomteleport.warmup.WarmupManager;
import org.bukkit.plugin.java.JavaPlugin;


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
        RandomTeleportCommand rtpCommand = new RandomTeleportCommand(rtpService, settings);
        getCommand("rtp").setExecutor(rtpCommand);
        getServer().getPluginManager().registerEvents(new RtpMoveListener(warmupManager, rtpService), this);
        getServer().getPluginManager().registerEvents(new RtpQuitListener(warmupManager), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getLogger().info("Plugin Enabled");
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
