package fun.leiuq.largeworld.lib;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class UUtils {
    public static String getConfigMessage(FileConfiguration config, String msg_id){
        return ChatColor.translateAlternateColorCodes('&', config.getString("Messages."+msg_id));
    }

    public static String getConfigMessage(FileConfiguration config, String msg_id, String name){
        return ChatColor.translateAlternateColorCodes('&', config.getString("Messages."+msg_id).replaceAll("%name%", name));
    }

    public static String raw_getConfigMessage(FileConfiguration config, String msg_id){
        return config.getString("Messages."+msg_id);
    }

    public static int getConfigInt(FileConfiguration config, String path){
        return config.getInt(path);
    }

    public static boolean getConfigBool(FileConfiguration config, String path){
        return config.getBoolean(path);
    }
}
