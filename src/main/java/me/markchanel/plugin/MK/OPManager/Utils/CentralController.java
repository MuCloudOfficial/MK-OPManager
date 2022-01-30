package me.markchanel.plugin.MK.OPManager.Utils;

import com.earth2me.essentials.User;
import me.markchanel.plugin.MK.OPManager.Commands.CommandProcessor;
import me.markchanel.plugin.MK.OPManager.Listeners.ApplyRemainingOPChangeListener;
import me.markchanel.plugin.MK.OPManager.Listeners.CommandReceiveListeners;
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

    private final Main main;
    private i18nManager im;
    private static String Version = null;

    private static File ConfigFolder;
    private static File ConfigFile;
    private static File SettingsFile;

    private static List<String> SuperAdministrators = new ArrayList<>();
    private static List<String> BannedCommands = new ArrayList<>();
    private static Map<String, Boolean> OPs = new HashMap<>();
    private static List<String> RemainingOPs = new ArrayList<>();
    private static int CheckInterval;
    private static String Password;

    private boolean EssentialLoaded = false;
    private Class<?> EssentialsClass;
    private Class<?> EssentialsConfigClass;
    private Class<?> EssentialsUserClass;

    private BukkitTask Task;

    public CentralController(Main plugin){
        main = plugin;
    }

    public void Preload(){
        loadVersion();
        ConfigFolder = main.getDataFolder();
        ConfigFile = new File(main.getDataFolder().getAbsolutePath() + File.separator + "config.yml");
        SettingsFile = new File(main.getDataFolder().getAbsolutePath() + File.separator + "settings.yml");
        try {
            checkIntegrity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        im = new i18nManager(main);
        im.readFile();
        im.loadMessages();
    }

    public void startPlugin(){
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.EnablingPlugin.getMessage());
        EssentialsHook();
        loadConfig();
        loadCommand();
        loadListeners();
        launchTask();
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.EnabledPlugin.getMessage());
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
            main.getServer().getConsoleSender().sendMessage(Main.Prefix + "§6§lEssentials Detected.");
            EssentialLoaded = true;
            EssentialsClass = Essentials.class;
            EssentialsConfigClass = IEssentials.class;
            EssentialsUserClass = User.class;
        }
    }

    private void checkIntegrity() throws IOException{
        if (!ConfigFolder.exists()){
            ConfigFolder.mkdir();
        }
        if (!ConfigFile.exists()) {
            ConfigFile.createNewFile();
            try (InputStream is = main.getClass().getClassLoader().getResourceAsStream("resources/config.yml"); OutputStream os = new FileOutputStream(ConfigFile)) {
                byte[] buf = new byte[1024];
                int temp;
                while ((temp = is.read(buf)) > 0) {
                    os.write(buf, 0, temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!SettingsFile.exists()) {
            SettingsFile.createNewFile();
            try (InputStream is = main.getClass().getClassLoader().getResourceAsStream("resources/settings.yml"); OutputStream os = new FileOutputStream(SettingsFile)) {
                byte[] buf = new byte[1024];
                int temp;
                while ((temp = is.read(buf)) > 0) {
                    os.write(buf, 0, temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        Bukkit.getServer().getPluginManager().registerEvents(new CommandReceiveListeners(),main);
        Bukkit.getServer().getPluginManager().registerEvents(new OPListener(main),main);
        Bukkit.getServer().getPluginManager().registerEvents(new ApplyRemainingOPChangeListener(),main);
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

        try {
            checkIntegrity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EssentialsHook();
        loadConfig();
        im.setLocale();
        im.loadMessages();
        loadListeners();
        launchTask();
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.ReloadedPlugin.getMessage());
    }

    private void disableAllTempOP(){
        for(Map.Entry<String,Boolean> entry : OPs.entrySet()){
            if(!entry.getValue()){
                main.getServer().getPlayer(entry.getKey()).setOp(false);
            }
        }
    }

    public void disablePlugin(){
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.DisablingPlugin.getMessage());
        Password = null;
        CheckInterval = 0;
        disableAllTempOP();
        OPs.clear();
        BannedCommands.clear();
        SuperAdministrators.clear();
        EssentialLoaded = false;
        EssentialsUserClass = null;
        EssentialsConfigClass = null;
        interruptTask();
        unloadListeners();
        main.getServer().getConsoleSender().sendMessage(Main.Prefix + Messages.DisabledPlugin.getMessage());
        im.clearMessages();
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

    public static File getConfigFile() {
        return ConfigFile;
    }

    public static List<String> getBannedCommands() {
        return BannedCommands;
    }

    public static Map<String, Boolean> getOPs() {
        return OPs;
    }

    public static List<String> getRemainingOPs() {
        return RemainingOPs;
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

    public static void modifyOP(String playerName,boolean addTrueOrDeleteFalse){
        try {
            FileConfiguration fc = new YamlConfiguration();
            fc.load(SettingsFile);
            if(addTrueOrDeleteFalse){
                OPs.put(playerName,true);
                fc.set("WhiteList",OPs.keySet());
            }else{
                OPs.remove(playerName);
                fc.set("WhiteList",OPs.keySet());
            }
            fc.save(SettingsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void modifyCommand(String commandName,boolean addTrueOrDeleteFalse){
        try{
            FileConfiguration fc = new YamlConfiguration();
            fc.load(SettingsFile);
            if(addTrueOrDeleteFalse){
                BannedCommands.add(commandName);
                fc.set("BannedCommands",BannedCommands);
            }else{
                BannedCommands.remove(commandName);
                fc.set("BannedCommands",BannedCommands);
            }
            fc.save(SettingsFile);
        }catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

}
