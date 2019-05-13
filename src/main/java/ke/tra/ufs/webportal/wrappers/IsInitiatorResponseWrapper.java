package ke.tra.ufs.webportal.wrappers;

import java.util.List;

public class IsInitiatorResponseWrapper {
    private List<Object> allowewd;
    private List<Object> notAllowed;

    public IsInitiatorResponseWrapper(){}
    public IsInitiatorResponseWrapper(List<Object> allowewd, List<Object> notAllowed) {
        this.allowewd = allowewd;
        this.notAllowed = notAllowed;
    }

    public List<Object> getAllowewd() {
        return allowewd;
    }

    public void setAllowewd(List<Object> allowewd) {
        this.allowewd = allowewd;
    }

    public List<Object> getNotAllowed() {
        return notAllowed;
    }

    public void setNotAllowed(List<Object> notAllowed) {
        this.notAllowed = notAllowed;
    }
}
