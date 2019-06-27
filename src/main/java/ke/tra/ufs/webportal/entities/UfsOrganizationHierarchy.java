/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import com.cm.projects.spring.resource.chasis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_ORGANIZATION_HIERARCHY", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsOrganizationHierarchy.findAll", query = "SELECT u FROM UfsOrganizationHierarchy u")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findById", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.id = :id")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findByLevelName", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.levelName = :levelName")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findByLevelNo", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.levelNo = :levelNo")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findByIsRootTenant", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.isRootTenant = :isRootTenant")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findByAction", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.action = :action")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findByActionStatus", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsOrganizationHierarchy.findByIntrash", query = "SELECT u FROM UfsOrganizationHierarchy u WHERE u.intrash = :intrash")})
public class UfsOrganizationHierarchy implements Serializable {

    @Size(max = 20)
    @Column(name = "LEVEL_NAME")
    private String levelName;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 2)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "levelId")
    private Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_ORGANIZATION_HIERARCHY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_ORGANIZATION_HIERARCHY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_ORGANIZATION_HIERARCHY_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "LEVEL_NO")
    private Short levelNo;
    @Column(name = "IS_ROOT_TENANT")
    private Short isRootTenant;
    @OneToMany(mappedBy = "levelId")
    private List<UfsOrganizationUnits> ufsOrganizationUnitsList;

    public UfsOrganizationHierarchy() {
    }

    public UfsOrganizationHierarchy(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Short getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(Short levelNo) {
        this.levelNo = levelNo;
    }

    public Short getIsRootTenant() {
        return isRootTenant;
    }

    public void setIsRootTenant(Short isRootTenant) {
        this.isRootTenant = isRootTenant;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }
    
    


    @XmlTransient
    public List<UfsOrganizationUnits> getUfsOrganizationUnitsList() {
        return ufsOrganizationUnitsList;
    }

    public void setUfsOrganizationUnitsList(List<UfsOrganizationUnits> ufsOrganizationUnitsList) {
        this.ufsOrganizationUnitsList = ufsOrganizationUnitsList;
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
        if (!(object instanceof UfsOrganizationHierarchy)) {
            return false;
        }
        UfsOrganizationHierarchy other = (UfsOrganizationHierarchy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsOrganizationHierarchy[ id=" + id + " ]";
    }


    @XmlTransient
    @JsonIgnore
    public Collection<UfsOrganizationUnits> getUfsOrganizationUnitsCollection() {
        return ufsOrganizationUnitsCollection;
    }

    public void setUfsOrganizationUnitsCollection(Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection) {
        this.ufsOrganizationUnitsCollection = ufsOrganizationUnitsCollection;
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
