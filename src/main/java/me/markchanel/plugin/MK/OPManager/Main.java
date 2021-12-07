package me.markchanel.plugin.MK.OPManager;

import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
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
        c.startPlugin();
    }

    @Override
    public void onDisable(){
        c.disablePlugin();
    }

    public void onReload(){
        c.reloadPlugin();
    }
}
