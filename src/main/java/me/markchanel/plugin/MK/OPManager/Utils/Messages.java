package me.markchanel.plugin.MK.OPManager.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Messages {

    public static String CommandDenied;
    public static String OPDenied;

    public static void setMessages(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(Config.ConfigFile);
            CommandDenied = f.getString("Messages.CommandDenied");
            OPDenied = f.getString("Messages.OPDenied");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
