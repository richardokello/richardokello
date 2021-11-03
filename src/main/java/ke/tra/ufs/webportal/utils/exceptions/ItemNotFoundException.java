package ke.tra.ufs.webportal.utils.exceptions;

public class ItemNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ItemNotFoundException() {
        super();
    }

    public ItemNotFoundException(String msg) {
        super(msg);
    }
}
