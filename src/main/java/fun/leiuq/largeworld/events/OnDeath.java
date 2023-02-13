package fun.leiuq.largeworld.events;

import fun.leiuq.largeworld.LargeWorld;
import fun.leiuq.largeworld.lib.DBHandler;
import fun.leiuq.largeworld.lib.UUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class OnDeath implements Listener {

    LargeWorld plugin;
    public OnDeath(LargeWorld plugin) {
        this.plugin = plugin;
    }

    private void permaBan(String name){
        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(name, UUtils.raw_getConfigMessage(plugin.getConfig(), "eliminated-kick"), null, "LargeWorld - Console");
        Bukkit.getServer().getPlayer(name).kickPlayer(UUtils.raw_getConfigMessage(plugin.getConfig(), "eliminated-kick"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        String name = p.getName();

        if(p.hasPermission("largeworld.infinitedeath")){
            e.setKeepInventory(true);
            return;
        }

        e.setDeathMessage("");

        // Player head
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwnerProfile(p.getPlayerProfile());
        meta.setDisplayName(ChatColor.RED+name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Cabeza de jugador");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&eJugador:&6 %player_name%").replaceAll("%player_name%", name));
        lore.add("");
        meta.setLore(lore);
        item.setItemMeta(meta);
        e.getDrops().add(item);

        int lifes = DBHandler.getLifes(plugin, p.getUniqueId().toString());
        if(lifes != 999){
            if(lifes <= 0){
                DBHandler.setLifes(plugin, p.getUniqueId().toString(), lifes);
                permaBan(name);
                String ultimas_palabras = DBHandler.getLastWords(plugin, p.getUniqueId().toString());
                String message = "&e-----------------------------------------------\n\n&aJugador &6&l%player_name%&a ha &c&lmuerto&a para &e&osiempre\n&aUltimas palabras:\n&b%last_player%\n\n&e-----------------------------------------------".replaceAll("%player_name%", name).replaceAll("%last_player%", ultimas_palabras);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
            if(lifes >= 1){
                lifes--;
                DBHandler.setLifes(plugin, p.getUniqueId().toString(), lifes);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e-----------------------------------------------\n\n&aJugador &6&l%player_name%&a ha &c&lmuerto\n&e%restante%&2/&6%total% \n\n&e-----------------------------------------------").replaceAll("%player_name%", name).replaceAll("%restante%", lifes+"").replaceAll("%total%", plugin.getConfig().getInt("Config.max-lifes")+""));
            }
        }
    }
}
