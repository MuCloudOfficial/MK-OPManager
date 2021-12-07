package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Map;

public class listOP {

    private CommandSender Sender;

    listOP(CommandSender sender){
        Sender = sender;
    }

    private void run(){
        StringBuilder result = new StringBuilder();
        StringBuilder result2 = new StringBuilder();
        for(String name : CentralController.getSuperAdministrators()){
            result.append("§4§l").append(name).append("§f, ");
            result.deleteCharAt(result.lastIndexOf(","));
        }
        for(Map.Entry<String,Boolean> entry : CentralController.getOPs().entrySet()){
            if(entry.getValue()){
                result2.append("§a").append(entry.getKey()).append("§f, ");
            }else{
                result2.append("§e").append(entry.getKey()).append("§f, ");
                result2.deleteCharAt(result.lastIndexOf(","));
            }
        }
        Sender.sendMessage(Main.Prefix + Messages.OPList.getMessage());
        Sender.sendMessage(result.toString());
        Sender.sendMessage(result2.toString());
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
