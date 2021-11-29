package me.markchanel.plugin.MK.OPManager.i18n;

import me.markchanel.plugin.MK.OPManager.Utils.Config;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Objects;

public class i18nManager{

    public void loadMessages(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(Config.ConfigFile);
            Messages.CommandDenied.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages.CommandDenied"))));
            Messages.OPDenied.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages.OPDenied"))));
            Messages.OPTimeOut.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages.OPTimeOut"))));
            Messages.OPCheckDenied.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages.OPCheckDenied"))));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}

