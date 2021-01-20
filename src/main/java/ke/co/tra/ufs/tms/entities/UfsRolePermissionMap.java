/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Cornelius M
 */
@Entity
@Table(name = "UFS_ROLE_PERMISSION_MAP")
@NamedQueries({
    @NamedQuery(name = "UfsRolePermissionMap.findAll", query = "SELECT u FROM UfsRolePermissionMap u")
    , @NamedQuery(name = "UfsRolePermissionMap.findByRolePermMapId", query = "SELECT u FROM UfsRolePermissionMap u WHERE u.rolePermMapId = :rolePermMapId")
    , @NamedQuery(name = "UfsRolePermissionMap.findByIntrash", query = "SELECT u FROM UfsRolePermissionMap u WHERE u.intrash = :intrash")})
public class UfsRolePermissionMap implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "UFS_ROLE_PERMISSION_MAP_SEQ", sequenceName = "UFS_ROLE_PERMISSION_MAP_SEQ")
    @GeneratedValue(generator = "UFS_ROLE_PERMISSION_MAP_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLE_PERM_MAP_ID")//
    private BigDecimal rolePermMapId;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ENTITY_PERMISSION_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    private UfsEntityPermission permission;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)    
    private UfsUserRole roleId;

    public UfsRolePermissionMap() {
    }

    public UfsRolePermissionMap(BigDecimal rolePermMapId) {
        this.rolePermMapId = rolePermMapId;
    }

    public UfsRolePermissionMap(UfsEntityPermission permission, UfsUserRole roleId) {
        this.permission = permission;
        this.roleId = roleId;
    }

    public UfsRolePermissionMap(UfsEntityPermission permission, UfsUserRole roleId, String intrash) {
        this.permission = permission;
        this.roleId = roleId;
        this.intrash = intrash;
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

    public UfsEntityPermission getPermission() {
        return permission;
    }

    public void setPermission(UfsEntityPermission permission) {
        this.permission = permission;
    }

    public UfsUserRole getRoleId() {
        return roleId;
    }

    public void setRoleId(UfsUserRole roleId) {
        this.roleId = roleId;
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
        return "ke.co.tra.ufs.tms.entities.UfsRolePermissionMap[ rolePermMapId=" + rolePermMapId + " ]";
    }

}
