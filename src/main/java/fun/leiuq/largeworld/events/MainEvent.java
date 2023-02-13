package fun.leiuq.largeworld.events;

import fun.leiuq.largeworld.LargeWorld;
import fun.leiuq.largeworld.lib.UUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import fun.leiuq.largeworld.lib.DBHandler;

import java.sql.Connection;

public class MainEvent implements Listener {
    LargeWorld plugin;
    public MainEvent(LargeWorld plugin) {
        this.plugin = plugin;
    }

    Connection SQLDB = null;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String name = p.getName();

        if(p.hasPermission("largeworld.invisiblejoin")){
            p.sendMessage(UUtils.getConfigMessage(plugin.getConfig(), "connect-silence"));
            return;
        }

        if(!DBHandler.playerExist(plugin, p.getUniqueId().toString())){
            if(DBHandler.createUser(plugin, p.getUniqueId().toString())){
                Bukkit.getLogger().info("Player " + name + " has joined the server. [FIRST CONNECT]");
            } else {
                Bukkit.getLogger().info("Player " + name + " has joined the server and failed to create new register [ERROR CREATE_USER]");
            }
        }

        // Join
        e.setJoinMessage(UUtils.getConfigMessage(plugin.getConfig(), "join", name));
    }


}
