package me.pluginhirai.minecraft_Plugin.command;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
public class DamageCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        damagePlayer(sender,args);
        return true;
    }
    private void damagePlayer(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            double damageAmount = 4.0;
            player.damage(damageAmount);
            sender.sendMessage("Successfully damage yourself");
        }
    }
}
