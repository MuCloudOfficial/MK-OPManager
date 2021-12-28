package me.markchanel.plugin.MK.OPManager.Commands;

import me.markchanel.plugin.MK.OPManager.Utils.CentralController;
import me.markchanel.plugin.MK.OPManager.i18n.i18nManager;
import org.bukkit.command.CommandSender;

public class info{

    private final CommandSender Sender;

    info(CommandSender sender){
        Sender = sender;
    }

    public void run(){
        switch (i18nManager.getLocale()){
            case "zh_CN":
                run_zh_CN();
                break;
            case "en_US":
                run_en_US();
                break;
        }
    }

    private void run_zh_CN(){
        Sender.sendMessage("§7§l| §b§lMK§7§l-§e§lOPManager  " + "§6§lVer §b§l" + CentralController.getVersion());
        Sender.sendMessage("§7§l| §6作者: §7§lManGo_MilkTea");
        Sender.sendMessage("§7§l| §6插件主页: §b https://gitee.com/ManGo_MilkTea/MK-OPManager");
        Sender.sendMessage("§7§l| §7§m-----------------------§b§l命令概述§7§m---------------------------");
        Sender.sendMessage("§7§l| §6 /mkom addTempOP [玩家] [密码] [时间]");
        Sender.sendMessage("§7§l| §6 添加一个临时管理员");
        Sender.sendMessage("§7§l| §6 /mkom addOP [玩家] [密码]");
        Sender.sendMessage("§7§l| §6 添加一个管理员");
        Sender.sendMessage("§7§l| §6 /mkom delOP [玩家] [密码]");
        Sender.sendMessage("§7§l| §6 移除一个管理员");
        Sender.sendMessage("§7§l| §6 /mkom addCommand [命令] [密码]");
        Sender.sendMessage("§7§l| §6 添加一个命令,它将会禁止被所有玩家执行(除超级管理外)");
        Sender.sendMessage("§7§l| §6 /mkom delCommand [命令] [密码]");
        Sender.sendMessage("§7§l| §6 从禁止命令中清除该命令");
        Sender.sendMessage("§7§l| §6 /mkom listOP");
        Sender.sendMessage("§7§l| §6 列出管理员名单");
        Sender.sendMessage("§7§l| §6 /mkom reload");
        Sender.sendMessage("§7§l| §6 重载插件");
        Sender.sendMessage("§7§l| §7§m-----------------------------------------------------------");
    }

    private void run_en_US(){
        Sender.sendMessage("§7§l| §b§lMK§7§l-§e§lOPManager  " + "§6§lVer §b§l" + CentralController.getVersion());
        Sender.sendMessage("§7§l| §6Author: §7§lManGo_MilkTea");
        Sender.sendMessage("§7§l| §6Website: §b https://gitee.com/ManGo_MilkTea/MK-OPManager");
        Sender.sendMessage("§7§l| §7§m----------------------§b§lCommands§7§m---------------------------");
        Sender.sendMessage("§7§l| §6 /mkom addTempOP [Player] [Password] [Time]          Add a Temporary Operator");
        Sender.sendMessage("§7§l| §6 /mkom addOP [Player] [Password]          Add a Operator");
        Sender.sendMessage("§7§l| §6 /mkom delOP [Player] [Password]          Del a Operator");
        Sender.sendMessage("§7§l| §6 /mkom addCommand [Command] [Password]      Add a command that will not be executed by all players (except Super Administrator)");
        Sender.sendMessage("§7§l| §6 /mkom delCommand [Command] [Password]      Clear the command from the inhibit command");
        Sender.sendMessage("§7§l| §6 /mkom listOP                 Display Operator List");
        Sender.sendMessage("§7§l| §6 /mkom reload                Reload Plugin");
        Sender.sendMessage("§7§l| §7§m-----------------------------------------------------------");
    }

    public void start(){
        run();
    }

}
