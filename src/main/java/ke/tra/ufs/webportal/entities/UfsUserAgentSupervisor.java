/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_USER_AGENT_SUPERVISOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsUserAgentSupervisor.findAll", query = "SELECT u FROM UfsUserAgentSupervisor u")
    , @NamedQuery(name = "UfsUserAgentSupervisor.findById", query = "SELECT u FROM UfsUserAgentSupervisor u WHERE u.id = :id")})
public class UfsUserAgentSupervisor implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "BRANCH_ID", referencedColumnName = "ID",updatable = false,insertable = false)
    @ManyToOne(optional = false)
    private UfsBankBranches branchId;
    @Column(name = "BRANCH_ID")
    private Long branchIds;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID",updatable = false,insertable = false)
    @ManyToOne(optional = false)
    private UfsBankRegion regionId;
    @Column(name = "REGION_ID")
    private BigDecimal regionIds;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID",insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private UfsUser userId;
    @Column(name = "USER_ID")
    private Long userIds;

    public UfsUserAgentSupervisor() {
    }

    public UfsUserAgentSupervisor(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Long getBranchIds() {
        return branchIds;
    }

    public BigDecimal getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(BigDecimal regionIds) {
        this.regionIds = regionIds;
    }

    public Long getUserIds() {
        return userIds;
    }

    public void setUserIds(Long userIds) {
        this.userIds = userIds;
    }

    public void setBranchIds(Long branchIds) {
        this.branchIds = branchIds;
    }

    public UfsBankBranches getBranchId() {
        return branchId;
    }

    public void setBranchId(UfsBankBranches branchId) {
        this.branchId = branchId;
    }

    public UfsBankRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(UfsBankRegion regionId) {
        this.regionId = regionId;
    }

    public UfsUser getUserId() {
        return userId;
    }

    public void setUserId(UfsUser userId) {
        this.userId = userId;
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
        if (!(object instanceof UfsUserAgentSupervisor)) {
            return false;
        }
        UfsUserAgentSupervisor other = (UfsUserAgentSupervisor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsUserAgentSupervisor[ id=" + id + " ]";
    }
    
}
