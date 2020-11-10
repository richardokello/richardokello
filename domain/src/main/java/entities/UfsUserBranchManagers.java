/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_USER_BRANCH_MANAGERS")
@NamedQueries({
    @NamedQuery(name = "UfsUserBranchManagers.findAll", query = "SELECT u FROM UfsUserBranchManagers u")})
public class UfsUserBranchManagers implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private UfsUser userId;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankRegion regionId;
    @JoinColumn(name = "BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankBranches branchId;

    public UfsUserBranchManagers() {
    }

    public UfsUserBranchManagers(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
    }

    public UfsBankRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(UfsBankRegion regionId) {
        this.regionId = regionId;
    }

    public UfsBankBranches getBranchId() {
        return branchId;
    }

    public void setBranchId(UfsBankBranches branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof UfsUserBranchManagers)) {
            return false;
        }
        UfsUserBranchManagers other = (UfsUserBranchManagers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsUserBranchManagers[ id=" + id + " ]";
    }
    
}
