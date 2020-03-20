/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_AUTHENTICATION")
@NamedQueries({
    @NamedQuery(name = "UfsAuthentication.findAll", query = "SELECT u FROM UfsAuthentication u")})
public class UfsAuthentication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "AUTHENTICATION_ID")
    private Long authenticationId;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
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
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "USER_", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private UfsUser user;
    @JoinColumn(name = "AUTHENTICATION_TYPE", referencedColumnName = "TYPE_ID")
    @ManyToOne(optional = false)
    private UfsAuthenticationType authenticationType;

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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsUser getUser() {
        return user;
    }

    public void setUser(UfsUser user) {
        this.user = user;
    }

    public UfsAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(UfsAuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
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
        return "com.mycompany.oracleufs.UfsAuthentication[ authenticationId=" + authenticationId + " ]";
    }
    
}
