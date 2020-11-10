package ke.tra.com.tsync.utils;

public enum NETWORKMESSAGETYPE {
    SIGNON(001), SIGNOFF(002), ECHO(301);
    private final int messageCode;

    private NETWORKMESSAGETYPE(int messageCode) {
        this.messageCode = messageCode;
    }

    public int getMessageCode() {
        return messageCode;
    }
}
