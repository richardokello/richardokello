/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import entities.UfsBankBranches;
import entities.UfsCustomerOutlet;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author ASUS
 */

@Entity
@Table(name = "UFS_GEOGRAPHICAL_REGION")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsGeographicalRegion.findAll", query = "SELECT u FROM UfsGeographicalRegion u"),
        @NamedQuery(name = "UfsGeographicalRegion.findById", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.id = :id"),
        @NamedQuery(name = "UfsGeographicalRegion.findByTenantId", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.tenantId = :tenantId"),
        @NamedQuery(name = "UfsGeographicalRegion.findByRegionName", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.regionName = :regionName"),
        @NamedQuery(name = "UfsGeographicalRegion.findByCode", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.code = :code"),
        @NamedQuery(name = "UfsGeographicalRegion.findByIsParent", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.isParent = :isParent"),
        @NamedQuery(name = "UfsGeographicalRegion.findByCreationDate", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.creationDate = :creationDate"),
        @NamedQuery(name = "UfsGeographicalRegion.findByAction", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.action = :action"),
        @NamedQuery(name = "UfsGeographicalRegion.findByActionStatus", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsGeographicalRegion.findByIntrash", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.intrash = :intrash")})
public class UfsGeographicalRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "UFS_GEOGRAPHICAL_REGION_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_GEOGRAPHICAL_REGION_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_GEOGRAPHICAL_REGION_SEQ")
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 64)
    @Column(name = "TENANT_ID")
    private String tenantId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "REGION_NAME")
    private String regionName;
    @Size(max = 20)
    @Column(name = "CODE")
    private String code;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "geographicalRegionId")
    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "geographicalRegionId")
    private Collection<UfsBankBranches> ufsBankBranchesCollection;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsGeographicalRegion> ufsGeographicalRegionCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsGeographicalRegion parentId;
//    @OneToMany(mappedBy = "geographRegId")
//    private Collection<TmsDevice> tmsDeviceCollection;

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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    @XmlTransient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
        return ufsCustomerOutletCollection;
    }

    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
    }

    @XmlTransient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Collection<UfsBankBranches> getUfsBankBranchesCollection() {
        return ufsBankBranchesCollection;
    }

    public void setUfsBankBranchesCollection(Collection<UfsBankBranches> ufsBankBranchesCollection) {
        this.ufsBankBranchesCollection = ufsBankBranchesCollection;
    }

    @XmlTransient
    @com.fasterxml.jackson.annotation.JsonIgnore
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

//    @XmlTransient
//    @com.fasterxml.jackson.annotation.JsonIgnore
//    public Collection<TmsDevice> getTmsDeviceCollection() {
//        return tmsDeviceCollection;
//    }
//
//    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
//        this.tmsDeviceCollection = tmsDeviceCollection;
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
        return "UfsGeographicalRegion[ id=" + id + " ]";
    }

}













