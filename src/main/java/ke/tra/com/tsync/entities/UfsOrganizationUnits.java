/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
        @NamedQuery(name = "UfsOrganizationUnits.findAll", query = "SELECT u FROM UfsOrganizationUnits u"),
        @NamedQuery(name = "UfsOrganizationUnits.findByName", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.name = :name"),
        @NamedQuery(name = "UfsOrganizationUnits.findByIsParent", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.isParent = :isParent"),
        @NamedQuery(name = "UfsOrganizationUnits.findByAction", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.action = :action"),
        @NamedQuery(name = "UfsOrganizationUnits.findByActionStatus", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsOrganizationUnits.findByIntrash", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.intrash = :intrash"),
        @NamedQuery(name = "UfsOrganizationUnits.findByUUid", query = "SELECT u FROM UfsOrganizationUnits u WHERE u.uUid = :uUid")})
public class UfsOrganizationUnits implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "U_UID")
    private String uUid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tenantId")
    private Collection<UfsBankBranches> ufsBankBranchesCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsCurrency> ufsCurrencyCollection;
    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsCustomerType> ufsCustomerTypeCollection;
    @JoinColumn(name = "LEVEL_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsOrganizationHierarchy levelId;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection;

    @JoinColumn(name = "PARENT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits parentId;

    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsBanks> ufsBanksCollection;

    @OneToMany(mappedBy = "tenantId")
    private Collection<UfsCustomer> ufsCustomerCollection;

    public UfsOrganizationUnits() {
    }

    public UfsOrganizationUnits(String uUid) {
        this.uUid = uUid;
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

    public String getUUid() {
        return uUid;
    }

    public void setUUid(String uUid) {
        this.uUid = uUid;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsBankBranches> getUfsBankBranchesCollection() {
        return ufsBankBranchesCollection;
    }

    public void setUfsBankBranchesCollection(Collection<UfsBankBranches> ufsBankBranchesCollection) {
        this.ufsBankBranchesCollection = ufsBankBranchesCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCurrency> getUfsCurrencyCollection() {
        return ufsCurrencyCollection;
    }

    public void setUfsCurrencyCollection(Collection<UfsCurrency> ufsCurrencyCollection) {
        this.ufsCurrencyCollection = ufsCurrencyCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomerType> getUfsCustomerTypeCollection() {
        return ufsCustomerTypeCollection;
    }

    public void setUfsCustomerTypeCollection(Collection<UfsCustomerType> ufsCustomerTypeCollection) {
        this.ufsCustomerTypeCollection = ufsCustomerTypeCollection;
    }

    public UfsOrganizationHierarchy getLevelId() {
        return levelId;
    }

    public void setLevelId(UfsOrganizationHierarchy levelId) {
        this.levelId = levelId;
    }

    @XmlTransient
    @JsonIgnore
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

    @XmlTransient
    @JsonIgnore
    public Collection<UfsBanks> getUfsBanksCollection() {
        return ufsBanksCollection;
    }

    public void setUfsBanksCollection(Collection<UfsBanks> ufsBanksCollection) {
        this.ufsBanksCollection = ufsBanksCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomer> getUfsCustomerCollection() {
        return ufsCustomerCollection;
    }

    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
        this.ufsCustomerCollection = ufsCustomerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uUid != null ? uUid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsOrganizationUnits)) {
            return false;
        }
        UfsOrganizationUnits other = (UfsOrganizationUnits) object;
        if ((this.uUid == null && other.uUid != null) || (this.uUid != null && !this.uUid.equals(other.uUid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UfsOrganizationUnits[ uUid=" + uUid + " ]";
    }

}
