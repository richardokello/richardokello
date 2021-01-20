/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

/**
 *
 * @author Cornelius M
 */
public class OTPAuthentication extends OAuth2Authentication{
//    private final Authentication a;
   
    
    public OTPAuthentication(OAuth2Request storedRequest, Authentication userAuthentication) {
        super(storedRequest, userAuthentication);
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return a.getAuthorities();
//    }
//
//    @Override
//    public Object getCredentials() {
//        return a.getCredentials();
//    }
//
//    @Override
//    public Object getDetails() {
//        return a.getDetails();
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return a.getPrincipal();
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return a.isAuthenticated();
//    }
//
//    @Override
//    public void setAuthenticated(boolean bln) throws IllegalArgumentException {
//        a.setAuthenticated(bln);
//    }
//
//    @Override
//    public String getName() {
//        return a.getName();
//    }

}
