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

    private final List<String> availableLocales = new ArrayList<>();
    private String Locale = null;
    private File MessageFile;

    public i18nManager(){
        availableLocales.add("zh_CN");
        availableLocales.add("en_US");
        availableLocales.add("zh_TW");
        readFile();
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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void readFile(){
        MessageFile = new File(CentralController.getConfigFile().getAbsoluteFile() + File.separator + "messages.yml");
        try {
            if(!MessageFile.exists()){
                MessageFile.createNewFile();
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("resources\\messages.yml");
                OutputStream os = new FileOutputStream(MessageFile.getAbsolutePath());
                byte[] buf = new byte[1024];
                int temp;
                while((temp = is.read(buf)) > 0){
                    os.write(buf,0,temp);
                }
                is.close();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMessages(){
        try {
            FileConfiguration f = new YamlConfiguration();
            f.load(MessageFile);
            Messages.CommandDenied.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages." + Locale + ".CommandDenied"))));
            Messages.OPDenied.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages." + Locale + ".OPDenied"))));
            Messages.OPTimeOut.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages." + Locale + ".OPTimeOut"))));
            Messages.OPCheckDenied.setMessage(StringConvert.convertOnlyColor(Objects.requireNonNull(f.getString("Messages." + Locale + ".OPCheckDenied"))));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void clearMessages(){
        Messages.CommandDenied.clear();
        Messages.OPDenied.clear();
        Messages.OPCheckDenied.clear();
        Messages.OPTimeOut.clear();
    }

}

