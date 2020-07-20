/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.ChasisUUID;
import ke.axle.chassis.annotations.TreeRoot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ORGANIZATION_UNITS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsOrganizationUnits.findAll", query = "SELECT u FROM UfsOrganizationUnits u")
        , @NamedQuery(name = "UfsOrganizationUnits.findByName", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.name = :name")
        , @NamedQuery(name = "UfsOrganizationUnits.findByIsParent", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.isParent = :isParent")
        , @NamedQuery(name = "UfsOrganizationUnits.findByAction", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.action = :action")
        , @NamedQuery(name = "UfsOrganizationUnits.findByActionStatus", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsOrganizationUnits.findByIntrash", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.intrash = :intrash")})
public class UfsOrganizationUnits implements Serializable {

    @Size(max = 20)
    @Column(name = "NAME")
    private String name;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    @JsonIgnore
    private Set<UfsBanks> ufsBanksSet;
    @OneToMany(mappedBy = "tenantId")
    @JsonIgnore
    private List<UfsTieredCommissionAmount> ufsTieredCommissionAmountList;
    @OneToMany(mappedBy = "tenantId")
    @JsonIgnore
    private List<UfsRevenueEntities> ufsRevenueEntitiesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    @JsonIgnore
    private List<UfsCustomerTypeRules> ufsCustomerTypeRulesList;
    @OneToMany(mappedBy = "tenantId")
    @JsonIgnore
    private Collection<UfsCustomerType> ufsCustomerTypeCollection;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private List<UfsGeographicalRegion> ufsGeographicalRegionList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private List<UfsBusinessUnits> ufsBusinessUnitsList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private List<UfsBankRegion> ufsBankRegionList;
    @JsonIgnore
    @OneToMany(mappedBy = "tenantId")
    private List<UfsCurrency> ufsCurrencyList;
    @JsonIgnore
    @OneToMany(mappedBy = "tenantId")
    private List<UfsDepartment> ufsDepartmentList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @ChasisUUID
    @Column(name = "U_UID")
    private String uuid;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @OneToMany(mappedBy = "tenantId")
    @JsonIgnore
    private List<UfsRole> ufsRoleList;
    @OneToMany(mappedBy = "tenantId")
    @JsonIgnore
    private List<UfsWorkgroup> ufsWorkgroupList;
    @JoinColumn(name = "LEVEL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationHierarchy levelId;
    @Column(name = "LEVEL_ID")
    private BigDecimal levelIds;

    @OneToMany(mappedBy = "parentId")
    @JsonIgnore
    private List<UfsOrganizationUnits> ufsOrganizationUnitsList;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationUnits parentId;
    @OneToMany(mappedBy = "tenantId")
    @JsonIgnore
    private List<UfsUser> ufsUserList;
    @Transient
    private List<UfsOrganizationUnits> children;
    @Column(name = "PARENT_ID")
    @TreeRoot
    private String parentIds;
    @Transient
    private String text;
    @Transient
    private UfsOrganizationHierarchy currentLevelTenant;
    @Transient
    private UfsOrganizationHierarchy nextLevelTenant;

    public UfsOrganizationUnits() {
    }

    public UfsOrganizationUnits(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Short getIsParent() {
        return isParent;
    }

    public void setIsParent(Short isParent) {
        this.isParent = isParent;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @XmlTransient
    public List<UfsRole> getUfsRoleList() {
        return ufsRoleList;
    }

    public void setUfsRoleList(List<UfsRole> ufsRoleList) {
        this.ufsRoleList = ufsRoleList;
    }

    @XmlTransient
    public List<UfsWorkgroup> getUfsWorkgroupList() {
        return ufsWorkgroupList;
    }

    public void setUfsWorkgroupList(List<UfsWorkgroup> ufsWorkgroupList) {
        this.ufsWorkgroupList = ufsWorkgroupList;
    }

    public UfsOrganizationHierarchy getLevelId() {
        return levelId;
    }

    public void setLevelId(UfsOrganizationHierarchy levelId) {
        this.levelId = levelId;
    }

    @XmlTransient
    @JsonIgnore
    public List<UfsOrganizationUnits> getUfsOrganizationUnitsList() {
        return ufsOrganizationUnitsList;
    }

    public void setUfsOrganizationUnitsList(List<UfsOrganizationUnits> ufsOrganizationUnitsList) {
        this.ufsOrganizationUnitsList = ufsOrganizationUnitsList;
    }

    public UfsOrganizationUnits getParentId() {
        return parentId;
    }

    public void setParentId(UfsOrganizationUnits parentId) {
        this.parentId = parentId;
    }

    @XmlTransient
    public List<UfsUser> getUfsUserList() {
        return ufsUserList;
    }

    public void setUfsUserList(List<UfsUser> ufsUserList) {
        this.ufsUserList = ufsUserList;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public BigDecimal getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(BigDecimal levelIds) {
        this.levelIds = levelIds;
    }

    public String getText() {
        try {
            String _text = this.getName();
            _text += "       :     " + this.getLevelId().getLevelName();
            return _text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsOrganizationUnits)) {
            return false;
        }
        UfsOrganizationUnits other = (UfsOrganizationUnits) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    public UfsOrganizationHierarchy getCurrentLevelTenant() {
        return getLevelId();
    }

    public void setCurrentLevelTenant(UfsOrganizationHierarchy currentLevelTenant) {
        this.currentLevelTenant = currentLevelTenant;
    }

    public UfsOrganizationHierarchy getNextLevelTenant() {
        return nextLevelTenant;
    }

    public void setNextLevelTenant(UfsOrganizationHierarchy nextLevelTenant) {
        this.nextLevelTenant = nextLevelTenant;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsOrganizationUnits[ id=" + uuid + " ]";
    }


    @XmlTransient
    public List<UfsGeographicalRegion> getUfsGeographicalRegionList() {
        return ufsGeographicalRegionList;
    }

    public void setUfsGeographicalRegionList(List<UfsGeographicalRegion> ufsGeographicalRegionList) {
        this.ufsGeographicalRegionList = ufsGeographicalRegionList;
    }

    @XmlTransient
    public List<UfsBusinessUnits> getUfsBusinessUnitsList() {
        return ufsBusinessUnitsList;
    }

    public void setUfsBusinessUnitsList(List<UfsBusinessUnits> ufsBusinessUnitsList) {
        this.ufsBusinessUnitsList = ufsBusinessUnitsList;
    }

    @XmlTransient
    public List<UfsBankRegion> getUfsBankRegionList() {
        return ufsBankRegionList;
    }

    public void setUfsBankRegionList(List<UfsBankRegion> ufsBankRegionList) {
        this.ufsBankRegionList = ufsBankRegionList;
    }

    @XmlTransient
    public List<UfsCurrency> getUfsCurrencyList() {
        return ufsCurrencyList;
    }

    public void setUfsCurrencyList(List<UfsCurrency> ufsCurrencyList) {
        this.ufsCurrencyList = ufsCurrencyList;
    }

    @XmlTransient
    public List<UfsDepartment> getUfsDepartmentList() {
        return ufsDepartmentList;
    }

    public void setUfsDepartmentList(List<UfsDepartment> ufsDepartmentList) {
        this.ufsDepartmentList = ufsDepartmentList;
    }

    public List<UfsOrganizationUnits> getChildren() {
        return ufsOrganizationUnitsList;
    }

    public void setChildren(List<UfsOrganizationUnits> children) {
        this.children = children;
    }


    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomerType> getUfsCustomerTypeCollection() {
        return ufsCustomerTypeCollection;
    }

    public void setUfsCustomerTypeCollection(Collection<UfsCustomerType> ufsCustomerTypeCollection) {
        this.ufsCustomerTypeCollection = ufsCustomerTypeCollection;
    }


    @XmlTransient
    @JsonIgnore
    public List<UfsTieredCommissionAmount> getUfsTieredCommissionAmountList() {
        return ufsTieredCommissionAmountList;
    }

    public void setUfsTieredCommissionAmountList(List<UfsTieredCommissionAmount> ufsTieredCommissionAmountList) {
        this.ufsTieredCommissionAmountList = ufsTieredCommissionAmountList;
    }

    @XmlTransient
    @JsonIgnore
    public List<UfsRevenueEntities> getUfsRevenueEntitiesList() {
        return ufsRevenueEntitiesList;
    }

    public void setUfsRevenueEntitiesList(List<UfsRevenueEntities> ufsRevenueEntitiesList) {
        this.ufsRevenueEntitiesList = ufsRevenueEntitiesList;
    }

    @XmlTransient
    @JsonIgnore
    public List<UfsCustomerTypeRules> getUfsCustomerTypeRulesList() {
        return ufsCustomerTypeRulesList;
    }

    public void setUfsCustomerTypeRulesList(List<UfsCustomerTypeRules> ufsCustomerTypeRulesList) {
        this.ufsCustomerTypeRulesList = ufsCustomerTypeRulesList;
    }


    @XmlTransient
    @JsonIgnore
    public Set<UfsBanks> getUfsBanksSet() {
        return ufsBanksSet;
    }

    public void setUfsBanksSet(Set<UfsBanks> ufsBanksSet) {
        this.ufsBanksSet = ufsBanksSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
