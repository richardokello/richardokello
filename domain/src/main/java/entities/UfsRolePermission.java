/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ROLE_PERMISSION")
@NamedQueries({
    @NamedQuery(name = "UfsRolePermission.findAll", query = "SELECT u FROM UfsRolePermission u")})
public class UfsRolePermission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ROLE_PERM_ID")
    private Long rolePermId;
    @Basic(optional = false)
    @Column(name = "PERMISSION")
    private short permission;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "ROLE", referencedColumnName = "ROLE_ID")
    @ManyToOne(optional = false)
    private UfsRole role;

    public UfsRolePermission() {
    }

    public UfsRolePermission(Long rolePermId) {
        this.rolePermId = rolePermId;
    }

    public UfsRolePermission(Long rolePermId, short permission, Date creationDate) {
        this.rolePermId = rolePermId;
        this.permission = permission;
        this.creationDate = creationDate;
    }

    public Long getRolePermId() {
        return rolePermId;
    }

    public void setRolePermId(Long rolePermId) {
        this.rolePermId = rolePermId;
    }

    public short getPermission() {
        return permission;
    }

    public void setPermission(short permission) {
        this.permission = permission;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsRole getRole() {
        return role;
    }

    public void setRole(UfsRole role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolePermId != null ? rolePermId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsRolePermission)) {
            return false;
        }
        UfsRolePermission other = (UfsRolePermission) object;
        if ((this.rolePermId == null && other.rolePermId != null) || (this.rolePermId != null && !this.rolePermId.equals(other.rolePermId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsRolePermission[ rolePermId=" + rolePermId + " ]";
    }
    
}
