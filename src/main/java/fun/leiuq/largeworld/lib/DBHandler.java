package fun.leiuq.largeworld.lib;

import fun.leiuq.largeworld.LargeWorld;

public class DBHandler {
    public static boolean playerExist(LargeWorld plugin, String UUID){
        return plugin.getConfig().contains("Users."+ UUID);
    }

    public static int getLifes(LargeWorld plugin, String UUID) {
        return plugin.getConfig().getInt("Users."+ UUID +".lifes");
    }

    public static void setLifes(LargeWorld plugin, String UUID, int lifes) {
        plugin.getConfig().set("Users."+ UUID +".lifes", lifes);
        plugin.saveConfig();
    }

    public static final String getLastWords(LargeWorld plugin, String UUID) {
        return plugin.getConfig().getString("Users."+ UUID +".lastWords");
    }

    public static void setLastWords(LargeWorld plugin, String UUID, String last_words) {
        plugin.getConfig().set("Users."+ UUID +".last-words", last_words);
        plugin.saveConfig();
    }

    public static boolean createUser(LargeWorld plugin, String UUID) {
        if (plugin.getConfig().contains("Users."+ UUID)) {
            return false;
        }
        String root = "Users."+ UUID;
        plugin.getConfig().set(root+".lifes", 3);
        plugin.getConfig().set(root+".last-words", "§cHe can't put a word here :(");
        plugin.saveConfig();

        // Search user
        String isHere = plugin.getConfig().getString(root+".last-words");
        return isHere != "" || isHere != null;
    }
}
