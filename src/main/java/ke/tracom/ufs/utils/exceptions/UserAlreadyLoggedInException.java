package ke.tracom.ufs.utils.exceptions;

import org.springframework.security.authentication.AccountStatusException;

public class UserAlreadyLoggedInException extends AccountStatusException {
    public UserAlreadyLoggedInException(String msg) {
        super(msg);
    }

    public UserAlreadyLoggedInException(String msg, Throwable t) {
        super(msg, t);
    }

}
