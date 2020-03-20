/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "UFS_POS_USER")
@NamedQueries({
    @NamedQuery(name = "UfsPosUser.findAll", query = "SELECT u FROM UfsPosUser u")})
public class UfsPosUser implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "POS_USER_ID")
    private BigDecimal posUserId;
    @Basic(optional = false)
    @Column(name = "ACTIVE_STATUS")
    private String activeStatus;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "PIN_STATUS")
    private String pinStatus;
    @Column(name = "PIN_LAST_LOGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pinLastLogin;
    @Column(name = "PIN_LOGIN_ATTEMTPS")
    private BigInteger pinLoginAttemtps;
    @Column(name = "PIN_CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pinChangeDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomerOutlet userId;

    public UfsPosUser() {
    }

    public UfsPosUser(BigDecimal posUserId) {
        this.posUserId = posUserId;
    }

    public UfsPosUser(BigDecimal posUserId, String activeStatus, String username) {
        this.posUserId = posUserId;
        this.activeStatus = activeStatus;
        this.username = username;
    }

    public BigDecimal getPosUserId() {
        return posUserId;
    }

    public void setPosUserId(BigDecimal posUserId) {
        this.posUserId = posUserId;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPinStatus() {
        return pinStatus;
    }

    public void setPinStatus(String pinStatus) {
        this.pinStatus = pinStatus;
    }

    public Date getPinLastLogin() {
        return pinLastLogin;
    }

    public void setPinLastLogin(Date pinLastLogin) {
        this.pinLastLogin = pinLastLogin;
    }

    public BigInteger getPinLoginAttemtps() {
        return pinLoginAttemtps;
    }

    public void setPinLoginAttemtps(BigInteger pinLoginAttemtps) {
        this.pinLoginAttemtps = pinLoginAttemtps;
    }

    public Date getPinChangeDate() {
        return pinChangeDate;
    }

    public void setPinChangeDate(Date pinChangeDate) {
        this.pinChangeDate = pinChangeDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UfsCustomerOutlet getUserId() {
        return userId;
    }

    public void setUserId(UfsCustomerOutlet userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (posUserId != null ? posUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsPosUser)) {
            return false;
        }
        UfsPosUser other = (UfsPosUser) object;
        if ((this.posUserId == null && other.posUserId != null) || (this.posUserId != null && !this.posUserId.equals(other.posUserId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsPosUser[ posUserId=" + posUserId + " ]";
    }
    
}
