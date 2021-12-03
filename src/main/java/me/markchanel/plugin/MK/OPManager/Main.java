package me.markchanel.plugin.MK.OPManager;

import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static final String Prefix = "§7§l[§e§lMK§7§l-§b§lOPManager§7§l] ";
    private final CentralController c = new CentralController(this);

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(Prefix + "§b§l正在加载 MK-OPManager...");
        c.startPlugin();
        getServer().getConsoleSender().sendMessage(Prefix + "§b§l加载完毕!");
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(Prefix +  "§b§l正在关闭 MK-OPManager...");
        c.disablePlugin();
        getServer().getConsoleSender().sendMessage(Prefix + "§b§l卸载完毕!");
    }

    public void onReload(){
        c.reloadPlugin();
        getServer().getConsoleSender().sendMessage(Prefix + "§b§l重载完毕");
    }
}
