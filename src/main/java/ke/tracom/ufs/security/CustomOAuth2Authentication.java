/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tracom.ufs.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Collection;

/**
 *
 * @author Cornelius M
 */
public class CustomOAuth2Authentication extends OAuth2Authentication {
    
    private Collection<GrantedAuthority> authorities;
    

    public CustomOAuth2Authentication(OAuth2Request storedRequest, Authentication userAuthentication) {
        super(storedRequest, userAuthentication);
        authorities = (Collection<GrantedAuthority>) super.getUserAuthentication().getAuthorities();
    }
    
    public Collection<GrantedAuthority> getSimpleAuthorities(){
        return authorities;
    }

}
