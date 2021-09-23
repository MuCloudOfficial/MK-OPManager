package me.markchanel.plugin.MK.OPManager.Tasks;

import me.markchanel.plugin.MK.OPManager.MKOPManager;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
import me.markchanel.plugin.MK.OPManager.Utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckServerPlayers extends BukkitRunnable {

    private final MKOPManager main;

    public CheckServerPlayers(MKOPManager plugin){
        main = plugin;
    }

    @Override
    public void run() {
        for(Player target : main.getServer().getOnlinePlayers()){
            if(!(target.isOp() && Config.OPs.containsKey(target.getName()))){
                target.sendMessage(Messages.OPDenied);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,1));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,1));
                target.setOp(false);
            }
        }
    }

}
