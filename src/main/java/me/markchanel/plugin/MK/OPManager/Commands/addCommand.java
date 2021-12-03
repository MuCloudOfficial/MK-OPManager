package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.List;

public class addCommand{

    private final CommandSender Sender;
    private final List<String> Args;

    addCommand(CommandSender sender,String[] args){
        Sender = sender;
        Args = Arrays.asList(args);
    }

    public void run() {
        String password = Args.get(Args.size() - 1);
        if (!password.equals(CentralController.getPassword())) {
            Sender.sendMessage(Main.Prefix + "§c§l密码错误");
            return;
        }
        StringBuilder target = new StringBuilder();
        for(String s : Args.subList(1,Args.size() - 2)){
            target.append(s).append(" ");
        }
        if(CentralController.getBannedCommands().contains(target.toString())){
            Sender.sendMessage(Main.Prefix + "§4§l该命令已被禁止");
            return;
        }
        CentralController.getBannedCommands().add(target.toString());
        Sender.sendMessage(Main.Prefix + "§a你已添加了一项禁止命令");
    }

    public void start() {
        if(!(Sender instanceof ConsoleCommandSender) ||
                !CentralController.getSuperAdministrators().contains(Sender.getName()) ||
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + "§c§l你没有使用该命令的权限");
            return;
        }
        run();
    }
}
