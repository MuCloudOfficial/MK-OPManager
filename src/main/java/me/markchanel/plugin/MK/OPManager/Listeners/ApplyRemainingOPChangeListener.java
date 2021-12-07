package me.markchanel.plugin.MK.OPManager.Listeners;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ApplyRemainingOPChangeListener implements Listener {

    @EventHandler
    public void onListen(PlayerJoinEvent pje){
        Player targetP = pje.getPlayer();
        if(!CentralController.getRemainingOPs().contains(targetP.getName())){
            return;
        }
        targetP.setOp(true);
        targetP.sendMessage(Main.Prefix + Messages.GiveOperatorForPlayer.getMessage());
    }

}
