package me.markchanel.plugin.MK.OPManager.Listeners;

import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandReceiveListeners implements Listener {

    // 检测玩家发出的命令
    @EventHandler
    public void onListen(PlayerCommandPreprocessEvent pcpe){
        Player target = pcpe.getPlayer();
        for(String s : CentralController.getBannedCommands()){
            if(pcpe.getMessage().substring(1).toLowerCase().startsWith(s)){
                if(!CentralController.getSuperAdministrators().contains(pcpe.getPlayer().getName())){
                    target.sendMessage(Messages.CommandDenied.getMessage());
                    pcpe.setCancelled(true);
                }
            }
        }
    }
}
