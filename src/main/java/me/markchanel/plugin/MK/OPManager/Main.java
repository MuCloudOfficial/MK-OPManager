package me.markchanel.plugin.MK.OPManager;

import me.markchanel.plugin.MK.OPManager.Listeners.CommandReciveListeners;
import me.markchanel.plugin.MK.OPManager.Tasks.CheckServerPlayers;
import me.markchanel.plugin.MK.OPManager.Commands.CommandProcessor;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Main extends JavaPlugin {

    public static final String Prefix = "§7§l[§e§lMK§7§l-§b§lOPManager§7§l] ";
    private final Config c = new Config(this);
    private BukkitTask CheckServerPlayersTask;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.AQUA + "正在加载 MK-OPManager...");
        c.startProcess();
        getCommand("mkopmanager").setExecutor(new CommandProcessor(this));
        getServer().getPluginManager().registerEvents(new CommandReciveListeners(),this);
        CheckServerPlayersTask = new CheckServerPlayers(this).runTaskTimer(this,0,Config.CheckInterval * 20L);
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.AQUA + "加载完毕!");
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.AQUA + "正在关闭 MK-OPManager...");
        c.saveConfig();
        HandlerList.unregisterAll(this);
        if(!CheckServerPlayersTask.isCancelled()){
            CheckServerPlayersTask.cancel();
        }
        getServer().getConsoleSender().sendMessage(Prefix + ChatColor.AQUA + "卸载完毕!");
    }

    public void onReload(){
        if(!CheckServerPlayersTask.isCancelled()){
            CheckServerPlayersTask.cancel();
        }
        HandlerList.unregisterAll(this);
        c.reloadConfig();
        getServer().getPluginManager().registerEvents(new CommandReciveListeners(),this);
        CheckServerPlayersTask = new CheckServerPlayers(this).runTaskTimer(this,0,Config.CheckInterval * 20L);
    }
}
