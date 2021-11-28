package me.markchanel.plugin.MK.OPManager.Utils;

import me.markchanel.plugin.MK.OPManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

public class Config {

    private final Main main;
    public static String Version = null;
    public static File ConfigFolder;
    public static File ConfigFile;
    public static File saveFile;
    public static List<String> SuperAdministrators = new ArrayList<>();
    public static List<String> BannedCommands = new ArrayList<>();
    public static Map<String, Boolean> OPs = new HashMap<>();
    public static int CheckInterval;
    public static String Password;

    public Config(Main plugin){
        main = plugin;
        ConfigFolder = new File(main.getDataFolder().getAbsolutePath());
        ConfigFile = new File(ConfigFolder + File.separator + "config.yml");
        saveFile = new File(ConfigFolder + File.separator + "settings.yml");
    }

    private void getVersion(){
        try {
            InputStream is = main.getClass().getClassLoader().getResourceAsStream("plugin.yml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length;
            while((length = is.read(buf)) != -1){
                baos.write(buf,0,length);
            }
            is.close();
            baos.close();
            LineNumberReader lnr = new LineNumberReader(new CharArrayReader(baos.toString("UTF-8").toCharArray()));
            Version = ((String) lnr.lines().toArray()[3]).substring(9);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startProcess(){
        getVersion();
        checkIntegrity();
        loadConfig();
    }

    private void checkIntegrity(){
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

    private void loadConfig(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(saveFile);
            SuperAdministrators = f.getStringList("Settings.SuperAdministrators");
            BannedCommands.addAll(f.getStringList("Settings.BannedCommands"));
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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (NullPointerException npe){
            npe.printStackTrace();
            ConfigFile.renameTo(new File(ConfigFile.getParentFile().getAbsolutePath() + File.separator + "config.yml.old"));
            main.saveDefaultConfig();
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + ChatColor.RED + "Config文件错误! 请检查Config.yml");
        }
    }

    private void cancelALLTempOP(){
        for(Map.Entry<String,Boolean> data : OPs.entrySet()){
            if(!data.getValue()){
                main.getServer().getPlayer(data.getKey()).setOp(false);
                OPs.remove(data.getKey(),data.getValue());
            }
        }
    }

    public void reloadConfig(){
        checkIntegrity();
        OPs.clear();
        SuperAdministrators.clear();
        BannedCommands.clear();
        Password = null;
        CheckInterval = 0;
        loadConfig();
    }

    public static void appendYamlConfig(String path,String value){
        FileConfiguration fc = new YamlConfiguration();
        try{
            fc.load(ConfigFile);
            fc.set(path,fc.getStringList(path).add(value));
            fc.save(ConfigFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    // 保存信息.
    public void saveConfig(){
        checkIntegrity();
        cancelALLTempOP();
        OPs.clear();
        SuperAdministrators.clear();
        BannedCommands.clear();
        Password = null;
    }
}
