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
@Table(name = "UFS_BANK_REGION")
@NamedQueries({
    @NamedQuery(name = "UfsBankRegion.findAll", query = "SELECT u FROM UfsBankRegion u")})
public class UfsBankRegion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "REGION_NAME")
    private String regionName;
    @Column(name = "CODE")
    private String code;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regionId")
    private Collection<UfsUserBranchManagers> ufsUserBranchManagersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regionId")
    private Collection<UfsUserRegionMap> ufsUserRegionMapCollection;
    @OneToMany(mappedBy = "bankRegionId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankRegionId")
    private Collection<UfsBankBranches> ufsBankBranchesCollection;
    @OneToMany(mappedBy = "targetRegions")
    private Collection<FieldQuestionsSupervisor> fieldQuestionsSupervisorCollection;
    @OneToMany(mappedBy = "regionId")
    private Collection<FieldTasks> fieldTasksCollection;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsBankRegion> ufsBankRegionCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion parentId;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBanks bankId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regionId")
    private Collection<UfsUserAgentSupervisor> ufsUserAgentSupervisorCollection;
    @OneToMany(mappedBy = "bankRegionId")
    private Collection<FieldTickets> fieldTicketsCollection;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankRegionId")
//    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;

    public UfsBankRegion() {
    }

    public UfsBankRegion(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getIsParent() {
        return isParent;
    }

    public void setIsParent(Short isParent) {
        this.isParent = isParent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    public Collection<UfsBankBranches> getUfsBankBranchesCollection() {
        return ufsBankBranchesCollection;
    }

    public void setUfsBankBranchesCollection(Collection<UfsBankBranches> ufsBankBranchesCollection) {
        this.ufsBankBranchesCollection = ufsBankBranchesCollection;
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

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public Collection<UfsBankRegion> getUfsBankRegionCollection() {
        return ufsBankRegionCollection;
    }

    public void setUfsBankRegionCollection(Collection<UfsBankRegion> ufsBankRegionCollection) {
        this.ufsBankRegionCollection = ufsBankRegionCollection;
    }

    public UfsBankRegion getParentId() {
        return parentId;
    }

    public void setParentId(UfsBankRegion parentId) {
        this.parentId = parentId;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
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

//    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
//        return ufsCustomerOutletCollection;
//    }
//
//    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
//        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsBankRegion)) {
            return false;
        }
        UfsBankRegion other = (UfsBankRegion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsBankRegion[ id=" + id + " ]";
    }
    
}
