package me.markchanel.plugin.MK.OPManager.Utils;

import me.markchanel.plugin.MK.OPManager.MKOPManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private final MKOPManager main;
    public static final String Version = "BETA 000";
    public static File ConfigFolder;
    public static File ConfigFile;
    public static List<String> SuperAdministrators = new ArrayList<>();
    public static List<String> BannedCommands = new ArrayList<>();
    public static Map<String, Boolean> OPs = new HashMap<>();
    public static int CheckInterval;
    public static String Password;

    public Config(MKOPManager plugin){
        main = plugin;
        ConfigFolder = new File(main.getDataFolder().getAbsolutePath());
        ConfigFile = new File(ConfigFolder + File.separator + "config.yml");
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
    }

    public void loadConfig(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(ConfigFile);
            SuperAdministrators = f.getStringList("SuperAdministrators");
            BannedCommands = f.getStringList("BannedCommands");
            CheckInterval = f.getInt("CheckInterval");
            Password = f.getString("Password");
            for(String s : f.getStringList("WhiteList")){
                OPs.put(s,true);
            }
            for(String s : f.getStringList("TempWhiteList")){
                OPs.put(s,false);
            }
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
        try {
            for(Map.Entry<String,Boolean> entry : OPs.entrySet()){
                if(entry.getValue()){
                    updateYaml("WhiteList",entry.getKey(), new HashMap<>(),ConfigFile.getAbsolutePath(),Yaml.class.newInstance());
                }else{
                    updateYaml("TempWhiteList",entry.getKey(),new HashMap<>(),ConfigFile.getAbsolutePath(),Yaml.class.newInstance());
                }
            }
            updateYaml("BannedCommands",BannedCommands,new HashMap<>(),ConfigFile.getAbsolutePath(),Yaml.class.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
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
        updateConfig();
        OPs.clear();
        SuperAdministrators.clear();
        BannedCommands.clear();
        Password = null;
        CheckInterval = 0;
        checkIntegrity();
        loadConfig();
    }

    // 保存信息.
    public void saveConfig(){
        cancelALLTempOP();
        updateConfig();
        OPs.clear();
        SuperAdministrators.clear();
        BannedCommands.clear();
        Password = null;
    }

    // org.yaml.snakeyaml 引用
    public static Object getValue(String key, Map<String, Object> yamlMap) {
        String[] keys = key.split("[.]");
        Object o = yamlMap.get(keys[0]);
        if (key.contains(".")) {
            if (o instanceof Map) {
                return getValue(key.substring(key.indexOf(".") + 1), (Map<String, Object>) o);
            } else {
                return null;
            }
        } else {
            return o;
        }
    }

    public static Map<String, Object> setValue(Map<String, Object> map, String key, Object value) {
        String[] keys = key.split("\\.");

        int len = keys.length;
        Map temp = map;
        for (int i = 0; i < len - 1; i++) {
            if (temp.containsKey(keys[i])) {
                temp = (Map) temp.get(keys[i]);
            } else {
                return null;
            }
            if (i == len - 2) {
                temp.put(keys[i + 1], value);
            }
        }
        for (int j = 0; j < len - 1; j++) {
            if (j == len - 1) {
                map.put(keys[j], temp);
            }
        }
        return map;
    }

    public static boolean updateYaml(String key, Object value, Map<String, Object> yamlToMap, String path, Yaml yaml) {
        Object oldVal = getValue(key, yamlToMap);

        if (null == oldVal) {
            return false;
        }

        try {
            Map<String, Object> resultMap = setValue(yamlToMap, key, value);
            if (resultMap != null) {
                yaml.dump(resultMap, new FileWriter(path));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("yaml file update failed !");
        }
        return false;
    }
}
