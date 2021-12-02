package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class reload {

    private CommandSender Sender;
    private Main main;

    reload(CommandSender sender,Main plugin){
        Sender = sender;
        main = plugin;
    }

    private void run(){
        main.onReload();
    }

    public void start(){
        if(!(Sender instanceof ConsoleCommandSender) ||
                !Config.getSuperAdministrators().contains(Sender.getName()) ||
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + "§c§l你没有使用该命令的权限");
            return;
        }
        run();
    }

}
