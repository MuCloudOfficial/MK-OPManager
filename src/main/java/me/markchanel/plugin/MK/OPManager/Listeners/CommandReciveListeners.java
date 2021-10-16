package me.markchanel.plugin.MK.OPManager.Listeners;

import me.markchanel.plugin.MK.OPManager.Utils.Config;
import me.markchanel.plugin.MK.OPManager.Utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandReciveListeners implements Listener {

    // 检测玩家发出的命令
    @EventHandler
    public void CommandListen(PlayerCommandPreprocessEvent pcpe){
        Player target = pcpe.getPlayer();
        for(String s : Config.BannedCommands){
            if(pcpe.getMessage().substring(1).toLowerCase().startsWith(s)){
                if(!Config.SuperAdministrators.contains(pcpe.getPlayer().getName())){
                    target.sendMessage(Messages.CommandDenied);
                    pcpe.setCancelled(true);
                }
            }
        }
    }

    // 检测玩家使用 /op 命令
    @EventHandler
    public void OPListener(PlayerCommandPreprocessEvent pcpe){
        Player target = pcpe.getPlayer();
        if (pcpe.getMessage().substring(1).toLowerCase().startsWith("op")){
            if (!Config.SuperAdministrators.contains(target.getName())){
                pcpe.setCancelled(true);
                target.sendMessage(Messages.OPDenied);
            }
        }
    }

}
