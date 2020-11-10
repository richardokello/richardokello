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
@Table(name = "UFS_ROLE_PERMISSION_MAP")
@NamedQueries({
    @NamedQuery(name = "UfsRolePermissionMap.findAll", query = "SELECT u FROM UfsRolePermissionMap u")})
public class UfsRolePermissionMap implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ROLE_PERM_MAP_ID")
    private BigDecimal rolePermMapId;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    @ManyToOne(optional = false)
    private UfsUserRole roleId;
    @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ENTITY_PERMISSION_ID")
    @ManyToOne(optional = false)
    private UfsEntityPermission permissionId;

    public UfsRolePermissionMap() {
    }

    public UfsRolePermissionMap(BigDecimal rolePermMapId) {
        this.rolePermMapId = rolePermMapId;
    }

    public BigDecimal getRolePermMapId() {
        return rolePermMapId;
    }

    public void setRolePermMapId(BigDecimal rolePermMapId) {
        this.rolePermMapId = rolePermMapId;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsUserRole getRoleId() {
        return roleId;
    }

    public void setRoleId(UfsUserRole roleId) {
        this.roleId = roleId;
    }

    public UfsEntityPermission getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(UfsEntityPermission permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolePermMapId != null ? rolePermMapId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsRolePermissionMap)) {
            return false;
        }
        UfsRolePermissionMap other = (UfsRolePermissionMap) object;
        if ((this.rolePermMapId == null && other.rolePermMapId != null) || (this.rolePermMapId != null && !this.rolePermMapId.equals(other.rolePermMapId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsRolePermissionMap[ rolePermMapId=" + rolePermMapId + " ]";
    }
    
}
