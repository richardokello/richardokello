/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ENTITY_PERMISSION")
@NamedQueries({
    @NamedQuery(name = "UfsEntityPermission.findAll", query = "SELECT u FROM UfsEntityPermission u")})
public class UfsEntityPermission implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ENTITY_PERMISSION_ID")
    private BigDecimal entityPermissionId;
    @Basic(optional = false)
    @Column(name = "PERMISSION")
    private String permission;
    @Basic(optional = false)
    @Column(name = "CAPTION")
    private String caption;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @JoinColumn(name = "ENTITY", referencedColumnName = "ENTITY_ID")
    @ManyToOne(optional = false)
    private UfsEntity entity;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permissionId")
    private Collection<UfsRolePermissionMap> ufsRolePermissionMapCollection;

    public UfsEntityPermission() {
    }

    public UfsEntityPermission(BigDecimal entityPermissionId) {
        this.entityPermissionId = entityPermissionId;
    }

    public UfsEntityPermission(BigDecimal entityPermissionId, String permission, String caption) {
        this.entityPermissionId = entityPermissionId;
        this.permission = permission;
        this.caption = caption;
    }

    public BigDecimal getEntityPermissionId() {
        return entityPermissionId;
    }

    public void setEntityPermissionId(BigDecimal entityPermissionId) {
        this.entityPermissionId = entityPermissionId;
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

    public UfsEntity getEntity() {
        return entity;
    }

    public void setEntity(UfsEntity entity) {
        this.entity = entity;
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
        hash += (entityPermissionId != null ? entityPermissionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsEntityPermission)) {
            return false;
        }
        UfsEntityPermission other = (UfsEntityPermission) object;
        if ((this.entityPermissionId == null && other.entityPermissionId != null) || (this.entityPermissionId != null && !this.entityPermissionId.equals(other.entityPermissionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsEntityPermission[ entityPermissionId=" + entityPermissionId + " ]";
    }
    
}
