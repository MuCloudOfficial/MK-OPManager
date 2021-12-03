package me.markchanel.plugin.MK.OPManager.Utils;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.i18n.i18nManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

public class CentralController {

    private final Main main;
    private i18nManager im = new i18nManager();
    private static String Version = null;
    private static File ConfigFolder;
    private static File ConfigFile;
    private static File SettingsFile;
    private static List<String> SuperAdministrators = new ArrayList<>();
    private static List<String> BannedCommands = new ArrayList<>();
    private static Map<String, Boolean> OPs = new HashMap<>();
    private static int CheckInterval;
    private static String Password;

    public CentralController(Main plugin){
        main = plugin;
        ConfigFolder = new File(main.getDataFolder().getAbsolutePath());
        ConfigFile = new File(ConfigFolder + File.separator + "config.yml");
        SettingsFile = new File(ConfigFolder + File.separator + "settings.yml");
    }

    private void loadVersion(){
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

    public void startPlugin(){
        loadVersion();
        checkIntegrity();
        loadMessages();
        loadConfig();
    }

    private void checkIntegrity(){
        try {
            if (!ConfigFolder.exists()){
                ConfigFolder.mkdir();
            }
            if (!ConfigFile.exists()) {
                main.saveDefaultConfig();
            }
            if (!SettingsFile.exists()) {
                SettingsFile.createNewFile();
                InputStream is = main.getClass().getClassLoader().getResourceAsStream("resources\\settings.yml");
                OutputStream os = new FileOutputStream(SettingsFile.getAbsolutePath());
                byte[] buf = new byte[1024];
                int temp;
                while((temp = is.read(buf)) > 0){
                    os.write(buf,0,temp);
                }
                is.close();
                os.close();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void loadMessages(){
        im.loadMessages();
    }

    private void loadConfig() {
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(ConfigFile);
            if (fc.get("General.Password") == null) {
                main.getServer().getConsoleSender().sendMessage(Main.Prefix + "§6§l未定义密码,已恢复至初始密码");
                fc.set("General.Password", "Mark_Chanel_Password");
                fc.save(ConfigFile);
            }
            Password = fc.getString("General.Password");

            if (fc.get("General.CheckInterval") == null) {
                main.getServer().getConsoleSender().sendMessage(Main.Prefix + "§6§l未定义检测时间,已恢复至初始状态");
                fc.set("General.CheckInterval", 1);
                fc.save(ConfigFile);
            }
            CheckInterval = fc.getInt("General.CheckInterval");

            fc.load(SettingsFile);
            if(fc.get("Settings.SuperAdministrator") != null){
                SuperAdministrators = fc.getStringList("Settings.SuperAdministrator");
            }
            if(fc.get("Settings.BannedCommands") != null){
                BannedCommands = fc.getStringList("Settings.BannedCommands");
            }
            if(fc.get("Settings.WhiteList") != null){
                for(String s : fc.getStringList("Settings.WhiteList")){
                    OPs.put(s,true);
                }
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadPlugin(){
        Password = null;
        CheckInterval = 0;
        OPs.clear();
        BannedCommands.clear();
        SuperAdministrators.clear();
        checkIntegrity();
        im.clearMessages();
        im.setLocale();
        im.loadMessages();
    }

    public void disablePlugin(){
        Password = null;
        CheckInterval = 0;
        OPs.clear();
        BannedCommands.clear();
        SuperAdministrators.clear();
        im.clearMessages();
    }

    public static String getVersion() {
        return Version;
    }

    public static File getConfigFolder() {
        return ConfigFolder;
    }

    public static File getConfigFile() {
        return ConfigFile;
    }

    public static File getSettingsFile() {
        return SettingsFile;
    }

    public static List<String> getBannedCommands() {
        return BannedCommands;
    }

    public static Map<String, Boolean> getOPs() {
        return OPs;
    }

    public static int getCheckInterval() {
        return CheckInterval;
    }

    public static String getPassword() {
        return Password;
    }

    private void disableAllTempOP(){

    }

    public static List<String> getSuperAdministrators(){
        return SuperAdministrators;
    }


}
