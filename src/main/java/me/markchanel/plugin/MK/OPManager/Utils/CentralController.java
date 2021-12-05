package me.markchanel.plugin.MK.OPManager.Utils;

import com.earth2me.essentials.User;
import me.markchanel.plugin.MK.OPManager.Commands.CommandProcessor;
import me.markchanel.plugin.MK.OPManager.Listeners.CommandReciveListeners;
import me.markchanel.plugin.MK.OPManager.Listeners.OPListener;
import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Tasks.CheckServerPlayers;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import me.markchanel.plugin.MK.OPManager.i18n.i18nManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;

import java.io.*;
import java.util.*;

public class CentralController {

    private Main main;
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

    private boolean EssentialLoaded = false;
    private Class<?> EssentialsClass;
    private Class<?> EssentialsConfigClass;
    private Class<?> EssentialsUserClass;

    private BukkitTask Task;

    public CentralController(Main plugin){
        main = plugin;
        ConfigFolder = new File(main.getDataFolder().getAbsolutePath());
        ConfigFile = new File(ConfigFolder + File.separator + "config.yml");
        SettingsFile = new File(ConfigFolder + File.separator + "settings.yml");
    }

    public CentralController(){}

    public void startPlugin(){
        loadVersion();
        checkIntegrity();
        loadConfig();
        loadCommand();
        loadListeners();
        launchTask();
        EssentialsHook();
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

    private void EssentialsHook(){
        if(main.getServer().getPluginManager().isPluginEnabled("Essentials")){
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + "§6§l已侦测到 Essentials");
            EssentialLoaded = true;
            EssentialsClass = Essentials.class;
            EssentialsConfigClass = IEssentials.class;
            EssentialsUserClass = User.class;
        }
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

    private void loadConfig() {
        FileConfiguration fc = new YamlConfiguration();
        try {
            fc.load(ConfigFile);
            if (fc.get("General.Password") == null) {
                main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.NotDefinedPassword.getMessage());
                fc.set("General.Password", "Mark_Chanel_Password");
                fc.save(ConfigFile);
            }
            Password = fc.getString("General.Password");

            if (fc.get("General.CheckInterval") == null) {
                main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.NotDefinedInterval.getMessage());
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

    private void launchTask(){
        Task = new CheckServerPlayers(main).runTaskTimer(main,0,CheckInterval *20L);
    }

    private void interruptTask(){
        Task.cancel();
        Task = null;
    }

    private void loadCommand(){
        Objects.requireNonNull(main.getServer().getPluginCommand("mkopmanager")).setExecutor(new CommandProcessor(main));
    }

    private void loadListeners(){
        Bukkit.getServer().getPluginManager().registerEvents(new CommandReciveListeners(),main);
        Bukkit.getServer().getPluginManager().registerEvents(new OPListener(main),main);
    }

    private void unloadListeners(){
        HandlerList.unregisterAll(main);
    }

    public void reloadPlugin(){
        Password = null;
        CheckInterval = 0;
        disableAllTempOP();
        OPs.clear();
        BannedCommands.clear();
        SuperAdministrators.clear();
        im.clearMessages();
        interruptTask();
        unloadListeners();

        EssentialLoaded = false;
        EssentialsUserClass = null;
        EssentialsConfigClass = null;

        checkIntegrity();
        EssentialsHook();
        loadConfig();
        im.setLocale();
        im.loadMessages();
        loadListeners();
        launchTask();
    }

    private void disableAllTempOP(){
        for(Map.Entry<String,Boolean> entry : OPs.entrySet()){
            if(!entry.getValue()){
                main.getServer().getPlayer(entry.getKey()).setOp(false);
            }
        }
    }

    public void disablePlugin(){
        Password = null;
        CheckInterval = 0;
        disableAllTempOP();
        OPs.clear();
        BannedCommands.clear();
        SuperAdministrators.clear();
        im.clearMessages();
        EssentialLoaded = false;
        EssentialsUserClass = null;
        EssentialsConfigClass = null;
        interruptTask();
        unloadListeners();
    }

    public Class<?> getEssentialsUser(){
        return EssentialsUserClass;
    }

    public Class<?> getEssentialsClass(){
        return EssentialsClass;
    }

    public Class<?> getEssentialsConfigClass(){
        return EssentialsConfigClass;
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

    public static List<String> getSuperAdministrators(){
        return SuperAdministrators;
    }

    public Boolean getEssentialsLoaded(){
        return EssentialLoaded;
    }

}
