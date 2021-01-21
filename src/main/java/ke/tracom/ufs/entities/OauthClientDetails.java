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
import java.math.BigInteger;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "OAUTH_CLIENT_DETAILS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OauthClientDetails.findAll", query = "SELECT o FROM OauthClientDetails o")
    , @NamedQuery(name = "OauthClientDetails.findByClientId", query = "SELECT o FROM OauthClientDetails o WHERE o.clientId = :clientId")
    , @NamedQuery(name = "OauthClientDetails.findByResourceIds", query = "SELECT o FROM OauthClientDetails o WHERE o.resourceIds = :resourceIds")
    , @NamedQuery(name = "OauthClientDetails.findByClientSecret", query = "SELECT o FROM OauthClientDetails o WHERE o.clientSecret = :clientSecret")
    , @NamedQuery(name = "OauthClientDetails.findByScope", query = "SELECT o FROM OauthClientDetails o WHERE o.scope = :scope")
    , @NamedQuery(name = "OauthClientDetails.findByAuthorizedGrantTypes", query = "SELECT o FROM OauthClientDetails o WHERE o.authorizedGrantTypes = :authorizedGrantTypes")
    , @NamedQuery(name = "OauthClientDetails.findByWebServerRedirectUri", query = "SELECT o FROM OauthClientDetails o WHERE o.webServerRedirectUri = :webServerRedirectUri")
    , @NamedQuery(name = "OauthClientDetails.findByAuthorities", query = "SELECT o FROM OauthClientDetails o WHERE o.authorities = :authorities")
    , @NamedQuery(name = "OauthClientDetails.findByAccessTokenValidity", query = "SELECT o FROM OauthClientDetails o WHERE o.accessTokenValidity = :accessTokenValidity")
    , @NamedQuery(name = "OauthClientDetails.findByAutoapprove", query = "SELECT o FROM OauthClientDetails o WHERE o.autoapprove = :autoapprove")
    , @NamedQuery(name = "OauthClientDetails.findByRefreshTokenValidity", query = "SELECT o FROM OauthClientDetails o WHERE o.refreshTokenValidity = :refreshTokenValidity")
    , @NamedQuery(name = "OauthClientDetails.findByAdditionalInformation", query = "SELECT o FROM OauthClientDetails o WHERE o.additionalInformation = :additionalInformation")})
public class OauthClientDetails implements Serializable {

    @Size(max = 256)
    @Column(name = "RESOURCE_IDS")
    private String resourceIds;
    @Size(max = 256)
    @Column(name = "CLIENT_SECRET")
    private String clientSecret;
    @Size(max = 256)
    @Column(name = "SCOPE")
    private String scope;
    @Size(max = 256)
    @Column(name = "AUTHORIZED_GRANT_TYPES")
    private String authorizedGrantTypes;
    @Size(max = 256)
    @Column(name = "WEB_SERVER_REDIRECT_URI")
    private String webServerRedirectUri;
    @Size(max = 256)
    @Column(name = "AUTHORITIES")
    private String authorities;
    @Size(max = 256)
    @Column(name = "AUTOAPPROVE")
    private String autoapprove;
    @Size(max = 4000)
    @Column(name = "ADDITIONAL_INFORMATION")
    private String additionalInformation;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Column(name = "ACCESS_TOKEN_VALIDITY")
    private BigInteger accessTokenValidity;
    @Column(name = "REFRESH_TOKEN_VALIDITY")
    private BigInteger refreshTokenValidity;

    public OauthClientDetails() {
    }

    public OauthClientDetails(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }


    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }


    public BigInteger getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(BigInteger accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }


    public BigInteger getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(BigInteger refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientId != null ? clientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OauthClientDetails)) {
            return false;
        }
        OauthClientDetails other = (OauthClientDetails) object;
        if ((this.clientId == null && other.clientId != null) || (this.clientId != null && !this.clientId.equals(other.clientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.OauthClientDetails[ clientId=" + clientId + " ]";
    }

    

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getAutoapprove() {
        return autoapprove;
    }

    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove;
    }
    
}
