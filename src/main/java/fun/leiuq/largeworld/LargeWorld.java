package fun.leiuq.largeworld;

import fun.leiuq.largeworld.commands.MainCMD;
import fun.leiuq.largeworld.events.MainEvent;
import fun.leiuq.largeworld.events.OnDeath;
import fun.leiuq.largeworld.extras.PlaceholderAPILargeworld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;

public class LargeWorld extends JavaPlugin {

    private String rutaConfig;
    @Override
    public void onEnable() {
        // TODO: Console loggin
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY+"-----------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY+"");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD+"Plugin started!"+ChatColor.GRAY+"[LargeWorld]");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY+"");
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY+"-----------------------------");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderAPILargeworld(this).register();
            Bukkit.getLogger().info("PlaceholderAPI loaded!");
        }
        registerConfig();
        eventRegister();
        getServer().getPluginCommand("largeworld").setExecutor(new MainCMD(this));
    }

    private void eventRegister(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainEvent(this), this);
        pm.registerEvents(new OnDeath(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getServer().getConsoleSender().sendMessage(ChatColor.GRAY+"Adeu!");
    }

    public void registerConfig(){
        File config = new File(this.getDataFolder(),"config.yml");
        rutaConfig = config.getPath();
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}