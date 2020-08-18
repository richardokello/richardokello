/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "UFS_BANK_BRANCHES")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsBankBranches.findAll", query = "SELECT u FROM UfsBankBranches u"),
        @NamedQuery(name = "UfsBankBranches.findById", query = "SELECT u FROM UfsBankBranches u WHERE u.id = :id"),
        @NamedQuery(name = "UfsBankBranches.findByName", query = "SELECT u FROM UfsBankBranches u WHERE u.name = :name"),
        @NamedQuery(name = "UfsBankBranches.findByCode", query = "SELECT u FROM UfsBankBranches u WHERE u.code = :code"),
        @NamedQuery(name = "UfsBankBranches.findByBankId", query = "SELECT u FROM UfsBankBranches u WHERE u.bankId = :bankId"),
        @NamedQuery(name = "UfsBankBranches.findByAction", query = "SELECT u FROM UfsBankBranches u WHERE u.action = :action"),
        @NamedQuery(name = "UfsBankBranches.findByActionStatus", query = "SELECT u FROM UfsBankBranches u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsBankBranches.findByCreatedAt", query = "SELECT u FROM UfsBankBranches u WHERE u.createdAt = :createdAt"),
        @NamedQuery(name = "UfsBankBranches.findByIntrash", query = "SELECT u FROM UfsBankBranches u WHERE u.intrash = :intrash")})
public class UfsBankBranches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Column(name = "BANK_ID")
    private Long bankId;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "bankBranchId")
    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankRegion bankRegionId;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsGeographicalRegion geographicalRegionId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "bankBranchId")
    private Collection<TmsDevice> tmsDeviceCollection;

    public UfsBankBranches() {
    }

    public UfsBankBranches(Long id) {
        this.id = id;
    }

    public UfsBankBranches(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
        return ufsCustomerOutletCollection;
    }

    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
    }

    public UfsBankRegion getBankRegionId() {
        return bankRegionId;
    }

    public void setBankRegionId(UfsBankRegion bankRegionId) {
        this.bankRegionId = bankRegionId;
    }

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
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
        if (!(object instanceof UfsBankBranches)) {
            return false;
        }
        UfsBankBranches other = (UfsBankBranches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UfsBankBranches[ id=" + id + " ]";
    }

}
