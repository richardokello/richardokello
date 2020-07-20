/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_POS_USER")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsPosUser.findAll", query = "SELECT u FROM UfsPosUser u")
        , @NamedQuery(name = "UfsPosUser.findByPosUserId", query = "SELECT u FROM UfsPosUser u WHERE u.posUserId = :posUserId")
        , @NamedQuery(name = "UfsPosUser.findByActiveStatus", query = "SELECT u FROM UfsPosUser u WHERE u.activeStatus = :activeStatus")
        , @NamedQuery(name = "UfsPosUser.findByPin", query = "SELECT u FROM UfsPosUser u WHERE u.pin = :pin")
        , @NamedQuery(name = "UfsPosUser.findByPinStatus", query = "SELECT u FROM UfsPosUser u WHERE u.pinStatus = :pinStatus")
        , @NamedQuery(name = "UfsPosUser.findByPinLastLogin", query = "SELECT u FROM UfsPosUser u WHERE u.pinLastLogin = :pinLastLogin")
        , @NamedQuery(name = "UfsPosUser.findByPinLoginAttemtps", query = "SELECT u FROM UfsPosUser u WHERE u.pinLoginAttemtps = :pinLoginAttemtps")
        , @NamedQuery(name = "UfsPosUser.findByPinChangeDate", query = "SELECT u FROM UfsPosUser u WHERE u.pinChangeDate = :pinChangeDate")
        , @NamedQuery(name = "UfsPosUser.findByAction", query = "SELECT u FROM UfsPosUser u WHERE u.action = :action")
        , @NamedQuery(name = "UfsPosUser.findByActionStatus", query = "SELECT u FROM UfsPosUser u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsPosUser.findByIntrash", query = "SELECT u FROM UfsPosUser u WHERE u.intrash = :intrash")})
public class UfsPosUser implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @GenericGenerator(
            name = "POS_USER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "POS_USER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "POS_USER_SEQ")
    @Column(name = "POS_USER_ID")
    private BigDecimal posUserId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ACTIVE_STATUS")
    private String activeStatus;
    @Size(max = 255)
    @Column(name = "PIN")
    private String pin;
    @Size(max = 10)
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
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION",insertable = false)
    private String action;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "ACTION_STATUS",insertable = false)
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH",insertable = false)
    private String intrash;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID",insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private UfsUser userId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "USER_ID")
    private Long userIds;

    @JoinColumn(name = "OUTLET_ID",referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne
    private UfsCustomerOutlet outletId;

    @Column(name = "OUTLET_ID")
    @Basic(optional = true)
    private Long outletIds;

    public UfsPosUser() {
    }

    public UfsPosUser(BigDecimal posUserId) {
        this.posUserId = posUserId;
    }

    public UfsPosUser(BigDecimal posUserId, String activeStatus, String action, String actionStatus) {
        this.posUserId = posUserId;
        this.activeStatus = activeStatus;
        this.action = action;
        this.actionStatus = actionStatus;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setUserId(UfsUser userId) {
        this.userId = userId;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public Long getUserIds() {
        return userIds;
    }

    public void setUserIds(Long userIds) {
        this.userIds = userIds;
    }

    public UfsCustomerOutlet getOutletId() {
        return outletId;
    }

    public void setOutletId(UfsCustomerOutlet outletId) {
        this.outletId = outletId;
    }

    public Long getOutletIds() {
        return outletIds;
    }

    public void setOutletIds(Long outletIds) {
        this.outletIds = outletIds;
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
        return "tra.boa.entities.ufs.UfsPosUser[ posUserId=" + posUserId + " ]";
    }

}
