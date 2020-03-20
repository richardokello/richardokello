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
@Table(name = "UFS_GEOGRAPHICAL_REGION")
@NamedQueries({
    @NamedQuery(name = "UfsGeographicalRegion.findAll", query = "SELECT u FROM UfsGeographicalRegion u")})
public class UfsGeographicalRegion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
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
    @OneToMany(mappedBy = "geographRegId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "geographicalRegionId")
    private Collection<UfsBankBranches> ufsBankBranchesCollection;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsGeographicalRegion> ufsGeographicalRegionCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsGeographicalRegion parentId;
    @OneToMany(mappedBy = "geographicalRegId")
    private Collection<UfsCustomer> ufsCustomerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "geographicalRegionId")
    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;

    public UfsGeographicalRegion() {
    }

    public UfsGeographicalRegion(BigDecimal id) {
        this.id = id;
    }

    public UfsGeographicalRegion(BigDecimal id, String regionName) {
        this.id = id;
        this.regionName = regionName;
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

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public Collection<UfsGeographicalRegion> getUfsGeographicalRegionCollection() {
        return ufsGeographicalRegionCollection;
    }

    public void setUfsGeographicalRegionCollection(Collection<UfsGeographicalRegion> ufsGeographicalRegionCollection) {
        this.ufsGeographicalRegionCollection = ufsGeographicalRegionCollection;
    }

    public UfsGeographicalRegion getParentId() {
        return parentId;
    }

    public void setParentId(UfsGeographicalRegion parentId) {
        this.parentId = parentId;
    }

    public Collection<UfsCustomer> getUfsCustomerCollection() {
        return ufsCustomerCollection;
    }

    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
        this.ufsCustomerCollection = ufsCustomerCollection;
    }

    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
        return ufsCustomerOutletCollection;
    }

    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
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
        if (!(object instanceof UfsGeographicalRegion)) {
            return false;
        }
        UfsGeographicalRegion other = (UfsGeographicalRegion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsGeographicalRegion[ id=" + id + " ]";
    }
    
}
