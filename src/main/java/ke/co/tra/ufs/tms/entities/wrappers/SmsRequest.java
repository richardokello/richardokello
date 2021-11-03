/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Owori Juma
 */
public class SmsRequest {
    @JsonProperty("SMS")
    private String sms;
    @JsonProperty("Telephone")
    private String telephone;

    public SmsRequest() {
    }

    public SmsRequest(String sms, String telephone) {
        this.sms = sms;
        this.telephone = telephone;
    }
    
    

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    
    
}
