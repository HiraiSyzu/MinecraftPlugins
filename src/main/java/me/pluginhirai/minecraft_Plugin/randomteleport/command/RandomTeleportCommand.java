    package me.pluginhirai.minecraft_Plugin.randomteleport.command;
    import me.pluginhirai.minecraft_Plugin.randomteleport.config.RtpSettings;
    import me.pluginhirai.minecraft_Plugin.randomteleport.service.RandomTeleportService;
    import net.kyori.adventure.text.minimessage.MiniMessage;
    import org.bukkit.command.Command;
    import org.bukkit.command.CommandExecutor;
    import org.bukkit.command.CommandSender;
    import org.bukkit.entity.Player;


    public class RandomTeleportCommand implements CommandExecutor {
        private final RandomTeleportService rtpService;
        private final RtpSettings settings;

        // Constructor
        public RandomTeleportCommand(RandomTeleportService rtpService, RtpSettings settings) {
            this.settings = settings;
            this.rtpService = rtpService;
        }
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player player)) {
                return true;
            }
            if (!player.hasPermission("minecraft_plugin.rtp")) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(settings.getNoPermissionMessage()));
                return true;
            }
            rtpService.requestTeleport(player);
            return true;
        }
    }