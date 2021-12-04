package me.markchanel.plugin.MK.OPManager.Tasks;

import me.markchanel.plugin.MK.OPManager.Main;
import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;

public class CheckServerPlayers extends BukkitRunnable {

    private final Main main;
    private final CentralController cc;

    public CheckServerPlayers(Main plugin){
        main = plugin;
        cc = new CentralController();
    }

    @Override
    public void run() {
        for(Player target : main.getServer().getOnlinePlayers()){
            if(target.isOp() &&
                    !CentralController.getOPs().containsKey(target.getName()) &&
                        !CentralController.getSuperAdministrators().contains(target.getName())){
                target.sendMessage(Messages.OPCheckDenied.getMessage());
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,1));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,1));
                target.setOp(false);
                if(target.getGameMode() == GameMode.CREATIVE){
                    target.setGameMode(GameMode.SURVIVAL);
                }
                if(cc.getEssentialsLoaded()){
                    try {
                        Object EssInstance = cc.getEssentialsClass().getConstructor().newInstance();
                        Object userDataInstance = cc.getEssentialsUser().getConstructor(Player.class,cc.getEssentialsConfigClass()).newInstance(target,EssInstance);
                        Boolean isGodMode = (Boolean) cc.getEssentialsUser().getMethod("isGodModeEnabled").invoke(userDataInstance);
                        if(isGodMode){
                            cc.getEssentialsUser().getMethod("setGodModeEnabled", Boolean.class).invoke(userDataInstance,false);
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
