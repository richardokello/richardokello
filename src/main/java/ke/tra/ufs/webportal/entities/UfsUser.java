/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_USER")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsUser.findAll", query = "SELECT u FROM UfsUser u")
        , @NamedQuery(name = "UfsUser.findByUserId", query = "SELECT u FROM UfsUser u WHERE u.userId = :userId")
        , @NamedQuery(name = "UfsUser.findByFullName", query = "SELECT u FROM UfsUser u WHERE u.fullName = :fullName")
        , @NamedQuery(name = "UfsUser.findByDateOfBirth", query = "SELECT u FROM UfsUser u WHERE u.dateOfBirth = :dateOfBirth")
        , @NamedQuery(name = "UfsUser.findByAvatar", query = "SELECT u FROM UfsUser u WHERE u.avatar = :avatar")
        , @NamedQuery(name = "UfsUser.findByStatus", query = "SELECT u FROM UfsUser u WHERE u.status = :status")
        , @NamedQuery(name = "UfsUser.findByAction", query = "SELECT u FROM UfsUser u WHERE u.action = :action")
        , @NamedQuery(name = "UfsUser.findByActionStatus", query = "SELECT u FROM UfsUser u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsUser.findByIntrash", query = "SELECT u FROM UfsUser u WHERE u.intrash = :intrash")
        , @NamedQuery(name = "UfsUser.findByCreationDate", query = "SELECT u FROM UfsUser u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsUser.findByPhoneNumber", query = "SELECT u FROM UfsUser u WHERE u.phoneNumber = :phoneNumber")})
public class UfsUser implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FULL_NAME")
    private String fullName;
    @Size(max = 255)
    @Column(name = "AVATAR")
    private String avatar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private short status;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Size(max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @OneToMany(mappedBy = "uploadedBy")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsRegionsBatch> ufsRegionsBatchList;
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsDepartment departmentId;

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsUserWorkgroup> ufsUserWorkgroupList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsPasswordHistory> ufsPasswordHistoryList;
    @OneToMany(mappedBy = "userId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsAuditLog> ufsAuditLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UfsAuthentication> ufsAuthenticationList;
    @JoinColumn(name = "GENDER", referencedColumnName = "GENDER_ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsGender gender;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @JoinColumn(name = "USER_TYPE", referencedColumnName = "TYPE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsUserType userType;

    @Transient
    @ModifiableField
    private List<BigDecimal> userWorkgroupIds;


    @Transient
    @ModifiableField
    private List<BigDecimal> workgroupIds;


    @Transient
    private String email;

    @Column(name = "GENDER")
    @Filter
    private BigDecimal genderId;


    @Column(name = "USER_TYPE")
    private BigDecimal userTypeId;

    public UfsUser() {
    }

    public UfsUser(Long userId) {
        this.userId = userId;
    }

    public UfsUser(Long userId, String fullName, short status) {
        this.userId = userId;
        this.fullName = fullName;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @XmlTransient
    public List<UfsUserWorkgroup> getUfsUserWorkgroupList() {
        return ufsUserWorkgroupList;
    }

    public void setUfsUserWorkgroupList(List<UfsUserWorkgroup> ufsUserWorkgroupList) {
        this.ufsUserWorkgroupList = ufsUserWorkgroupList;
    }

    @XmlTransient
    public List<UfsPasswordHistory> getUfsPasswordHistoryList() {
        return ufsPasswordHistoryList;
    }

    public void setUfsPasswordHistoryList(List<UfsPasswordHistory> ufsPasswordHistoryList) {
        this.ufsPasswordHistoryList = ufsPasswordHistoryList;
    }

    @XmlTransient
    public List<UfsAuditLog> getUfsAuditLogList() {
        return ufsAuditLogList;
    }

    public void setUfsAuditLogList(List<UfsAuditLog> ufsAuditLogList) {
        this.ufsAuditLogList = ufsAuditLogList;
    }

    @XmlTransient
    public List<UfsAuthentication> getUfsAuthenticationList() {
        return ufsAuthenticationList;
    }

    public void setUfsAuthenticationList(List<UfsAuthentication> ufsAuthenticationList) {
        this.ufsAuthenticationList = ufsAuthenticationList;
    }

    public UfsGender getGender() {
        return gender;
    }

    public void setGender(UfsGender gender) {
        this.gender = gender;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public UfsUserType getUserType() {
        return userType;
    }

    public void setUserType(UfsUserType userType) {
        this.userType = userType;
    }

    public List<BigDecimal> getUserWorkgroupIds() {
        return userWorkgroupIds;
    }

    public void setUserWorkgroupIds(List<BigDecimal> userWorkgroupIds) {
        this.userWorkgroupIds = userWorkgroupIds;
    }

    public List<BigDecimal> getWorkgroupIds() {
        return workgroupIds;
    }

    public void setWorkgroupIds(List<BigDecimal> workgroupIds) {
        this.workgroupIds = workgroupIds;
    }

    public String getEmail() {
        try {
            return this.ufsAuthenticationList.get(0).getUsername();
        } catch (Exception e) {
            return email;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getGenderId() {
        return genderId;
    }

    public void setGenderId(BigDecimal genderId) {
        this.genderId = genderId;
    }

    public BigDecimal getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(BigDecimal userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsUser)) {
            return false;
        }
        UfsUser other = (UfsUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsUser[ userId=" + userId + " ]";
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @XmlTransient
    @JsonIgnore
    public List<UfsRegionsBatch> getUfsRegionsBatchList() {
        return ufsRegionsBatchList;
    }

    public void setUfsRegionsBatchList(List<UfsRegionsBatch> ufsRegionsBatchList) {
        this.ufsRegionsBatchList = ufsRegionsBatchList;
    }

    public UfsDepartment getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UfsDepartment departmentId) {
        this.departmentId = departmentId;
    }

}
