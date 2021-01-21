/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.entities;

import ke.axle.chassis.annotations.Filter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kenny
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "entityId")
public class UfsEntity implements Serializable {

    private static final long serialVersionUID = 1534162750L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENTITY_ID")
    private Short entityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @JoinColumn(name = "MODULE", referencedColumnName = "MODULE_ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsModule module;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    @Filter
    private String actionStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private Collection<UfsEntityPermission> permissions;


    public UfsEntity() {
    }

    public UfsEntity(Short entityId) {
        this.entityId = entityId;
    }

    public UfsEntity(Short entityId, String entityName, UfsModule module) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.module = module;
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

    public UfsModule getModule() {
        return module;
    }

    public void setModule(UfsModule module) {
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

    public Collection<UfsEntityPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<UfsEntityPermission> permissions) {
        this.permissions = permissions;
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

}
