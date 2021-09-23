package me.markchanel.plugin.MK.OPManager.Utils;

import me.markchanel.plugin.MK.OPManager.MKOPManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private final MKOPManager main;

    public Commands(MKOPManager plugin){
        main = plugin;
    }

    public void sendHelpPage(CommandSender sender){
        sender.sendMessage(MKOPManager.Prefix + ChatColor.AQUA + "MK-OPManager");
        sender.sendMessage(MKOPManager.Prefix + ChatColor.AQUA + "Version: " + Config.Version);
        sender.sendMessage(MKOPManager.Prefix + ChatColor.AQUA + "插件主页: https://gitee.com/markchanel/mk-opmanager");
        sender.sendMessage(MKOPManager.Prefix + ChatColor.AQUA + "作者: Mark_Chanel");
        sender.sendMessage(ChatColor.GOLD+"=============="+ChatColor.AQUA+"MK-OPManager 插件命令概述"+ChatColor.GOLD+"==============");
        sender.sendMessage(ChatColor.GOLD+"/mkom addTempOP [玩家] [密码] [时间]   "+ChatColor.AQUA+"添加一个临时管理员");
        sender.sendMessage(ChatColor.GOLD+"/mkom addOP [玩家] [密码]            "+ChatColor.AQUA+"添加一个管理员");
        sender.sendMessage(ChatColor.GOLD+"/mkom addCommand [命令] [密码]       "+ChatColor.AQUA+"添加一个命令,它将会禁止被所有玩家执行(除超级管理外)");
        sender.sendMessage(ChatColor.GOLD+"/mkom reload                     "+ChatColor.AQUA+"重载插件");
        sender.sendMessage(ChatColor.GOLD+"=============="+ChatColor.AQUA+"Designed By Mark_Chanel"+ChatColor.GOLD+"==============");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] ss){
        if(cmd.getName().equalsIgnoreCase("mkopmanager")){
            if(ss.length != 0) {
                if (ss[0].equalsIgnoreCase("addTempOP")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            Config.OPs.containsKey(sender.getName()) ||
                            sender.hasPermission("mkopmanager.addTempOP"))) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你没有权限执行本操作.");
                        return true;
                    }
                    if (ss.length < 3) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "参数不足! 请重新输入.");
                        return true;
                    }
                    if (!main.getServer().getOnlinePlayers().contains(main.getServer().getPlayer(ss[1]))) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你指定的玩家不存在或未在线.");
                        return true;
                    }
                    if (ss[2].equals(Config.Password)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "密码错误.");
                        return true;
                    }
                    if (ss.length < 7) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "参数不足! 请重新输入.");
                        return true;
                    }

                    Player target = main.getServer().getPlayer(ss[1]);
                    Config.OPs.put(ss[1], false);
                    target.setOp(true);
                    Config.updateConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            long tick = Long.parseLong(ss[2]) * 24 * 3600 + Long.parseLong(ss[3]) * 3600 + Long.parseLong(ss[4]) * 60 + Long.parseLong(ss[5]);
                            if (tick == 0) {
                                target.setOp(false);
                            }
                        }
                    }.runTaskTimer(main, 0, 20L);
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已加入了一个临时管理员.");
                    return true;
                }

                if (ss[0].equalsIgnoreCase("addOP")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            Config.OPs.containsKey(sender.getName()) ||
                            sender.hasPermission("mkopmanager.addOP"))) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你没有权限执行本操作.");
                        return true;
                    }
                    if (ss.length < 3) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "参数不足! 请重新输入.");
                        return true;
                    }
                    if (!main.getServer().getOnlinePlayers().contains(main.getServer().getPlayer(ss[1]))) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你指定的玩家不存在或未在线.");
                        return true;
                    }
                    if (ss[2].equals(Config.Password)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "密码错误.");
                        return true;
                    }
                    main.getServer().getPlayer(ss[1]).setOp(true);
                    Config.OPs.put(ss[1], true);
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已加入了一个管理员.");
                    return true;
                }

                if (ss[0].equalsIgnoreCase("addCommand")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            sender.hasPermission("mkopmanager.addCommands"))) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你没有权限执行本操作.");
                        return true;
                    }
                    if (ss.length < 3) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "参数不足! 请重新输入.");
                        return true;
                    }
                    if (ss[ss.length - 1].equals(Config.Password)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "密码错误.");
                        return true;
                    }
                    int time = 0;
                    String result = "";
                    for (String a : ss) {
                        if (time != 0 && time != ss.length - 1) {
                            result = a + "";
                        }
                        time++;
                    }
                    Config.BannedCommands.add(result);
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已加入了一个禁止命令.");
                    return true;
                }

                if (ss[0].equalsIgnoreCase("reload")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            Config.OPs.containsKey(sender.getName()))) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你没有权限执行本操作.");
                        return true;
                    }
                    main.onReload();
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已重载完毕.");
                    return true;
                }
            }else{
                sendHelpPage(sender);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] ss) {

        return null;
    }

}
