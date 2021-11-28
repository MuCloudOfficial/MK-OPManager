package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
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
        StringBuilder target = new StringBuilder();
        String password = Args.get(Args.size() - 1);
        if(password.equals(Config.Password)){
            Sender.sendMessage(Main.Prefix + "§c§l密码错误");
            return;
        }
        for(String s :Args.subList(1,Args.size() - 2)){
            target.append(s).append(" ");
        }
        if(!Config.BannedCommands.contains(target.toString())){
            Sender.sendMessage(Main.Prefix + "§c§l该命令没有被禁止");
            return;
        }
        Config.BannedCommands.remove(target.toString());
        Sender.sendMessage(Main.Prefix + "§a已清除一个禁止命令");
    }

    public void start() {
        if(!(Sender instanceof ConsoleCommandSender) ||
                !Config.SuperAdministrators.contains(Sender.getName()) ||
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + "§c§l你没有使用该命令的权限");
            return;
        }
        run();
    }
}
