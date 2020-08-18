/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_BANK_REGION")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsBankRegion.findAll", query = "SELECT u FROM UfsBankRegion u"),
        @NamedQuery(name = "UfsBankRegion.findById", query = "SELECT u FROM UfsBankRegion u WHERE u.id = :id"),
        @NamedQuery(name = "UfsBankRegion.findByTenantId", query = "SELECT u FROM UfsBankRegion u WHERE u.tenantId = :tenantId"),
        @NamedQuery(name = "UfsBankRegion.findByRegionName", query = "SELECT u FROM UfsBankRegion u WHERE u.regionName = :regionName"),
        @NamedQuery(name = "UfsBankRegion.findByCode", query = "SELECT u FROM UfsBankRegion u WHERE u.code = :code"),
        @NamedQuery(name = "UfsBankRegion.findByIsParent", query = "SELECT u FROM UfsBankRegion u WHERE u.isParent = :isParent"),
        @NamedQuery(name = "UfsBankRegion.findByCreationDate", query = "SELECT u FROM UfsBankRegion u WHERE u.creationDate = :creationDate"),
        @NamedQuery(name = "UfsBankRegion.findByAction", query = "SELECT u FROM UfsBankRegion u WHERE u.action = :action"),
        @NamedQuery(name = "UfsBankRegion.findByActionStatus", query = "SELECT u FROM UfsBankRegion u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsBankRegion.findByIntrash", query = "SELECT u FROM UfsBankRegion u WHERE u.intrash = :intrash")})
public class UfsBankRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 64)
    @Column(name = "TENANT_ID")
    private String tenantId;
    @Size(max = 100)
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankRegionId")
    private Collection<UfsBankBranches> ufsBankBranchesCollection;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBanks bankId;
    @OneToMany(mappedBy = "parentId")
    private Collection<UfsBankRegion> ufsBankRegionCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion parentId;

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
    @JsonIgnore
    public Collection<UfsBankBranches> getUfsBankBranchesCollection() {
        return ufsBankBranchesCollection;
    }

    public void setUfsBankBranchesCollection(Collection<UfsBankBranches> ufsBankBranchesCollection) {
        this.ufsBankBranchesCollection = ufsBankBranchesCollection;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
    }

    @XmlTransient
    @JsonIgnore
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
        return "UfsBankRegion[ id=" + id + " ]";
    }

}
