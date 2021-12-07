package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
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
        Sender.sendMessage(Main.Prefix + Messages.ReloadedPlugin.getMessage());
    }

    public void start(){
        if(!(Sender instanceof ConsoleCommandSender) &&
                !CentralController.getSuperAdministrators().contains(Sender.getName()) &&
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + Messages.PermissionDenied.getMessage());
            return;
        }
        run();
    }

}
