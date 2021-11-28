package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class delOP{

    private final CommandSender Sender;
    private final List<String> Args;

    delOP(CommandSender sender,String[] args){
        Sender = sender;
        Args = Arrays.asList(args);
    }

    public void run() {
        String targetName = Args.get(1);
        String password = Args.get(Args.size() - 1);
        if(password.equals(Config.Password)){
            Sender.sendMessage(Main.Prefix + "§c§l密码错误");
            return;
        }
        Player targetP = Bukkit.getPlayer(targetName);
        if(!Config.OPs.containsKey(targetName)){
            Sender.sendMessage(Main.Prefix + "§c§l该玩家不在管理员名单中.");
            return;
        }
        Config.OPs.remove(targetName);
        targetP.sendMessage("§e你的管理员身份已被 " + Sender.getName() + " 取消");
        targetP.setOp(false);
        Sender.sendMessage(Main.Prefix + "§a已移除了一个管理员");
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
