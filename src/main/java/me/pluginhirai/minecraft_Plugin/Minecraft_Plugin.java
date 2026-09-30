package me.pluginhirai.minecraft_Plugin;

import me.pluginhirai.minecraft_Plugin.commands.DamageCommand;
import me.pluginhirai.minecraft_Plugin.commands.GreetCommand;
import me.pluginhirai.minecraft_Plugin.commands.HealCommand;
import me.pluginhirai.minecraft_Plugin.randomteleport.command.RandomTeleportCommand;
import me.pluginhirai.minecraft_Plugin.listener.JoinListener;
import me.pluginhirai.minecraft_Plugin.listener.QuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minecraft_Plugin extends JavaPlugin {

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
        getCommand("Rtp").setExecutor(new RandomTeleportCommand());
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getLogger().info("Plugin Enabled");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Disabled");
    }
}
