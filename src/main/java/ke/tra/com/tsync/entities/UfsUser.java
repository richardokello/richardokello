/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_USER")
@NamedQueries({
    @NamedQuery(name = "UfsUser.findAll", query = "SELECT u FROM UfsUser u")})
public class UfsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private BigDecimal userId;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "AVATAR")
    private String avatar;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Column(name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private short status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UfsPasswordHistory> ufsPasswordHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UfsUserBranchManagers> ufsUserBranchManagersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UfsUserRegionMap> ufsUserRegionMapCollection;
    @OneToMany(mappedBy = "supervisorId")
    private Collection<FieldTicketsComments> fieldTicketsCommentsCollection;
    @OneToMany(mappedBy = "uploadedBy")
    private Collection<UfsCustomerComplaintsBatch> ufsCustomerComplaintsBatchCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervisorId")
    private Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection;
    @OneToMany(mappedBy = "supervisorId")
    private Collection<FieldTasks> fieldTasksCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UfsUserAgentSupervisor> ufsUserAgentSupervisorCollection;
    @OneToMany(mappedBy = "supervisorId")
    private Collection<FieldTickets> fieldTicketsCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<UfsAuthOtp> ufsAuthOtpCollection;
    @OneToMany(mappedBy = "uploadedBy")
    private Collection<UfsRegionsBatch> ufsRegionsBatchCollection;
    @OneToMany(mappedBy = "uploadedBy")
    private Collection<UfsGlsBatch> ufsGlsBatchCollection;
    @JoinColumn(name = "USER_TYPE", referencedColumnName = "TYPE_ID")
    @ManyToOne(optional = false)
    private UfsUserType userType;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @JoinColumn(name = "GENDER", referencedColumnName = "GENDER_ID")
    @ManyToOne(optional = false)
    private UfsGender gender;
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsDepartment departmentId;
    @OneToMany(mappedBy = "uploadedBy")
    private Collection<TmsWhitelistBatch> tmsWhitelistBatchCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UfsUserRoleMap> ufsUserRoleMapCollection;
    @OneToMany(mappedBy = "supervisorId")
    private Collection<FieldQuestionsFeedback> fieldQuestionsFeedbackCollection;
    @OneToMany(mappedBy = "uploadedBy")
    private Collection<UfsTrainedAgentsBatch> ufsTrainedAgentsBatchCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UfsAuthentication> ufsAuthenticationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UfsUserWorkgroup> ufsUserWorkgroupCollection;

    public UfsUser() {
    }

    public UfsUser(BigDecimal userId) {
        this.userId = userId;
    }

    public UfsUser(BigDecimal userId, String actionStatus, Date creationDate, String fullName, String action, short status) {
        this.userId = userId;
        this.actionStatus = actionStatus;
        this.creationDate = creationDate;
        this.fullName = fullName;
        this.action = action;
        this.status = status;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Collection<UfsPasswordHistory> getUfsPasswordHistoryCollection() {
        return ufsPasswordHistoryCollection;
    }

    public void setUfsPasswordHistoryCollection(Collection<UfsPasswordHistory> ufsPasswordHistoryCollection) {
        this.ufsPasswordHistoryCollection = ufsPasswordHistoryCollection;
    }

    public Collection<UfsUserBranchManagers> getUfsUserBranchManagersCollection() {
        return ufsUserBranchManagersCollection;
    }

    public void setUfsUserBranchManagersCollection(Collection<UfsUserBranchManagers> ufsUserBranchManagersCollection) {
        this.ufsUserBranchManagersCollection = ufsUserBranchManagersCollection;
    }

    public Collection<UfsUserRegionMap> getUfsUserRegionMapCollection() {
        return ufsUserRegionMapCollection;
    }

    public void setUfsUserRegionMapCollection(Collection<UfsUserRegionMap> ufsUserRegionMapCollection) {
        this.ufsUserRegionMapCollection = ufsUserRegionMapCollection;
    }

    public Collection<FieldTicketsComments> getFieldTicketsCommentsCollection() {
        return fieldTicketsCommentsCollection;
    }

    public void setFieldTicketsCommentsCollection(Collection<FieldTicketsComments> fieldTicketsCommentsCollection) {
        this.fieldTicketsCommentsCollection = fieldTicketsCommentsCollection;
    }

    public Collection<UfsCustomerComplaintsBatch> getUfsCustomerComplaintsBatchCollection() {
        return ufsCustomerComplaintsBatchCollection;
    }

    public void setUfsCustomerComplaintsBatchCollection(Collection<UfsCustomerComplaintsBatch> ufsCustomerComplaintsBatchCollection) {
        this.ufsCustomerComplaintsBatchCollection = ufsCustomerComplaintsBatchCollection;
    }

    public Collection<FieldQuestionsSupervisor> getFieldQuestionsSupervisorCollection() {
        return fieldQuestionsSupervisorCollection;
    }

    public void setFieldQuestionsSupervisorCollection(Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection) {
        this.fieldQuestionsSupervisorCollection = fieldQuestionsSupervisorCollection;
    }

    public Collection<FieldTasks> getFieldTasksCollection() {
        return fieldTasksCollection;
    }

    public void setFieldTasksCollection(Collection<FieldTasks> fieldTasksCollection) {
        this.fieldTasksCollection = fieldTasksCollection;
    }

    public Collection<UfsUserAgentSupervisor> getUfsUserAgentSupervisorCollection() {
        return ufsUserAgentSupervisorCollection;
    }

    public void setUfsUserAgentSupervisorCollection(Collection<UfsUserAgentSupervisor> ufsUserAgentSupervisorCollection) {
        this.ufsUserAgentSupervisorCollection = ufsUserAgentSupervisorCollection;
    }

    public Collection<FieldTickets> getFieldTicketsCollection() {
        return fieldTicketsCollection;
    }

    public void setFieldTicketsCollection(Collection<FieldTickets> fieldTicketsCollection) {
        this.fieldTicketsCollection = fieldTicketsCollection;
    }

    public Collection<UfsAuthOtp> getUfsAuthOtpCollection() {
        return ufsAuthOtpCollection;
    }

    public void setUfsAuthOtpCollection(Collection<UfsAuthOtp> ufsAuthOtpCollection) {
        this.ufsAuthOtpCollection = ufsAuthOtpCollection;
    }

    public Collection<UfsRegionsBatch> getUfsRegionsBatchCollection() {
        return ufsRegionsBatchCollection;
    }

    public void setUfsRegionsBatchCollection(Collection<UfsRegionsBatch> ufsRegionsBatchCollection) {
        this.ufsRegionsBatchCollection = ufsRegionsBatchCollection;
    }

    public Collection<UfsGlsBatch> getUfsGlsBatchCollection() {
        return ufsGlsBatchCollection;
    }

    public void setUfsGlsBatchCollection(Collection<UfsGlsBatch> ufsGlsBatchCollection) {
        this.ufsGlsBatchCollection = ufsGlsBatchCollection;
    }

    public UfsUserType getUserType() {
        return userType;
    }

    public void setUserType(UfsUserType userType) {
        this.userType = userType;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public UfsGender getGender() {
        return gender;
    }

    public void setGender(UfsGender gender) {
        this.gender = gender;
    }

    public UfsDepartment getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UfsDepartment departmentId) {
        this.departmentId = departmentId;
    }

    public Collection<TmsWhitelistBatch> getTmsWhitelistBatchCollection() {
        return tmsWhitelistBatchCollection;
    }

    public void setTmsWhitelistBatchCollection(Collection<TmsWhitelistBatch> tmsWhitelistBatchCollection) {
        this.tmsWhitelistBatchCollection = tmsWhitelistBatchCollection;
    }

    public Collection<UfsUserRoleMap> getUfsUserRoleMapCollection() {
        return ufsUserRoleMapCollection;
    }

    public void setUfsUserRoleMapCollection(Collection<UfsUserRoleMap> ufsUserRoleMapCollection) {
        this.ufsUserRoleMapCollection = ufsUserRoleMapCollection;
    }

    public Collection<FieldQuestionsFeedback> getFieldQuestionsFeedbackCollection() {
        return fieldQuestionsFeedbackCollection;
    }

    public void setFieldQuestionsFeedbackCollection(Collection<FieldQuestionsFeedback> fieldQuestionsFeedbackCollection) {
        this.fieldQuestionsFeedbackCollection = fieldQuestionsFeedbackCollection;
    }

    public Collection<UfsTrainedAgentsBatch> getUfsTrainedAgentsBatchCollection() {
        return ufsTrainedAgentsBatchCollection;
    }

    public void setUfsTrainedAgentsBatchCollection(Collection<UfsTrainedAgentsBatch> ufsTrainedAgentsBatchCollection) {
        this.ufsTrainedAgentsBatchCollection = ufsTrainedAgentsBatchCollection;
    }

    public Collection<UfsAuthentication> getUfsAuthenticationCollection() {
        return ufsAuthenticationCollection;
    }

    public void setUfsAuthenticationCollection(Collection<UfsAuthentication> ufsAuthenticationCollection) {
        this.ufsAuthenticationCollection = ufsAuthenticationCollection;
    }

    public Collection<UfsUserWorkgroup> getUfsUserWorkgroupCollection() {
        return ufsUserWorkgroupCollection;
    }

    public void setUfsUserWorkgroupCollection(Collection<UfsUserWorkgroup> ufsUserWorkgroupCollection) {
        this.ufsUserWorkgroupCollection = ufsUserWorkgroupCollection;
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
        return "com.mycompany.oracleufs.UfsUser[ userId=" + userId + " ]";
    }
    
}
