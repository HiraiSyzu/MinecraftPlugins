package me.PluginHirai.minecraft_Plugin.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text("Welcome to Hirai Server"));
    }
}
