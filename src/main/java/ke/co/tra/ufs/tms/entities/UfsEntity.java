/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "UFS_ENTITY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsEntity.findAll", query = "SELECT u FROM UfsEntity u")
    , @NamedQuery(name = "UfsEntity.findByEntityId", query = "SELECT u FROM UfsEntity u WHERE u.entityId = :entityId")
    , @NamedQuery(name = "UfsEntity.findByEntityName", query = "SELECT u FROM UfsEntity u WHERE u.entityName = :entityName")
    , @NamedQuery(name = "UfsEntity.findByModule", query = "SELECT u FROM UfsEntity u WHERE u.module = :module")})
public class UfsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENTITY_ID")
    private BigDecimal entityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ENTITY_NAME")
    private String entityName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MODULE")
    private String module; 
    @org.codehaus.jackson.annotate.JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityId", fetch = FetchType.EAGER)
    private List<UfsEntityPermission> ufsEntityPermissionList;

    public UfsEntity() {
    }

    public UfsEntity(BigDecimal entityId) {
        this.entityId = entityId;
    }

    public UfsEntity(BigDecimal entityId, String entityName, String module) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.module = module;
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
        return "ke.co.tra.ufs.tms.entities.UfsEntity[ entityId=" + entityId + " ]";
    }

}
