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
@Table(name = "UFS_USER_ROLE")
@NamedQueries({
    @NamedQuery(name = "UfsUserRole.findAll", query = "SELECT u FROM UfsUserRole u")})
public class UfsUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ROLE_ID")
    private BigDecimal roleId;
    @Basic(optional = false)
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<UfsUserRoleMap> ufsUserRoleMapCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleId")
    private Collection<UfsRolePermissionMap> ufsRolePermissionMapCollection;

    public UfsUserRole() {
    }

    public UfsUserRole(BigDecimal roleId) {
        this.roleId = roleId;
    }

    public UfsUserRole(BigDecimal roleId, String roleName, String description, Date creationDate, String action, String actionStatus) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.creationDate = creationDate;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public BigDecimal getRoleId() {
        return roleId;
    }

    public void setRoleId(BigDecimal roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Collection<UfsUserRoleMap> getUfsUserRoleMapCollection() {
        return ufsUserRoleMapCollection;
    }

    public void setUfsUserRoleMapCollection(Collection<UfsUserRoleMap> ufsUserRoleMapCollection) {
        this.ufsUserRoleMapCollection = ufsUserRoleMapCollection;
    }

    public Collection<UfsRolePermissionMap> getUfsRolePermissionMapCollection() {
        return ufsRolePermissionMapCollection;
    }

    public void setUfsRolePermissionMapCollection(Collection<UfsRolePermissionMap> ufsRolePermissionMapCollection) {
        this.ufsRolePermissionMapCollection = ufsRolePermissionMapCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsUserRole)) {
            return false;
        }
        UfsUserRole other = (UfsUserRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsUserRole[ roleId=" + roleId + " ]";
    }
    
}
