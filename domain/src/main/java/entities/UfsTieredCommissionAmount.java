/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_TIERED_COMMISSION_AMOUNT")
@NamedQueries({
    @NamedQuery(name = "UfsTieredCommissionAmount.findAll", query = "SELECT u FROM UfsTieredCommissionAmount u")})
public class UfsTieredCommissionAmount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "LIMIT_TYPE")
    private String limitType;
    @Basic(optional = false)
    @Column(name = "LOWER_LIMIT")
    private long lowerLimit;
    @Basic(optional = false)
    @Column(name = "UPPER_LIMIT")
    private long upperLimit;
    @Basic(optional = false)
    @Column(name = "TIERED_VALUE")
    private long tieredValue;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;

    public UfsTieredCommissionAmount() {
    }

    public UfsTieredCommissionAmount(Long id) {
        this.id = id;
    }

    public UfsTieredCommissionAmount(Long id, long lowerLimit, long upperLimit, long tieredValue, Date creationDate) {
        this.id = id;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.tieredValue = tieredValue;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public long getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public long getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(long upperLimit) {
        this.upperLimit = upperLimit;
    }

    public long getTieredValue() {
        return tieredValue;
    }

    public void setTieredValue(long tieredValue) {
        this.tieredValue = tieredValue;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsTieredCommissionAmount)) {
            return false;
        }
        UfsTieredCommissionAmount other = (UfsTieredCommissionAmount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsTieredCommissionAmount[ id=" + id + " ]";
    }
    
}
