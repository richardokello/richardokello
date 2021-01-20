/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.wrappers;

import ke.co.tra.ufs.tms.entities.UfsUser;

import java.util.List;

/**
 * @author Owori Juma
 */
public class OtpResponse {
    public List<String> permissions;
    public UfsUser userDetails;
    public boolean otpEnabled;
}
