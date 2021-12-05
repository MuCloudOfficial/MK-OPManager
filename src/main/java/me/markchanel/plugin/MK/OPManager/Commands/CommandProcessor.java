package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Main;
import org.bukkit.command.*;

public class CommandProcessor implements CommandExecutor {

    private final Main main;

    public CommandProcessor(Main plugin){
        main = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] ss){
        String targetC = cmd.getName();
        if(targetC.equalsIgnoreCase("mkopmanager")){
            if(ss.length == 0) {
                new info(sender).start();
                return true;
            }
            String subCommand = ss[0];
            if(subCommand.equalsIgnoreCase("info")){
                new info(sender).start();
                return true;
            }
            if(subCommand.equalsIgnoreCase("addOP")){
                new addOP(sender,ss).start();
                return true;
            }
            if(subCommand.equalsIgnoreCase("addCommand")){
                new addCommand(sender,ss).start();
                return true;
            }
            if(subCommand.equalsIgnoreCase("delOP")){
                new delOP(sender,ss).start();
                return true;
            }
            if(subCommand.equalsIgnoreCase("delCommand")){
                new delCommand(sender,ss).start();
                return true;
            }
            if(subCommand.equalsIgnoreCase("listOP")){
                new listOP(sender).start();
                return true;
            }
            if(subCommand.equalsIgnoreCase("reload")){
               new reload(sender,main).start();
                return true;
            }
        }
        return true;
    }
}
