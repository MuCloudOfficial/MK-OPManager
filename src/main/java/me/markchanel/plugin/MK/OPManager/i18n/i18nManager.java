package me.markchanel.plugin.MK.OPManager.i18n;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class i18nManager{

    private Main main;
    private final List<String> availableLocales = new ArrayList<>();
    private static String Locale = null;
    private File MessageFile;

    public i18nManager(Main plugin) {
        main = plugin;
        availableLocales.add("zh_CN");
        availableLocales.add("en_US");
        availableLocales.add("zh_TW");
        setLocale();
    }

    public void setLocale(){
        try{
            FileConfiguration fc = new YamlConfiguration();
            fc.load(CentralController.getConfigFile());
            if(fc.get("General.Locale") == null || !availableLocales.contains(fc.getString("General.Locale"))){
                Bukkit.getServer().getConsoleSender().sendMessage(Main.Prefix + "§4§lLoad Error: Locale.  Will set en_US");
                Locale = availableLocales.get(1);
            }
            Locale = fc.getString("General.Locale");
            Bukkit.getServer().getConsoleSender().sendMessage(Main.Prefix+ "§b§lLocale §e§l" + Locale);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        MessageFile = new File(main.getDataFolder().getAbsoluteFile() + File.separator + "messages.yml");
        try {
            if(!MessageFile.exists()){
                MessageFile.createNewFile();
                try (InputStream is = main.getClass().getClassLoader().getResourceAsStream("resources/messages.yml"); OutputStream os = new FileOutputStream(MessageFile)) {
                    byte[] buf = new byte[1024];
                    int temp;
                    while ((temp = is.read(buf)) > 0) {
                        os.write(buf, 0, temp);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMessages(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(MessageFile);
            for(Messages m : Messages.values()){
                if(f.get("Messages." + Locale + "." + m.name()) == null){
                    main.getServer().getConsoleSender().sendMessage(Main.Prefix + "§b§lLoading Message:" + " §6" + m.name());
                    continue;
                }
                main.getServer().getConsoleSender().sendMessage(Main.Prefix + "§b§lLoading Message:" + " §a" + m.name());
                m.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages." + Locale + "." + m.name()))));
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getLocale(){
        return Locale;
    }

    public void clearMessages(){
        for(Messages m : Messages.values()){
            m.clear();
        }
    }

}

