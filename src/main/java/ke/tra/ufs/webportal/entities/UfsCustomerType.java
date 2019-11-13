/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Unique;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Kenny
 */
@Entity
@Table(name = "UFS_CUSTOMER_TYPE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomerType.findAll", query = "SELECT u FROM UfsCustomerType u"),
        @NamedQuery(name = "UfsCustomerType.findById", query = "SELECT u FROM UfsCustomerType u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomerType.findByName", query = "SELECT u FROM UfsCustomerType u WHERE u.name = :name"),
        @NamedQuery(name = "UfsCustomerType.findByDescription", query = "SELECT u FROM UfsCustomerType u WHERE u.description = :description"),
        @NamedQuery(name = "UfsCustomerType.findByAction", query = "SELECT u FROM UfsCustomerType u WHERE u.action = :action"),
        @NamedQuery(name = "UfsCustomerType.findByActionStatus", query = "SELECT u FROM UfsCustomerType u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsCustomerType.findByCreationDate", query = "SELECT u FROM UfsCustomerType u WHERE u.creationDate = :creationDate"),
        @NamedQuery(name = "UfsCustomerType.findByIntrash", query = "SELECT u FROM UfsCustomerType u WHERE u.intrash = :intrash")})
public class UfsCustomerType implements Serializable {

    @Basic(optional = false)
    @NotNull
    @ModifiableField
    @Unique
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Size(max = 100)
    @ModifiableField
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    


    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_CUSTOMER_TYPE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_TYPE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_TYPE_SEQ")
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "CREATION_DATE", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationUnits tenantId;
    @Column(name = "TENANT_ID")
    @Filter
    private BigDecimal tenantIds;
    @Transient
    private List<Long> ruleIds;

    public UfsCustomerType() {
    }

    public UfsCustomerType(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
    }

    public List<Long> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<Long> ruleIds) {
        this.ruleIds = ruleIds;
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
        if (!(object instanceof UfsCustomerType)) {
            return false;
        }
        UfsCustomerType other = (UfsCustomerType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerType[ id=" + id + " ]";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }


}
