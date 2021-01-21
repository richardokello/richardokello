/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "OAUTH_CLIENT_TOKEN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OauthClientToken.findAll", query = "SELECT o FROM OauthClientToken o")
    , @NamedQuery(name = "OauthClientToken.findByAuthenticationId", query = "SELECT o FROM OauthClientToken o WHERE o.authenticationId = :authenticationId")
    , @NamedQuery(name = "OauthClientToken.findByTokenId", query = "SELECT o FROM OauthClientToken o WHERE o.tokenId = :tokenId")
    , @NamedQuery(name = "OauthClientToken.findByUsername", query = "SELECT o FROM OauthClientToken o WHERE o.username = :username")
    , @NamedQuery(name = "OauthClientToken.findByClientId", query = "SELECT o FROM OauthClientToken o WHERE o.clientId = :clientId")})
public class OauthClientToken implements Serializable {

    @Size(max = 256)
    @Column(name = "TOKEN_ID")
    private String tokenId;
    @Size(max = 100)
    @Column(name = "USERNAME")
    private String username;
    @Size(max = 256)
    @Column(name = "CLIENT_ID")
    private String clientId;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "AUTHENTICATION_ID")
    private String authenticationId;
    @Lob
    @Column(name = "TOKEN")
    private Serializable token;

    public OauthClientToken() {
    }

    public OauthClientToken(String authenticationId) {
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

    public Serializable getToken() {
        return token;
    }

    public void setToken(Serializable token) {
        this.token = token;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
        if (!(object instanceof OauthClientToken)) {
            return false;
        }
        OauthClientToken other = (OauthClientToken) object;
        if ((this.authenticationId == null && other.authenticationId != null) || (this.authenticationId != null && !this.authenticationId.equals(other.authenticationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.OauthClientToken[ authenticationId=" + authenticationId + " ]";
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
