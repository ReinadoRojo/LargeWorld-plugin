package fun.leiuq.largeworld.commands;

import fun.leiuq.largeworld.LargeWorld;
import fun.leiuq.largeworld.lib.DBHandler;
import fun.leiuq.largeworld.lib.UUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCMD implements CommandExecutor, TabCompleter {

    private final LargeWorld plugin;
    public MainCMD(LargeWorld plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int type = 0;
        if(sender instanceof Player){
            sender = (Player) sender;
            type = 1;
        } else {
            sender = Bukkit.getConsoleSender();
        }

        if(args.length == 0){
            sender.sendMessage("§c/largeworld help");
            return true;
        }

        if(args[0].equalsIgnoreCase("help")){
            sender.sendMessage("§a/largeworld info <player>");
            sender.sendMessage("§a/largeworld help");
            sender.sendMessage("§a/largeworld up <palabras>");
            sender.sendMessage("§a/largeworld reload");
            return true;
        }

        if(args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                sender.sendMessage("§c/largeworld info <player>");
                return true;
            }
            // target
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§c" + args[1] + " is not online.");
                return true;
            }
            // Search in DB
            if (DBHandler.playerExist(plugin, target.getUniqueId().toString())) {
                int lifes = DBHandler.getLifes(plugin, target.getUniqueId().toString());
                Location pos = target.getLocation();
                List<String> msg = new ArrayList<>();
                msg.add("&8----------------------------");
                msg.add("&f");
                msg.add("&f");
                msg.add("&a Jugador %name% tiene %restante%❤ restantes ");
                if (sender.isOp()) {
                    msg.add("&f");
                    msg.add("&aPos: &eX" + pos.getX() + "&6 | &eY" + pos.getY() + "&6 | &eZ" + pos.getZ());
                }
                msg.add("&f");
                msg.add("&f");
                msg.add("&8----------------------------");
                for (String s : msg) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%name%", target.getName()).replaceAll("%restante%", lifes + "")));
                }
            }
        }
        
        if(args[0].equalsIgnoreCase("up") && type == 1){
            if (args.length == 1) {
                sender.sendMessage("§c/largeworld up <palabras>");
                return true;
            }
            // target
            for(int i=0; i<args.length; i++){
                String up = "";
                up = "%s%s ".formatted(up, args[i]);
                up.trim();
                DBHandler.setLastWords(plugin, ((Player) sender).getPlayer().getUniqueId().toString(), up);
            }
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")){
            plugin.reloadConfig();
            sender.sendMessage(UUtils.getConfigMessage(plugin.getConfig(), "reload-complete"));
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        int argsCount = args.length;

        if(argsCount == 0){
            suggestions.add("op");
            suggestions.add("info");
            suggestions.add("list");
            suggestions.add("kill");
            suggestions.add("help");
            return suggestions;
        }

        if(argsCount == 1){
            if(!args[0].equalsIgnoreCase("help") || !args[0].equalsIgnoreCase("list")){
                suggestions = sender.getServer().getOnlinePlayers().stream().map(Player::getName).collect(java.util.stream.Collectors.toList());
                return suggestions;
            }
            return suggestions;
        }

        return suggestions;
    }
}
