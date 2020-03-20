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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_ENTITY")
@NamedQueries({
    @NamedQuery(name = "UfsEntity.findAll", query = "SELECT u FROM UfsEntity u")})
public class UfsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ENTITY_ID")
    private BigDecimal entityId;
    @Basic(optional = false)
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @Basic(optional = false)
    @Column(name = "MODULE")
    private String module;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private Collection<UfsEntityPermission> ufsEntityPermissionCollection;

    public UfsEntity() {
    }

    public UfsEntity(BigDecimal entityId) {
        this.entityId = entityId;
    }

    public UfsEntity(BigDecimal entityId, String entityName, String module, String action, String actionStatus) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.module = module;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public BigDecimal getEntityId() {
        return entityId;
    }

    public void setEntityId(BigDecimal entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Collection<UfsEntityPermission> getUfsEntityPermissionCollection() {
        return ufsEntityPermissionCollection;
    }

    public void setUfsEntityPermissionCollection(Collection<UfsEntityPermission> ufsEntityPermissionCollection) {
        this.ufsEntityPermissionCollection = ufsEntityPermissionCollection;
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
        return "com.mycompany.oracleufs.UfsEntity[ entityId=" + entityId + " ]";
    }
    
}
