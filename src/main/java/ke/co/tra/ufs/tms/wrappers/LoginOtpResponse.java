package ke.co.tra.ufs.tms.wrappers;

import ke.co.tra.ufs.tms.entities.UfsUser;

import java.io.Serializable;
import java.util.List;

public class LoginOtpResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<String> permissions;
    public UfsUser userDetails;
}
