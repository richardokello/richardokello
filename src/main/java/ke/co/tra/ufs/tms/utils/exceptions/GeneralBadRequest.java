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
public class GeneralBadRequest extends Exception {
    
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public GeneralBadRequest() {
        super();
    }

    public GeneralBadRequest(String msg){
        super(msg);
    }
    
    public GeneralBadRequest(String msg, HttpStatus status){
        super(msg);
        this.status = status;
    }
    
    public GeneralBadRequest setHttpStatus(HttpStatus status){
        this.status = status;
        return this;                
    }
    
    public HttpStatus getHttpStatus(){
        return status;
    }
    
    
}