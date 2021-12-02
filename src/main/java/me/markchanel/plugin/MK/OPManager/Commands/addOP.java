package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class addOP{

    private final CommandSender Sender;
    private final List<String> Args;

    addOP(CommandSender sender,String[] args){
        Sender = sender;
        Args = Arrays.asList(args);
    }

    public void run() {
        String password = Args.get(Args.size() - 1);
        if (!password.equals(Config.getPassword())) {
            Sender.sendMessage(Main.Prefix + "§c§l密码错误");
            return;
        }
        Player targetP = Bukkit.getPlayer(Args.get(1));
        if(Config.getOPs().containsKey(targetP.getName()) ||
                Config.getSuperAdministrators().contains(targetP.getName())){
            Sender.sendMessage(Main.Prefix + "§c§l该玩家已是管理员");
            return;
        }
        targetP.sendMessage("§e你已被 " + Sender.getName() + " 授予了管理员.");
        Config.getOPs().put(targetP.getName(), true);
    }

    public void start() {
        if(!(Sender instanceof ConsoleCommandSender) ||
                !Config.getSuperAdministrators().contains(Sender.getName()) ||
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + "§c§l你没有使用该命令的权限");
            return;
        }
        run();
    }

}
