package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import org.bukkit.command.CommandSender;

public class info{

    private final CommandSender Sender;

    info(CommandSender sender){
        Sender = sender;
    }

    public void run(){
        Sender.sendMessage("§7§l| §b§lMK§7§l-§e§lOPManager  " + "§6§lVer §b§l" + CentralController.getVersion());
        Sender.sendMessage("§7§l| §6作者: §7§lMark_Chanel");
        Sender.sendMessage("§7§l| §6插件主页: §b https://gitee.com/markchanel/mk-opmanager");
        Sender.sendMessage("§7§l| §7§m----------------------§b§l命令概述§7§m---------------------------");
        Sender.sendMessage("§7§l| §6 /mkom addTempOP [玩家] [密码] [时间]          添加一个临时管理员");
        Sender.sendMessage("§7§l| §6 /mkom addOP [玩家] [密码]                    添加一个管理员");
        Sender.sendMessage("§7§l| §6 /mkom delOP [玩家] [密码]                    添加一个管理员");
        Sender.sendMessage("§7§l| §6 /mkom addCommand [命令] [密码]               添加一个命令,它将会禁止被所有玩家执行(除超级管理外)");
        Sender.sendMessage("§7§l| §6 /mkom delCommand [命令] [密码]               添加一个命令,它将会禁止被所有玩家执行(除超级管理外)");
        Sender.sendMessage("§7§l| §6 /mkom reload                             重载插件");
        Sender.sendMessage("§7§l| §7§m-----------------------------------------------------------");
    }

    public void start(){
        run();
    }

}
