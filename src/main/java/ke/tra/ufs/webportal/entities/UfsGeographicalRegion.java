/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.TreeRoot;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_GEOGRAPHICAL_REGION")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsGeographicalRegion.findAll", query = "SELECT u FROM UfsGeographicalRegion u")
        , @NamedQuery(name = "UfsGeographicalRegion.findById", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.id = :id")
        , @NamedQuery(name = "UfsGeographicalRegion.findByGeographicalId", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.id = :id")
        , @NamedQuery(name = "UfsGeographicalRegion.findByRegionName", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.regionName = :regionName")
        , @NamedQuery(name = "UfsGeographicalRegion.findByCode", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.code = :code")
        , @NamedQuery(name = "UfsGeographicalRegion.findByIsParent", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.isParent = :isParent")
        , @NamedQuery(name = "UfsGeographicalRegion.findByCreationDate", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsGeographicalRegion.findByAction", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.action = :action")
        , @NamedQuery(name = "UfsGeographicalRegion.findByActionStatus", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsGeographicalRegion.findByIntrash", query = "SELECT u FROM UfsGeographicalRegion u WHERE u.intrash = :intrash")})
public class UfsGeographicalRegion implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "REGION_NAME")
    private String regionName;
    @Size(max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "geographicalRegId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Collection<UfsCustomer> ufsCustomerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "geographicalRegionId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<UfsBankBranches> ufsBankBranchesSet;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
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
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @Column(name = "CREATION_DATE",insertable = false , updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(mappedBy = "parentId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsGeographicalRegion> ufsGeographicalRegionList;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsGeographicalRegion parentId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    @Column(name = "PARENT_ID")
    @TreeRoot
    private BigDecimal parentIds;
    @Column(name = "TENANT_ID")
    private String tenantIds;
    @Transient
    private MultipartFile file;


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


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }



    public BigDecimal getParentIds() {
        return parentIds;
    }

    public void setParentIds(BigDecimal parentIds) {
        this.parentIds = parentIds;
    }

    public String getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(String tenantIds) {
        this.tenantIds = tenantIds;
    }

    @XmlTransient
    @JsonIgnore
    public List<UfsGeographicalRegion> getUfsGeographicalRegionList() {
        return ufsGeographicalRegionList;
    }

    public void setUfsGeographicalRegionList(List<UfsGeographicalRegion> ufsGeographicalRegionList) {
        this.ufsGeographicalRegionList = ufsGeographicalRegionList;
    }

    public UfsGeographicalRegion getParentId() {
        return parentId;
    }

    public void setParentId(UfsGeographicalRegion parentId) {
        this.parentId = parentId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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
        return "ke.tracom.ufs.entities.UfsGeographicalRegion[ id=" + id + " ]";
    }


    @XmlTransient
    @JsonIgnore
    public Set<UfsBankBranches> getUfsBankBranchesSet() {
        return ufsBankBranchesSet;
    }

    public void setUfsBankBranchesSet(Set<UfsBankBranches> ufsBankBranchesSet) {
        this.ufsBankBranchesSet = ufsBankBranchesSet;
    }

 


    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomer> getUfsCustomerCollection() {
        return ufsCustomerCollection;
    }

    public void setUfsCustomerCollection(Collection<UfsCustomer> ufsCustomerCollection) {
        this.ufsCustomerCollection = ufsCustomerCollection;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
