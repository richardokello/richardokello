/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_ENTITY_PERMISSION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsEntityPermission.findAll", query = "SELECT u FROM UfsEntityPermission u")
    , @NamedQuery(name = "UfsEntityPermission.findByPermissionId", query = "SELECT u FROM UfsEntityPermission u WHERE u.permissionId = :permissionId")
    , @NamedQuery(name = "UfsEntityPermission.findByPermission", query = "SELECT u FROM UfsEntityPermission u WHERE u.permission = :permission")
    , @NamedQuery(name = "UfsEntityPermission.findByCaption", query = "SELECT u FROM UfsEntityPermission u WHERE u.caption = :caption")})
public class UfsEntityPermission implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENTITY_PERMISSION_ID")
    private BigDecimal permissionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PERMISSION")
    private String permission;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CAPTION")
    private String caption;
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID")
    @ManyToOne(optional = false)
    private UfsEntity entityId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permission", fetch = FetchType.EAGER)
    private Set<UfsRolePermissionMap> ufsRolePermissionMapSet;

    public UfsEntityPermission() {
    }

    public UfsEntityPermission(BigDecimal permissionId) {
        this.permissionId = permissionId;
    }

    public UfsEntityPermission(BigDecimal permissionId, String permission, String caption) {
        this.permissionId = permissionId;
        this.permission = permission;
        this.caption = caption;
    }

    public BigDecimal getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(BigDecimal permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UfsEntity getEntityId() {
        return entityId;
    }

    public void setEntityId(UfsEntity entityId) {
        this.entityId = entityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permissionId != null ? permissionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsEntityPermission)) {
            return false;
        }
        UfsEntityPermission other = (UfsEntityPermission) object;
        if ((this.permissionId == null && other.permissionId != null) || (this.permissionId != null && !this.permissionId.equals(other.permissionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tra.ufs.tms.entities.UfsEntityPermission[ permissionId=" + permissionId + " ]";
    }

    public Set<UfsRolePermissionMap> getUfsRolePermissionMapSet() {
        return ufsRolePermissionMapSet;
    }

    public void setUfsRolePermissionMapSet(Set<UfsRolePermissionMap> ufsRolePermissionMapSet) {
        this.ufsRolePermissionMapSet = ufsRolePermissionMapSet;
    }

}
