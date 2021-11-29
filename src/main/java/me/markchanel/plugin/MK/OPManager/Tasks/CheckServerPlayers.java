package me.markchanel.plugin.MK.OPManager.Tasks;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.Config;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckServerPlayers extends BukkitRunnable {

    private final Main main;

    public CheckServerPlayers(Main plugin){
        main = plugin;
    }

    @Override
    public void run() {
        for(Player target : main.getServer().getOnlinePlayers()){
            if(target.isOp() &&
                    !Config.OPs.containsKey(target.getName()) &&
                        !Config.SuperAdministrators.contains(target.getName())){
                target.sendMessage(Messages.OPCheckDenied.getMessage());
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,1));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,1));
                target.setOp(false);
                if(target.getGameMode() == GameMode.CREATIVE){
                    target.setGameMode(GameMode.SURVIVAL);
                }
            }
        }
    }

}
