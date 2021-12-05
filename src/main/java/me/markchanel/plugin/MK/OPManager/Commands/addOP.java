package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import me.markchanel.plugin.MK.OPManager.i18n.StringConvert;
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
        if (!password.equals(CentralController.getPassword())) {
            Sender.sendMessage(Main.Prefix + Messages.WrongPassword.getMessage());
            return;
        }
        Player targetP = Bukkit.getPlayer(Args.get(1));
        if(CentralController.getOPs().containsKey(targetP.getName()) ||
                CentralController.getSuperAdministrators().contains(targetP.getName())){
            Sender.sendMessage(Main.Prefix + Messages.AlreadyOperator.getMessage());
            return;
        }
        Sender.sendMessage(Main.Prefix + Messages.GiveOperator.getMessage());
        CentralController.getOPs().put(targetP.getName(), true);
        targetP.setOp(true);
        targetP.sendMessage(StringConvert.convert(Messages.GiveOperatorForPlayer.getMessage(), "{player}",Sender.getName()));
    }

    public void start() {
        if(!(Sender instanceof ConsoleCommandSender) &&
                !CentralController.getSuperAdministrators().contains(Sender.getName()) &&
                !Sender.hasPermission("mkopmanager.admin")){
            Sender.sendMessage(Main.Prefix + Messages.PermissionDenied);
            return;
        }
        run();
    }

}
