package ke.co.tra.ufs.tms.wrappers;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPinWrapper {

    private String username;
    private String pin;
    private String newpin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNewpin() {
        return newpin;
    }

    public void setNewpin(String newpin) {
        this.newpin = newpin;
    }
}
