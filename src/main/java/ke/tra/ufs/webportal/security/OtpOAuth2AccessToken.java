/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.ufs.webportal.security;


import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author eli.muraya
 */
public class OtpOAuth2AccessToken implements OAuth2AccessToken, Serializable {
    
    private final OAuth2AccessToken accessToken;
    private Map<String, Object> additionalInformation;

    public OtpOAuth2AccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
        this.additionalInformation = accessToken.getAdditionalInformation();
    }
    
    public OtpOAuth2AccessToken setAdditionalInformation(Map<String, Object> additionalInformation){
        this.additionalInformation = additionalInformation;
        return this;
    }
    
    @Override
    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    @Override
    public Set<String> getScope() {
        return accessToken.getScope();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return accessToken.getRefreshToken();
    }

    @Override
    public String getTokenType() {
        return accessToken.getTokenType();
    }

    @Override
    public boolean isExpired() {
        return accessToken.isExpired();
    }

    @Override
    public Date getExpiration() {
        return accessToken.getExpiration();
    }

    @Override
    public int getExpiresIn() {
        return accessToken.getExpiresIn();
    }

    @Override
    public String getValue() {
        return accessToken.getValue();
    }

}
