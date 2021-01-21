/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.wrappers;

import javax.validation.constraints.NotNull;

/**
 *
 * @author emuraya
 */
public class ResetPasswordWrapper {

    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;

    public ResetPasswordWrapper(){}
    public ResetPasswordWrapper(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
