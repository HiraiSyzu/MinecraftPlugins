package me.PluginHirai.minecraft_Plugin;

import me.PluginHirai.minecraft_Plugin.commands.DamageCommand;
import me.PluginHirai.minecraft_Plugin.commands.GreetCommand;
import me.PluginHirai.minecraft_Plugin.commands.HealCommand;
import me.PluginHirai.minecraft_Plugin.listener.JoinListener;
import me.PluginHirai.minecraft_Plugin.listener.QuitListener;
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
