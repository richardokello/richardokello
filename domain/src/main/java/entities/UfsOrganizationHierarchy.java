/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ORGANIZATION_HIERARCHY")
@NamedQueries({
    @NamedQuery(name = "UfsOrganizationHierarchy.findAll", query = "SELECT u FROM UfsOrganizationHierarchy u")})
public class UfsOrganizationHierarchy implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "LEVEL_NAME")
    private String levelName;
    @Column(name = "LEVEL_NO")
    private BigInteger levelNo;
    @Column(name = "IS_ROOT_TENANT")
    private Short isRootTenant;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "levelId")
    private Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection;

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

    public BigInteger getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(BigInteger levelNo) {
        this.levelNo = levelNo;
    }

    public Short getIsRootTenant() {
        return isRootTenant;
    }

    public void setIsRootTenant(Short isRootTenant) {
        this.isRootTenant = isRootTenant;
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

    public Collection<UfsOrganizationUnits> getUfsOrganizationUnitsCollection() {
        return ufsOrganizationUnitsCollection;
    }

    public void setUfsOrganizationUnitsCollection(Collection<UfsOrganizationUnits> ufsOrganizationUnitsCollection) {
        this.ufsOrganizationUnitsCollection = ufsOrganizationUnitsCollection;
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
        return "com.mycompany.oracleufs.UfsOrganizationHierarchy[ id=" + id + " ]";
    }
    
}
