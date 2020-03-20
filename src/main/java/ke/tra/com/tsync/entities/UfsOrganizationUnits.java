/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ORGANIZATION_UNITS")
@NamedQueries({
    @NamedQuery(name = "UfsOrganizationUnits.findAll", query = "SELECT u FROM UfsOrganizationUnits u")})
public class UfsOrganizationUnits implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsCustomerTypeRules> ufsCustomerTypeRulesCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsReconBatch> ufsReconBatchCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsGeographicalRegion> ufsGeographicalRegionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsCurrency> ufsCurrencyCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsWorkgroup> ufsWorkgroupCollection;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsOrganizationUnits parentId;
    @JoinColumn(name = "LEVEL_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsOrganizationHierarchy levelId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsBankRegion> ufsBankRegionCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsDepartment> ufsDepartmentCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsCustomerType> ufsCustomerTypeCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsCustomer> ufsCustomerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsBanks> ufsBanksCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsUser> ufsUserCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsBusinessUnits> ufsBusinessUnitsCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsRole> ufsRoleCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsTieredCommissionAmount> ufsTieredCommissionAmountCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsGls> ufsGlsCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsRevenueEntities> ufsRevenueEntitiesCollection;

    public UfsOrganizationUnits() {
    }

    public UfsOrganizationUnits(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getIsParent() {
        return isParent;
    }

    public void setIsParent(Short isParent) {
        this.isParent = isParent;
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

    public Collection<UfsCustomerTypeRules> getUfsCustomerTypeRulesCollection() {
        return ufsCustomerTypeRulesCollection;
    }

    public void setUfsCustomerTypeRulesCollection(Collection<UfsCustomerTypeRules> ufsCustomerTypeRulesCollection) {
        this.ufsCustomerTypeRulesCollection = ufsCustomerTypeRulesCollection;
    }

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    public Collection<UfsReconBatch> getUfsReconBatchCollection() {
        return ufsReconBatchCollection;
    }

    public void setUfsReconBatchCollection(Collection<UfsReconBatch> ufsReconBatchCollection) {
        this.ufsReconBatchCollection = ufsReconBatchCollection;
    }

    public Collection<UfsGeographicalRegion> getUfsGeographicalRegionCollection() {
        return ufsGeographicalRegionCollection;
    }

    public void setUfsGeographicalRegionCollection(Collection<UfsGeographicalRegion> ufsGeographicalRegionCollection) {
        this.ufsGeographicalRegionCollection = ufsGeographicalRegionCollection;
    }

    public Collection<UfsCurrency> getUfsCurrencyCollection() {
        return ufsCurrencyCollection;
    }

    public void setUfsCurrencyCollection(Collection<UfsCurrency> ufsCurrencyCollection) {
        this.ufsCurrencyCollection = ufsCurrencyCollection;
    }

    public Collection<UfsWorkgroup> getUfsWorkgroupCollection() {
        return ufsWorkgroupCollection;
    }

    public void setUfsWorkgroupCollection(Collection<UfsWorkgroup> ufsWorkgroupCollection) {
        this.ufsWorkgroupCollection = ufsWorkgroupCollection;
    }

    public Collection<UfsOrganizationUnits> getUfsOrganizationUnitsCollection() {
        return ufsOrganizationUnitsCollection;
    }

    public void setUfsOrganizationUnitsCollection(Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection) {
        this.ufsOrganizationUnitsCollection = ufsOrganizationUnitsCollection;
    }

    public UfsOrganizationUnits getParentId() {
        return parentId;
    }

    public void setParentId(UfsOrganizationUnits parentId) {
        this.parentId = parentId;
    }

    public UfsOrganizationHierarchy getLevelId() {
        return levelId;
    }

    public void setLevelId(UfsOrganizationHierarchy levelId) {
        this.levelId = levelId;
    }

    public Collection<UfsBankRegion> getUfsBankRegionCollection() {
        return ufsBankRegionCollection;
    }

    public void setUfsBankRegionCollection(Collection<UfsBankRegion> ufsBankRegionCollection) {
        this.ufsBankRegionCollection = ufsBankRegionCollection;
    }

    public Collection<UfsDepartment> getUfsDepartmentCollection() {
        return ufsDepartmentCollection;
    }

    public void setUfsDepartmentCollection(Collection<UfsDepartment> ufsDepartmentCollection) {
        this.ufsDepartmentCollection = ufsDepartmentCollection;
    }

    public Collection<UfsCustomerType> getUfsCustomerTypeCollection() {
        return ufsCustomerTypeCollection;
    }

    public void setUfsCustomerTypeCollection(Collection<UfsCustomerType> ufsCustomerTypeCollection) {
        this.ufsCustomerTypeCollection = ufsCustomerTypeCollection;
    }

    public Collection<UfsCustomer> getUfsCustomerCollection() {
        return ufsCustomerCollection;
    }

    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
        this.ufsCustomerCollection = ufsCustomerCollection;
    }

    public Collection<UfsBanks> getUfsBanksCollection() {
        return ufsBanksCollection;
    }

    public void setUfsBanksCollection(Collection<UfsBanks> ufsBanksCollection) {
        this.ufsBanksCollection = ufsBanksCollection;
    }

    public Collection<UfsUser> getUfsUserCollection() {
        return ufsUserCollection;
    }

    public void setUfsUserCollection(Collection<UfsUser> ufsUserCollection) {
        this.ufsUserCollection = ufsUserCollection;
    }

    public Collection<UfsBusinessUnits> getUfsBusinessUnitsCollection() {
        return ufsBusinessUnitsCollection;
    }

    public void setUfsBusinessUnitsCollection(Collection<UfsBusinessUnits> ufsBusinessUnitsCollection) {
        this.ufsBusinessUnitsCollection = ufsBusinessUnitsCollection;
    }

    public Collection<UfsRole> getUfsRoleCollection() {
        return ufsRoleCollection;
    }

    public void setUfsRoleCollection(Collection<UfsRole> ufsRoleCollection) {
        this.ufsRoleCollection = ufsRoleCollection;
    }

    public Collection<UfsTieredCommissionAmount> getUfsTieredCommissionAmountCollection() {
        return ufsTieredCommissionAmountCollection;
    }

    public void setUfsTieredCommissionAmountCollection(Collection<UfsTieredCommissionAmount> ufsTieredCommissionAmountCollection) {
        this.ufsTieredCommissionAmountCollection = ufsTieredCommissionAmountCollection;
    }

    public Collection<UfsGls> getUfsGlsCollection() {
        return ufsGlsCollection;
    }

    public void setUfsGlsCollection(Collection<UfsGls> ufsGlsCollection) {
        this.ufsGlsCollection = ufsGlsCollection;
    }

    public Collection<UfsRevenueEntities> getUfsRevenueEntitiesCollection() {
        return ufsRevenueEntitiesCollection;
    }

    public void setUfsRevenueEntitiesCollection(Collection<UfsRevenueEntities> ufsRevenueEntitiesCollection) {
        this.ufsRevenueEntitiesCollection = ufsRevenueEntitiesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsOrganizationUnits)) {
            return false;
        }
        UfsOrganizationUnits other = (UfsOrganizationUnits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsOrganizationUnits[ id=" + id + " ]";
    }
    
}
