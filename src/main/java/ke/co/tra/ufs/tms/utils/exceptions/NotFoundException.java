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
public class NotFoundException extends GeneralBadRequest{
    
    public NotFoundException(){
        super();
    }

    public NotFoundException(String msg, HttpStatus status) {
        super(msg, status);
    }
    
    public NotFoundException(String message){
        super(message);
    }

}
