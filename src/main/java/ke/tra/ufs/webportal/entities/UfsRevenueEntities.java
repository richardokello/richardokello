/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_REVENUE_ENTITIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsRevenueEntities.findAll", query = "SELECT u FROM UfsRevenueEntities u")
    , @NamedQuery(name = "UfsRevenueEntities.findById", query = "SELECT u FROM UfsRevenueEntities u WHERE u.id = :id")
    , @NamedQuery(name = "UfsRevenueEntities.findByName", query = "SELECT u FROM UfsRevenueEntities u WHERE u.name = :name")
    , @NamedQuery(name = "UfsRevenueEntities.findByDescription", query = "SELECT u FROM UfsRevenueEntities u WHERE u.description = :description")
    , @NamedQuery(name = "UfsRevenueEntities.findByAction", query = "SELECT u FROM UfsRevenueEntities u WHERE u.action = :action")
    , @NamedQuery(name = "UfsRevenueEntities.findByActionStatus", query = "SELECT u FROM UfsRevenueEntities u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsRevenueEntities.findByCreationDate", query = "SELECT u FROM UfsRevenueEntities u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsRevenueEntities.findByIntrash", query = "SELECT u FROM UfsRevenueEntities u WHERE u.intrash = :intrash")})
public class UfsRevenueEntities implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "REVENUE_ENTITIES_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "REVENUE_ENTITIES_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "REVENUE_ENTITIES_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsOrganizationUnits tenantId;

    @Column(name = "TENANT_ID")
    private BigDecimal tenantIds;

    public UfsRevenueEntities() {
    }

    public UfsRevenueEntities(Long id) {
        this.id = id;
    }

    public UfsRevenueEntities(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
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
        if (!(object instanceof UfsRevenueEntities)) {
            return false;
        }
        UfsRevenueEntities other = (UfsRevenueEntities) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsRevenueEntities[ id=" + id + " ]";
    }
    
}
