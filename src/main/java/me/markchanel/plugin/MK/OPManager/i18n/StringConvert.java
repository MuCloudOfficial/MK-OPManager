package me.markchanel.plugin.MK.OPManager.i18n;

import com.sun.istack.internal.NotNull;
import org.bukkit.ChatColor;

public class StringConvert {

    public static String convert(@NotNull String target, @NotNull String wantReplace,@NotNull String replaced){
        if(!target.contains(wantReplace)){
            return target;
        }
        target = target.replace(wantReplace,replaced);
        if(target.contains("&")){
            target = ChatColor.translateAlternateColorCodes('&',target);
        }
        return target;
    }

    public static String convertOnlyColor(@NotNull String target){
        if(target.contains("&")){
            target = ChatColor.translateAlternateColorCodes('&',target);
        }
        return target;
    }

}
