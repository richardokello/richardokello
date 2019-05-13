/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.cm.projects.spring.resource.chasis.annotations.ModifiableField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_AUTHENTICATION", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsAuthentication.findAll", query = "SELECT u FROM UfsAuthentication u")
        , @NamedQuery(name = "UfsAuthentication.findByAuthenticationId", query = "SELECT u FROM UfsAuthentication u WHERE u.authenticationId = :authenticationId")
        , @NamedQuery(name = "UfsAuthentication.findByUsername", query = "SELECT u FROM UfsAuthentication u WHERE u.username = :username")
        , @NamedQuery(name = "UfsAuthentication.findByPassword", query = "SELECT u FROM UfsAuthentication u WHERE u.password = :password")
        , @NamedQuery(name = "UfsAuthentication.findByPasswordStatus", query = "SELECT u FROM UfsAuthentication u WHERE u.passwordStatus = :passwordStatus")
        , @NamedQuery(name = "UfsAuthentication.findByLastLogin", query = "SELECT u FROM UfsAuthentication u WHERE u.lastLogin = :lastLogin")
        , @NamedQuery(name = "UfsAuthentication.findByLoginAttempts", query = "SELECT u FROM UfsAuthentication u WHERE u.loginAttempts = :loginAttempts")
        , @NamedQuery(name = "UfsAuthentication.findByPasswordChangeDate", query = "SELECT u FROM UfsAuthentication u WHERE u.passwordChangeDate = :passwordChangeDate")
        , @NamedQuery(name = "UfsAuthentication.findByIntrash", query = "SELECT u FROM UfsAuthentication u WHERE u.intrash = :intrash")})
public class UfsAuthentication implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "AUTHENTICATION_ID")
    private Long authenticationId;
    @Column(name = "PASSWORD_STATUS")
    private Short passwordStatus;
    @Column(name = "LAST_LOGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    @Column(name = "LOGIN_ATTEMPTS")
    private Short loginAttempts;
    @Column(name = "PASSWORD_CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangeDate;
    @Column(name = "USER_")
    private Long userId;
    @JoinColumn(name = "AUTHENTICATION_TYPE", referencedColumnName = "TYPE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsAuthenticationType authenticationType;
    @JoinColumn(name = "USER_", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsUser user;

    @ModifiableField
    @Column(name = "AUTHENTICATION_TYPE")
    private BigDecimal authenticationTypeId;

    public UfsAuthentication() {
    }

    public UfsAuthentication(Long authenticationId) {
        this.authenticationId = authenticationId;
    }

    public UfsAuthentication(Long authenticationId, String username, String password) {
        this.authenticationId = authenticationId;
        this.username = username;
        this.password = password;
    }

    public Long getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(Long authenticationId) {
        this.authenticationId = authenticationId;
    }


    public Short getPasswordStatus() {
        return passwordStatus;
    }

    public void setPasswordStatus(Short passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Short getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Short loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }


    public UfsAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(UfsAuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UfsUser getUser() {
        return user;
    }

    public void setUser(UfsUser user) {
        this.user = user;
    }

    public BigDecimal getAuthenticationTypeId() {
        return authenticationTypeId;
    }

    public void setAuthenticationTypeId(BigDecimal authenticationTypeId) {
        this.authenticationTypeId = authenticationTypeId;
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
        if (!(object instanceof UfsAuthentication)) {
            return false;
        }
        UfsAuthentication other = (UfsAuthentication) object;
        if ((this.authenticationId == null && other.authenticationId != null) || (this.authenticationId != null && !this.authenticationId.equals(other.authenticationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsAuthentication[ authenticationId=" + authenticationId + " ]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

}
