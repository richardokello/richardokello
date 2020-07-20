/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ROLE")
@NamedQueries({
    @NamedQuery(name = "UfsRole.findAll", query = "SELECT u FROM UfsRole u")})
public class UfsRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ROLE_ID")
    private Long roleId;
    @Basic(optional = false)
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<UfsRolePermission> ufsRolePermissionCollection;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<UfsWorkgroupRole> ufsWorkgroupRoleCollection;

    public UfsRole() {
    }

    public UfsRole(Long roleId) {
        this.roleId = roleId;
    }

    public UfsRole(Long roleId, String roleName, Date creationDate) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.creationDate = creationDate;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
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

    public Collection<UfsRolePermission> getUfsRolePermissionCollection() {
        return ufsRolePermissionCollection;
    }

    public void setUfsRolePermissionCollection(Collection<UfsRolePermission> ufsRolePermissionCollection) {
        this.ufsRolePermissionCollection = ufsRolePermissionCollection;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public Collection<UfsWorkgroupRole> getUfsWorkgroupRoleCollection() {
        return ufsWorkgroupRoleCollection;
    }

    public void setUfsWorkgroupRoleCollection(Collection<UfsWorkgroupRole> ufsWorkgroupRoleCollection) {
        this.ufsWorkgroupRoleCollection = ufsWorkgroupRoleCollection;
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
        if (!(object instanceof UfsRole)) {
            return false;
        }
        UfsRole other = (UfsRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsRole[ roleId=" + roleId + " ]";
    }
    
}
