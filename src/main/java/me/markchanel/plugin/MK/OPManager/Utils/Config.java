package me.markchanel.plugin.MK.OPManager.Utils;

import me.markchanel.plugin.MK.OPManager.MKOPManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

public class Config {

    private final MKOPManager main;
    public static final String Version = "BETA 000";
    public static File ConfigFolder;
    public static File ConfigFile;
    public static File saveFile;
    public static List<String> SuperAdministrators = new ArrayList<>();
    public static List<String> BannedCommands = new ArrayList<>();
    public static Map<String, Boolean> OPs = new HashMap<>();
    public static int CheckInterval;
    public static String Password;

    public Config(MKOPManager plugin){
        main = plugin;
        ConfigFolder = new File(main.getDataFolder().getAbsolutePath());
        ConfigFile = new File(ConfigFolder + File.separator + "config.yml");
        saveFile = new File(ConfigFolder + File.separator + "settings.yml");
    }

    public void startProcess(){
        checkIntegrity();
        loadConfig();
    }

    public void checkIntegrity(){
        if(!ConfigFile.getParentFile().exists()){
            ConfigFile.getParentFile().mkdir();
        }
        if(!ConfigFile.exists()){
            main.saveDefaultConfig();
        }
        if(!saveFile.exists()){
            try {
                saveFile.createNewFile();

                try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("settings.yml"); OutputStream os = new FileOutputStream(saveFile.getAbsolutePath())) {
                    byte[] buf = new byte[1024];
                    int temp;
                    while ((temp = is.read(buf)) > 0) {
                        os.write(buf, 0, temp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadConfig(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(saveFile);
            SuperAdministrators = f.getStringList("Settings.SuperAdministrators");
            BannedCommands = f.getStringList("Settings.BannedCommands");
            for(String s : f.getStringList("Settings.WhiteList")){
                OPs.put(s,true);
            }
            for(String s : f.getStringList("Settings.TempWhiteList")){
                OPs.put(s,false);
            }
            f.load(ConfigFile);
            CheckInterval = f.getInt("General.CheckInterval");
            Password = f.getString("General.Password");
            Messages.setMessages();
            cancelALLTempOP();
            showConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (NullPointerException npe){
            npe.printStackTrace();
            ConfigFile.renameTo(new File(ConfigFile.getParentFile().getAbsolutePath() + File.separator + "config.yml.old"));
            main.saveDefaultConfig();
            main.getServer().getConsoleSender().sendMessage(MKOPManager.Prefix + ChatColor.RED + "Config文件错误! 请检查Config.yml");
        }
    }

    public void cancelALLTempOP(){
        for(Map.Entry<String,Boolean> data : OPs.entrySet()){
            if(!data.getValue()){
                main.getServer().getPlayer(data.getKey()).setOp(false);
                OPs.remove(data.getKey(),data.getValue());
            }
        }
    }

    public static void updateConfig(){
        List<String> ops = new ArrayList<>();
        List<String> tempOps = new ArrayList<>();

        for(Map.Entry<String,Boolean> entry : OPs.entrySet()){
            if(entry.getValue()){
                ops.add(entry.getKey());
            }else{
                tempOps.add(entry.getKey());
            }
        }

        FileConfiguration f = new YamlConfiguration();
        try {
            f.load(saveFile);
            f.set("Settings.WhiteList",ops);
            f.set("Settings.TempWhiteList",tempOps);
            f.set("Settings.BannedCommands",BannedCommands);
            f.save(saveFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void showConfig(){
        main.getServer().getConsoleSender().sendMessage(String.valueOf(SuperAdministrators));
        main.getServer().getConsoleSender().sendMessage(String.valueOf(OPs));
        main.getServer().getConsoleSender().sendMessage(String.valueOf(BannedCommands));
        main.getServer().getConsoleSender().sendMessage(String.valueOf(Password));
        main.getServer().getConsoleSender().sendMessage(String.valueOf(CheckInterval));
    }

    public void reloadConfig(){
        checkIntegrity();
        updateConfig();
        OPs.clear();
        SuperAdministrators.clear();
        BannedCommands.clear();
        Password = null;
        CheckInterval = 0;
        loadConfig();
    }

    // 保存信息.
    public void saveConfig(){
        checkIntegrity();
        cancelALLTempOP();
        updateConfig();
        OPs.clear();
        SuperAdministrators.clear();
        BannedCommands.clear();
        Password = null;
    }
}
