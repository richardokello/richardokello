/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.utils.exceptions;

import org.springframework.security.authentication.DisabledException;

/**
 *
 * @author emuraya
 */
public class PasswordExpiryException extends DisabledException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PasswordExpiryException(String msg) {
        super(msg);
    }
}
