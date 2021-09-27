package me.markchanel.plugin.MK.OPManager.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Messages {

    public static String CommandDenied;
    public static String OPDenied;
    public static String OPCheckDenied;
    public static String OPTimeOut;

    public static void setMessages(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(Config.ConfigFile);
            CommandDenied = StringConvert.convertOnlyColor(f.getString("Messages.CommandDenied"));
            OPDenied = StringConvert.convertOnlyColor(f.getString("Messages.OPDenied"));
            OPTimeOut = StringConvert.convertOnlyColor(f.getString("Messages.OPTimeOut"));
            OPCheckDenied = StringConvert.convertOnlyColor(f.getString("Messages.OPCheckDenied"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
