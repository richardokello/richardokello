/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Owori Juma
 */
public class UserProfile {

    @NotNull
    @Size(min = 1, max = 150)
    private String fullName;
    @NotNull
    @Size(min = 1, max = 20)
    private String msisdn;
    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})", message = "Wrong email address format")
    private String emailAddress;

    public UserProfile() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
