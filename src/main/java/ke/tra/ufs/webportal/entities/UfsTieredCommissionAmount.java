/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ojuma
 */
@Entity
@Table(name = "UFS_TIERED_COMMISSION_AMOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsTieredCommissionAmount.findAll", query = "SELECT u FROM UfsTieredCommissionAmount u")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findById", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.id = :id")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByLimitType", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.limitType = :limitType")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByLowerLimit", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.lowerLimit = :lowerLimit")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByUpperLimit", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.upperLimit = :upperLimit")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByTieredValue", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.tieredValue = :tieredValue")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByAction", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.action = :action")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByActionStatus", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByCreationDate", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsTieredCommissionAmount.findByIntrash", query = "SELECT u FROM UfsTieredCommissionAmount u WHERE u.intrash = :intrash")})
public class UfsTieredCommissionAmount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "LIMIT_TYPE")
    private String limitType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOWER_LIMIT")
    private long lowerLimit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPPER_LIMIT")
    private long upperLimit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIERED_VALUE")
    private long tieredValue;
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
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;

    public UfsTieredCommissionAmount() {
    }

    public UfsTieredCommissionAmount(Long id) {
        this.id = id;
    }

    public UfsTieredCommissionAmount(Long id, long lowerLimit, long upperLimit, long tieredValue) {
        this.id = id;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.tieredValue = tieredValue;
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
        return "ke.tra.ufs.webportal.entities.UfsTieredCommissionAmount[ id=" + id + " ]";
    }
    
}
