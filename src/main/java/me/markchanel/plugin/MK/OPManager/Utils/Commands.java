package me.markchanel.plugin.MK.OPManager.Utils;

import me.markchanel.plugin.MK.OPManager.MKOPManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Commands implements CommandExecutor {

    private final MKOPManager main;

    public Commands(MKOPManager plugin){
        main = plugin;
    }

    public void sendHelpPage(CommandSender sender){
        sender.sendMessage(MKOPManager.Prefix + ChatColor.AQUA + "MK-OPManager");
        sender.sendMessage(MKOPManager.Prefix + ChatColor.AQUA + "Ver." + Config.Version);
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
                            sender.hasPermission("mkopmanager.addTempOP") ||
                                sender instanceof ConsoleCommandSender)) {
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
                    if (!ss[2].equals(Config.Password)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "密码错误.");
                        return true;
                    }
                    if (ss.length < 7) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "参数不足! 请重新输入.");
                        return true;
                    }
                    if (Config.SuperAdministrators.contains(ss[1]) ||
                            Config.OPs.containsKey(ss[1])){
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "该玩家已拥有管理员身份.");
                        return true;
                    }
                    Player target = main.getServer().getPlayer(ss[1]);
                    Config.OPs.put(ss[1], false);
                    Config.appendYamlConfig("Settings.TempWhiteList",ss[1]);

                    target.setOp(true);
                    target.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + sender.getName()  + "已赋予你 " + ChatColor.YELLOW + "临时管理员" + ChatColor.GREEN + " 身份." + ChatColor.YELLOW + "时长: " + ss[3] + "日" + ss[4] + "时" + ss[5] + "分" + ss[6] + "秒");
                    new BukkitRunnable() {
                        int tick = Integer.parseInt(ss[3]) * 24 * 3600 + Integer.parseInt(ss[4]) * 3600 + Integer.parseInt(ss[5]) * 60 + Integer.parseInt(ss[6]);
                        @Override
                        public void run() {
                            if (tick == 0) {
                                target.setOp(false);
                                Config.OPs.remove(target.getName());
                                target.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "时间已到,你已不再拥有管理员身份.");
                                this.cancel();
                            }
                            tick--;
                        }
                    }.runTaskTimer(main, 0, 20L);
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已加入了一个临时管理员.");
                    return true;
                }

                if (ss[0].equalsIgnoreCase("addOP")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            sender.hasPermission("mkopmanager.addOP") ||
                                sender instanceof ConsoleCommandSender)) {
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
                    if (!ss[2].equals(Config.Password)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "密码错误.");
                        return true;
                    }
                    if (Config.SuperAdministrators.contains(ss[1]) ||
                            Config.OPs.containsKey(ss[1])){
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "该玩家已拥有管理员身份.");
                        return true;
                    }
                    Player target = main.getServer().getPlayer(ss[1]);
                    target.setOp(true);
                    Config.OPs.put(ss[1], true);
                    Config.appendYamlConfig("Settings.WhiteList",ss[1]);
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已加入了一个管理员.");
                    target.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + sender.getName()  + "已赋予你 " + ChatColor.YELLOW + "管理员" + ChatColor.GREEN + " 身份.");
                    return true;
                }

                if (ss[0].equalsIgnoreCase("addCommand")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            sender.hasPermission("mkopmanager.addCommands") ||
                                sender instanceof ConsoleCommandSender)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你没有权限执行本操作.");
                        return true;
                    }
                    if (ss.length < 3) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "参数不足! 请重新输入.");
                        return true;
                    }
                    if (!ss[ss.length - 1].equals(Config.Password)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "密码错误.");
                        return true;
                    }
                    int time = 0;
                    StringBuilder result = new StringBuilder();
                    for (String a : ss) {
                        if(time == 1){
                            result = new StringBuilder(a);
                        }
                        if(time > 1 && time < ss.length-1){
                            result.append(" ").append(a);
                        }
                        time++;
                    }
                    if(Config.BannedCommands.contains(result.toString())){
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "该命令已经禁止过.");
                        return true;
                    }
                    Config.BannedCommands.add(result.toString());
                    Config.appendYamlConfig("Settings.BannedCommands",result.toString());
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已加入了一个禁止命令.");
                    return true;
                }

                if (ss[0].equalsIgnoreCase("reload")) {
                    if (!(Config.SuperAdministrators.contains(sender.getName()) ||
                            Config.OPs.containsKey(sender.getName()) ||
                                sender.hasPermission("mkopmanager.admin") ||
                                    sender instanceof ConsoleCommandSender)) {
                        sender.sendMessage(MKOPManager.Prefix + ChatColor.RED + "你没有权限执行本操作.");
                        return true;
                    }
                    main.onReload();
                    sender.sendMessage(MKOPManager.Prefix + ChatColor.GREEN + "命令执行成功! 已重载完毕.");
                    return true;
                }

            }
            sendHelpPage(sender);
            return true;
        }
        return false;
    }
}
