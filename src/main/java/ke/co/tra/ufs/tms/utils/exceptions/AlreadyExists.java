/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.utils.exceptions;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Owori Juma
 */
public class AlreadyExists extends GeneralBadRequest{

    public AlreadyExists(String msg) {
        super(msg);
    }

    public AlreadyExists(String msg, HttpStatus status) {
        super(msg, status);
    }
    
    
}
