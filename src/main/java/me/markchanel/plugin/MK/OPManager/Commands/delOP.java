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

public class delOP{

    private final CommandSender Sender;
    private final List<String> Args;

    delOP(CommandSender sender,String[] args){
        Sender = sender;
        Args = Arrays.asList(args);
    }

    public void run() {
        if(Args.size() < 3){
            Sender.sendMessage(Main.Prefix + Messages.ArgsNotEnough.getMessage());
            return;
        }
        String targetName = Args.get(1);
        String password = Args.get(Args.size() - 1);
        if(password.equals(CentralController.getPassword())){
            Sender.sendMessage(Main.Prefix + Messages.WrongPassword.getMessage());
            return;
        }
        if(CentralController.getRemainingOPs().contains(Args.get(1))){
            CentralController.getRemainingOPs().remove(Args.get(1));
            Sender.sendMessage(Main.Prefix + Messages.RemoveOperator);
            return;
        }
        Player targetP = Bukkit.getPlayerExact(targetName);
        if(targetP == null){
            Sender.sendMessage(Main.Prefix + Messages.RemoveOperator);
            return;
        }
        if(!CentralController.getOPs().containsKey(targetName)){
            Sender.sendMessage(Main.Prefix + Messages.NotOperator.getMessage());
            return;
        }
        CentralController.getOPs().remove(targetName);
        Sender.sendMessage(Main.Prefix + Messages.RemoveOperator);
        targetP.setOp(false);
        targetP.sendMessage(StringConvert.convert(Messages.RemoveOperatorForPlayer.getMessage(),"{player}",Sender.getName()));
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
