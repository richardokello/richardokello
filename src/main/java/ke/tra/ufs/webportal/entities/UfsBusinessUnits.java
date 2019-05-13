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
import java.math.BigDecimal;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_BUSINESS_UNITS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsBusinessUnits.findAll", query = "SELECT u FROM UfsBusinessUnits u")
    , @NamedQuery(name = "UfsBusinessUnits.findById", query = "SELECT u FROM UfsBusinessUnits u WHERE u.id = :id")
    , @NamedQuery(name = "UfsBusinessUnits.findByUnitName", query = "SELECT u FROM UfsBusinessUnits u WHERE u.unitName = :unitName")
    , @NamedQuery(name = "UfsBusinessUnits.findByDescription", query = "SELECT u FROM UfsBusinessUnits u WHERE u.description = :description")
    , @NamedQuery(name = "UfsBusinessUnits.findByStatus", query = "SELECT u FROM UfsBusinessUnits u WHERE u.status = :status")
    , @NamedQuery(name = "UfsBusinessUnits.findByAction", query = "SELECT u FROM UfsBusinessUnits u WHERE u.action = :action")
    , @NamedQuery(name = "UfsBusinessUnits.findByActionStatus", query = "SELECT u FROM UfsBusinessUnits u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsBusinessUnits.findByIntrash", query = "SELECT u FROM UfsBusinessUnits u WHERE u.intrash = :intrash")})
public class UfsBusinessUnits implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "UNIT_NAME")
    private String unitName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;

    public UfsBusinessUnits() {
    }

    public UfsBusinessUnits(BigDecimal id) {
        this.id = id;
    }

    public UfsBusinessUnits(BigDecimal id, String unitName, String description) {
        this.id = id;
        this.unitName = unitName;
        this.description = description;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsBusinessUnits)) {
            return false;
        }
        UfsBusinessUnits other = (UfsBusinessUnits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsBusinessUnits[ id=" + id + " ]";
    }
    
}
