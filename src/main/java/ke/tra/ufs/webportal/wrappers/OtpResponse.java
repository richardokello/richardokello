/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.wrappers;


import ke.tra.ufs.webportal.security.CustomUserDetails;

import java.util.Set;

/**
 *
 * @author eli.muraya
 */
public class OtpResponse {
    
    public Set<String> permissions;
    
    public CustomUserDetails userDetails;
}
