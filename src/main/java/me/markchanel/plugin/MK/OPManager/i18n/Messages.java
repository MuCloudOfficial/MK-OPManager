package me.markchanel.plugin.MK.OPManager.i18n;

public enum Messages {

    EnablingPlugin("Messages.EnablingPlugin.Lost"),
    EnabledPlugin("Messages.EnabledPlugin.Lost"),
    DisablingPlugin("Messages.DisablingPlugin.Lost"),
    DisabledPlugin("Messages.DisabledPlugin.Lost"),
    EssentialsDetected("Messages.EssentialsDetected.Lost"),
    NotDefinedPassword("Messages.NotDefinedPassword.Lost"),
    NotDefinedInterval("Messages.NotDefinedInterval.Lost"),
    ArgsNotEnough("Messages.ArgsNotEnough.Lost"),
    CommandDenied("Messages.CommandDenied.Lost"),
    OPDenied("Messages.OPDenied.Lost"),
    OPCheckDenied("Messages.OPCheckDenied.Lost"),
    OPTimeOut("Messages.OPTimeOut.Lost"),
    WrongPassword("Messages.WrongPassword.Lost"),
    AlreadyOperator("Messages.AlreadyOperator.Lost"),
    GiveOperator("Messages.GiveOperator.Lost"),
    GiveOperatorForPlayer("Messages.GiveOperatorForPlayer.Lost"),
    NotOperator("Messages.NotOperator.Lost"),
    RemoveOperator("Messages.RemoveOperator.Lost"),
    RemoveOperatorForPlayer("Messages.RemoveOperatorForPlayer.Lost"),
    AddedBannedCommand("Messages.AddedBannedCommand.Lost"),
    AlreadyBannedCommand("Messages.AlreadyBannedCommand.Lost"),
    NotBannedCommand("Messages.NotBannedCommand.Lost"),
    RemoveBannedCommand("Messages.RemoveBannedCommand.Lost"),
    ReloadedPlugin("Messages.ReloadedPlugin.Lost"),
    OPList("Messages.OPList.Lost"),
    PermissionDenied("Messages.PermissionDenied.Lost");

    private String message;

    Messages(String message) {
        this.message = message;
    }

    void setMessage(String message) {
        if(message == null)  return;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void clear(){
        message = "Messages." + name() + ".Lost";
    }
}
