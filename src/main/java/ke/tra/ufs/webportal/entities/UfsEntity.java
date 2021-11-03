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
@Table(name = "UFS_ENTITY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsEntity.findAll", query = "SELECT u FROM UfsEntity u")
    , @NamedQuery(name = "UfsEntity.findByEntityId", query = "SELECT u FROM UfsEntity u WHERE u.entityId = :entityId")
    , @NamedQuery(name = "UfsEntity.findByEntityName", query = "SELECT u FROM UfsEntity u WHERE u.entityName = :entityName")
    , @NamedQuery(name = "UfsEntity.findByDescription", query = "SELECT u FROM UfsEntity u WHERE u.description = :description")
    , @NamedQuery(name = "UfsEntity.findByAction", query = "SELECT u FROM UfsEntity u WHERE u.action = :action")
    , @NamedQuery(name = "UfsEntity.findByActionStatus", query = "SELECT u FROM UfsEntity u WHERE u.actionStatus = :actionStatus")})
public class UfsEntity implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
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
    @Column(name = "ENTITY_ID")
    private Short entityId;
    @JoinColumn(name = "MODULE", referencedColumnName = "MODULE_ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsModule module;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private List<UfsEntityPermission> ufsEntityPermissionList;

    public UfsEntity() {
    }

    public UfsEntity(Short entityId) {
        this.entityId = entityId;
    }

    public UfsEntity(Short entityId, String entityName) {
        this.entityId = entityId;
        this.entityName = entityName;
    }

    public Short getEntityId() {
        return entityId;
    }

    public void setEntityId(Short entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public UfsModule getModule() {
        return module;
    }

    public void setModule(UfsModule module) {
        this.module = module;
    }

    @XmlTransient
    public List<UfsEntityPermission> getUfsEntityPermissionList() {
        return ufsEntityPermissionList;
    }

    public void setUfsEntityPermissionList(List<UfsEntityPermission> ufsEntityPermissionList) {
        this.ufsEntityPermissionList = ufsEntityPermissionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entityId != null ? entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsEntity)) {
            return false;
        }
        UfsEntity other = (UfsEntity) object;
        if ((this.entityId == null && other.entityId != null) || (this.entityId != null && !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsEntity[ entityId=" + entityId + " ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
}
