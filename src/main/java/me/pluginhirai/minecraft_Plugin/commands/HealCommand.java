package me.pluginhirai.minecraft_Plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        onHealPlayer(sender);
        return true;
    }
    private void onHealPlayer(CommandSender sender) {
        if (sender instanceof Player player) {
            player.setHealth(Math.min(player.getHealth() + 5, 20)); // Maximum Heal 20
            sender.sendMessage("You've been Successfully healed ");
        } else {
            sender.sendRichMessage("<red> Only player can access this command!");
        }
    }
}
