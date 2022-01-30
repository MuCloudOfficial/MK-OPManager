package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.List;

public class delCommand{

    private final CommandSender Sender;
    private final List<String> Args;

    delCommand(CommandSender sender,String[] args){
        Sender = sender;
        Args = Arrays.asList(args);
    }

    public void run() {
        if(Args.size() < 3){
            Sender.sendMessage(Main.Prefix + Messages.ArgsNotEnough.getMessage());
            return;
        }
        StringBuilder target = new StringBuilder();
        String password = Args.get(Args.size() - 1);
        if(password.equals(CentralController.getPassword())){
            Sender.sendMessage(Main.Prefix + Messages.WrongPassword.getMessage());
            return;
        }
        for(String s :Args.subList(1,Args.size() - 2)){
            target.append(s).append(" ");
        }
        if(!CentralController.getBannedCommands().contains(target.toString())){
            Sender.sendMessage(Main.Prefix + Messages.NotBannedCommand.getMessage());
            return;
        }
        CentralController.modifyCommand(target.toString(),false);
        Sender.sendMessage(Main.Prefix + Messages.RemoveBannedCommand.getMessage());
    }

    public void start() {
        if(!(Sender instanceof ConsoleCommandSender) &&
                !CentralController.getSuperAdministrators().contains(Sender.getName()) &&
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + Messages.PermissionDenied.getMessage());
            return;
        }
        run();
    }
}
