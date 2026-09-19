package me.PluginHirai.minecraft_Plugin.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

public class QuitListener implements Listener{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // create component
        event.quitMessage(Component.text("Goodbye!"));
    }
}
