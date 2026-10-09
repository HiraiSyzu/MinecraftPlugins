package me.pluginhirai.minecraft_Plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class GreetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        greetPlayer(sender, args);
        return true;
    }
    private void greetPlayer(CommandSender sender, String[] args) {
            if (args.length > 0) {
                sender.sendMessage("Hello " + args[0] + "!"); // Display Hello (any name you've used)
                // ex. /greet Hiraiya (Displays Hiraya)
            } else {
                sender.sendMessage("Hello " + sender.getName() + "!"); // Displays Hello CONSOLE
            }
    }
}
