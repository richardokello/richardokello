/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_BANK_REGION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsBankRegion.findAll", query = "SELECT u FROM UfsBankRegion u")
    , @NamedQuery(name = "UfsBankRegion.findById", query = "SELECT u FROM UfsBankRegion u WHERE u.id = :id")
    , @NamedQuery(name = "UfsBankRegion.findByBranchName", query = "SELECT u FROM UfsBankRegion u WHERE u.branchName = :branchName")
    , @NamedQuery(name = "UfsBankRegion.findByCode", query = "SELECT u FROM UfsBankRegion u WHERE u.code = :code")
    , @NamedQuery(name = "UfsBankRegion.findByIsParent", query = "SELECT u FROM UfsBankRegion u WHERE u.isParent = :isParent")
    , @NamedQuery(name = "UfsBankRegion.findByCreationDate", query = "SELECT u FROM UfsBankRegion u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsBankRegion.findByAction", query = "SELECT u FROM UfsBankRegion u WHERE u.action = :action")
    , @NamedQuery(name = "UfsBankRegion.findByActionStatus", query = "SELECT u FROM UfsBankRegion u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsBankRegion.findByIntrash", query = "SELECT u FROM UfsBankRegion u WHERE u.intrash = :intrash")})
public class UfsBankRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 100)
    @Column(name = "BRANCH_NAME")
    private String branchName;
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
    @OneToMany(mappedBy = "parentId")
    private List<UfsBankRegion> ufsBankRegionList;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion parentId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;

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

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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
    public List<UfsBankRegion> getUfsBankRegionList() {
        return ufsBankRegionList;
    }

    public void setUfsBankRegionList(List<UfsBankRegion> ufsBankRegionList) {
        this.ufsBankRegionList = ufsBankRegionList;
    }

    public UfsBankRegion getParentId() {
        return parentId;
    }

    public void setParentId(UfsBankRegion parentId) {
        this.parentId = parentId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
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
        return "ke.tracom.ufs.entities.UfsBankRegion[ id=" + id + " ]";
    }
    
}
