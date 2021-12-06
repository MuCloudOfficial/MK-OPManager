package me.markchanel.plugin.MK.OPManager;

import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static final String Prefix = "§7§l[§e§lMK§7§l-§b§lOPManager§7§l] ";
    private final CentralController c = new CentralController(this);

    @Override
    public void onLoad(){
        c.Preload();
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(Prefix + Messages.EnablingPlugin.getMessage());
        c.startPlugin();
        getServer().getConsoleSender().sendMessage(Prefix + Messages.EnabledPlugin.getMessage());
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(Prefix +  Messages.DisablingPlugin.getMessage());
        c.disablePlugin();
        getServer().getConsoleSender().sendMessage(Prefix + Messages.DisabledPlugin.getMessage());
    }

    public void onReload(){
        c.reloadPlugin();
        getServer().getConsoleSender().sendMessage(Prefix + Messages.ReloadedPlugin.getMessage());
    }
}
