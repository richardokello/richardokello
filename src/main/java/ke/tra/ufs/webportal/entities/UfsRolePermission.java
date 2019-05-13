/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_ROLE_PERMISSION", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsRolePermission.findAll", query = "SELECT u FROM UfsRolePermission u")
    , @NamedQuery(name = "UfsRolePermission.findByRolePermId", query = "SELECT u FROM UfsRolePermission u WHERE u.rolePermId = :rolePermId")
    , @NamedQuery(name = "UfsRolePermission.findByCreationDate", query = "SELECT u FROM UfsRolePermission u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsRolePermission.findByIntrash", query = "SELECT u FROM UfsRolePermission u WHERE u.intrash = :intrash")})
public class UfsRolePermission implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLE_PERM_ID")
    private Long rolePermId;
    @JoinColumn(name = "PERMISSION", referencedColumnName = "ENTITY_PERMISSION_ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsEntityPermission permission;
    @JoinColumn(name = "ROLE", referencedColumnName = "ROLE_ID")
    @ManyToOne(optional = false)
    private UfsRole role;

    public UfsRolePermission() {
    }

    public UfsRolePermission(Long rolePermId) {
        this.rolePermId = rolePermId;
    }

    public UfsRolePermission(Long rolePermId, Date creationDate) {
        this.rolePermId = rolePermId;
        this.creationDate = creationDate;
    }
    public UfsRolePermission(UfsEntityPermission permission, UfsRole role, String intrash) {
        this.permission = permission;
        this.role = role;
        this.intrash = intrash;
    }

    public Long getRolePermId() {
        return rolePermId;
    }

    public void setRolePermId(Long rolePermId) {
        this.rolePermId = rolePermId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public UfsEntityPermission getPermission() {
        return permission;
    }

    public void setPermission(UfsEntityPermission permission) {
        this.permission = permission;
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
        return "ke.tracom.ufs.entities.UfsRolePermission[ rolePermId=" + rolePermId + " ]";
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }
    
}
