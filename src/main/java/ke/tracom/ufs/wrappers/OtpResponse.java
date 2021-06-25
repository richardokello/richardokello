/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.wrappers;

import ke.tracom.ufs.security.CustomUserDetails;

import java.math.BigDecimal;
import java.util.Set;

/**
 *
 * @author eli.muraya
 * @author Kenny
 */
public class OtpResponse {
    
    public Set<String> permissions;
    
    public CustomUserDetails userDetails;

    public String tenantIds;

    public String userType;

    public String countyId;

    public String licenseMessage;
}
