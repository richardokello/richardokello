/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "OAUTH_ACCESS_TOKEN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OauthAccessToken.findAll", query = "SELECT o FROM OauthAccessToken o")
    , @NamedQuery(name = "OauthAccessToken.findByAuthenticationId", query = "SELECT o FROM OauthAccessToken o WHERE o.authenticationId = :authenticationId")
    , @NamedQuery(name = "OauthAccessToken.findByTokenId", query = "SELECT o FROM OauthAccessToken o WHERE o.tokenId = :tokenId")
    , @NamedQuery(name = "OauthAccessToken.findByUserName", query = "SELECT o FROM OauthAccessToken o WHERE o.userName = :userName")
    , @NamedQuery(name = "OauthAccessToken.findByClientId", query = "SELECT o FROM OauthAccessToken o WHERE o.clientId = :clientId")
    , @NamedQuery(name = "OauthAccessToken.findByRefreshToken", query = "SELECT o FROM OauthAccessToken o WHERE o.refreshToken = :refreshToken")})
public class OauthAccessToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "AUTHENTICATION_ID")
    private String authenticationId;
    @Size(max = 256)
    @Column(name = "TOKEN_ID")
    private String tokenId;
    @Size(max = 150)
    @Column(name = "USER_NAME")
    private String userName;
    @Size(max = 256)
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Size(max = 256)
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
    @Lob
    @Column(name = "TOKEN")
    private Serializable token;
    @Lob
    @Column(name = "AUTHENTICATION")
    private Serializable authentication;

    public OauthAccessToken() {
    }

    public OauthAccessToken(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Serializable getToken() {
        return token;
    }

    public void setToken(Serializable token) {
        this.token = token;
    }

    public Serializable getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Serializable authentication) {
        this.authentication = authentication;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authenticationId != null ? authenticationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OauthAccessToken)) {
            return false;
        }
        OauthAccessToken other = (OauthAccessToken) object;
        if ((this.authenticationId == null && other.authenticationId != null) || (this.authenticationId != null && !this.authenticationId.equals(other.authenticationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.OauthAccessToken[ authenticationId=" + authenticationId + " ]";
    }
    
}
