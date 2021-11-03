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
public class PasswordReset {

    @Pattern(regexp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})", message = "Sorry invalid email address")
    @NotNull
    @Size(max = 200)
    private String email;

    public PasswordReset() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
