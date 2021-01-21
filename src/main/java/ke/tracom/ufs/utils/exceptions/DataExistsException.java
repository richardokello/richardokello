package ke.tracom.ufs.utils.exceptions;

import ke.axle.chassis.exceptions.GeneralBadRequest;

public class DataExistsException extends GeneralBadRequest {
    private static final long serialVersionUID = 1L;

    public DataExistsException() {
        super();
    }

    public DataExistsException(String msg) {
        super(msg);
    }
}
