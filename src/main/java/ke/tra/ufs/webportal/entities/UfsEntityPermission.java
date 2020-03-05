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
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_ENTITY_PERMISSION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsEntityPermission.findAll", query = "SELECT u FROM UfsEntityPermission u")
    , @NamedQuery(name = "UfsEntityPermission.findByEntityPermissionId", query = "SELECT u FROM UfsEntityPermission u WHERE u.entityPermissionId = :entityPermissionId")
    , @NamedQuery(name = "UfsEntityPermission.findByPermission", query = "SELECT u FROM UfsEntityPermission u WHERE u.permission = :permission")
    , @NamedQuery(name = "UfsEntityPermission.findByCaption", query = "SELECT u FROM UfsEntityPermission u WHERE u.caption = :caption")
    , @NamedQuery(name = "UfsEntityPermission.findByAction", query = "SELECT u FROM UfsEntityPermission u WHERE u.action = :action")
    , @NamedQuery(name = "UfsEntityPermission.findByActionStatus", query = "SELECT u FROM UfsEntityPermission u WHERE u.actionStatus = :actionStatus")})
public class UfsEntityPermission implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "PERMISSION")
    private String permission;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "CAPTION")
    private String caption;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENTITY_PERMISSION_ID")
    private Short entityPermissionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permission")
    private List<UfsRolePermission> ufsRolePermissionList;
    @JoinColumn(name = "ENTITY", referencedColumnName = "ENTITY_ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsEntity entity;

    public UfsEntityPermission() {
    }

    public UfsEntityPermission(Short entityPermissionId) {
        this.entityPermissionId = entityPermissionId;
    }

    public UfsEntityPermission(Short entityPermissionId, String permission, String caption) {
        this.entityPermissionId = entityPermissionId;
        this.permission = permission;
        this.caption = caption;
    }

    public Short getEntityPermissionId() {
        return entityPermissionId;
    }

    public void setEntityPermissionId(Short entityPermissionId) {
        this.entityPermissionId = entityPermissionId;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @XmlTransient
    public List<UfsRolePermission> getUfsRolePermissionList() {
        return ufsRolePermissionList;
    }

    public void setUfsRolePermissionList(List<UfsRolePermission> ufsRolePermissionList) {
        this.ufsRolePermissionList = ufsRolePermissionList;
    }

    public UfsEntity getEntity() {
        return entity;
    }

    public void setEntity(UfsEntity entity) {
        this.entity = entity;
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
        return "ke.tracom.ufs.entities.UfsEntityPermission[ entityPermissionId=" + entityPermissionId + " ]";
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
}
