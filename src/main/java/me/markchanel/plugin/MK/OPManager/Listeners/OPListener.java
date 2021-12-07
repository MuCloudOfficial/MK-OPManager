package me.markchanel.plugin.MK.OPManager.Listeners;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OPListener implements Listener {

    private final Main main;

    public OPListener(Main plugin){
        main = plugin;
    }

    // 检测玩家使用 /op 命令
    @EventHandler
    public void onListen(PlayerCommandPreprocessEvent pcpe){
        Player target = pcpe.getPlayer();
        if (pcpe.getMessage().substring(1).toLowerCase().startsWith("op")){
            if (!CentralController.getSuperAdministrators().contains(target.getName())){
                pcpe.setCancelled(true);
                target.sendMessage(Messages.OPDenied.getMessage());
            }
        }
    }
}
