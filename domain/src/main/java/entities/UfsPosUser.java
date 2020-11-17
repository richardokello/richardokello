/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
 * @author cotuoma
 */
@Entity
@Table(name = "UFS_POS_USER")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsPosUser.findAll", query = "SELECT u FROM UfsPosUser u"),
        @NamedQuery(name = "UfsPosUser.findByPosUserId", query = "SELECT u FROM UfsPosUser u WHERE u.posUserId = :posUserId"),
        @NamedQuery(name = "UfsPosUser.findByActiveStatus", query = "SELECT u FROM UfsPosUser u WHERE u.activeStatus = :activeStatus"),
        @NamedQuery(name = "UfsPosUser.findByPin", query = "SELECT u FROM UfsPosUser u WHERE u.pin = :pin"),
        @NamedQuery(name = "UfsPosUser.findByPinStatus", query = "SELECT u FROM UfsPosUser u WHERE u.pinStatus = :pinStatus"),
        @NamedQuery(name = "UfsPosUser.findByPinLastLogin", query = "SELECT u FROM UfsPosUser u WHERE u.pinLastLogin = :pinLastLogin"),
        @NamedQuery(name = "UfsPosUser.findByPinLoginAttemtps", query = "SELECT u FROM UfsPosUser u WHERE u.pinLoginAttemtps = :pinLoginAttemtps"),
        @NamedQuery(name = "UfsPosUser.findByPinChangeDate", query = "SELECT u FROM UfsPosUser u WHERE u.pinChangeDate = :pinChangeDate"),
        @NamedQuery(name = "UfsPosUser.findByAction", query = "SELECT u FROM UfsPosUser u WHERE u.action = :action"),
        @NamedQuery(name = "UfsPosUser.findByActionStatus", query = "SELECT u FROM UfsPosUser u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsPosUser.findByIntrash", query = "SELECT u FROM UfsPosUser u WHERE u.intrash = :intrash"),
        @NamedQuery(name = "UfsPosUser.findByUsername", query = "SELECT u FROM UfsPosUser u WHERE u.username = :username"),
        @NamedQuery(name = "UfsPosUser.findByPosRole", query = "SELECT u FROM UfsPosUser u WHERE u.posRole = :posRole")})
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
    @Size(min = 1, max = 15)
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
    @Size(max = 10)
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "USERNAME")
    private String username;
    @Size(max = 30)
    @Column(name = "POS_ROLE")
    private String posRole;
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "DEVICE_ID", insertable = false, updatable = false)
    @ManyToOne
    private TmsDevice deviceId;

    @Column(name = "DEVICE_ID")
    private BigDecimal deviceIds;

    @JoinColumn(name = "CONTACT_PERSON_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsContactPerson contactPersonId;
    @Column(name = "CONTACT_PERSON_ID")
    private Long contactPersonIds;
    @JoinColumn(name = "CUSTOMER_OWNER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerOwners customerOwnerId;

    @Column(name = "CUSTOMER_OWNER_ID")
    private Long customerOwnerIds;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ID_NUMBER")
    private String idNumber;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "OTHERNAME")
    private String otherName;

    @Column(name="TID")
    private String tid;

    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "FIRST_TIME_USER")
    private Short firstTimeUser;


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

    public String getPosRole() {
        return posRole;
    }

    public void setPosRole(String posRole) {
        this.posRole = posRole;
    }

    public TmsDevice getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TmsDevice deviceId) {
        this.deviceId = deviceId;
    }

    public UfsContactPerson getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(UfsContactPerson contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public UfsCustomerOwners getCustomerOwnerId() {
        return customerOwnerId;
    }

    public void setCustomerOwnerId(UfsCustomerOwners customerOwnerId) {
        this.customerOwnerId = customerOwnerId;
    }

    public Long getContactPersonIds() {
        return contactPersonIds;
    }

    public void setContactPersonIds(Long contactPersonIds) {
        this.contactPersonIds = contactPersonIds;
    }

    public Long getCustomerOwnerIds() {
        return customerOwnerIds;
    }

    public void setCustomerOwnerIds(Long customerOwnerIds) {
        this.customerOwnerIds = customerOwnerIds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public BigDecimal getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(BigDecimal deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Short getFirstTimeUser() {
        return firstTimeUser;
    }

    public void setFirstTimeUser(Short firstTimeUser) {
        this.firstTimeUser = firstTimeUser;
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
        return "ke.tra.boa.ufs.entities.UfsPosUser[ posUserId=" + posUserId + " ]";
    }

}
