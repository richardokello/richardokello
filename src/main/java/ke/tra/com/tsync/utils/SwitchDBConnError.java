package ke.tra.com.tsync.utils;

public class SwitchDBConnError extends RuntimeException  {

    public SwitchDBConnError() {
        super("GatewayDatabase Connection Error: ");
    }
}
