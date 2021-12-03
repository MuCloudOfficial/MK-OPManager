package me.markchanel.plugin.MK.OPManager.i18n;

public enum Messages {

    CommandDenied("Messages.CommandDenied.Lost"),
    OPDenied("Messages.OPDenied.Lost"),
    OPCheckDenied("Messages.OPCheckDenied.Lost"),
    OPTimeOut("Messages.OPTimeOut.Lost");

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
