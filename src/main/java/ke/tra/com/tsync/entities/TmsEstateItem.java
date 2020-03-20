/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_ESTATE_ITEM")
@NamedQueries({
    @NamedQuery(name = "TmsEstateItem.findAll", query = "SELECT t FROM TmsEstateItem t")})
public class TmsEstateItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "UNIT_ITEM_ID")
    private BigDecimal unitItemId;
    @Basic(optional = false)
    @Column(name = "UNIT_ID")
    private BigInteger unitId;
    @Column(name = "PARENT_ID")
    private BigInteger parentId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "IS_PARENT")
    private BigInteger isParent;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "estateId")
    private Collection<TmsDevice> tmsDeviceCollection;

    public TmsEstateItem() {
    }

    public TmsEstateItem(BigDecimal unitItemId) {
        this.unitItemId = unitItemId;
    }

    public TmsEstateItem(BigDecimal unitItemId, BigInteger unitId, String name, String description, String status, BigInteger isParent) {
        this.unitItemId = unitItemId;
        this.unitId = unitId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.isParent = isParent;
    }

    public BigDecimal getUnitItemId() {
        return unitItemId;
    }

    public void setUnitItemId(BigDecimal unitItemId) {
        this.unitItemId = unitItemId;
    }

    public BigInteger getUnitId() {
        return unitId;
    }

    public void setUnitId(BigInteger unitId) {
        this.unitId = unitId;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigInteger getIsParent() {
        return isParent;
    }

    public void setIsParent(BigInteger isParent) {
        this.isParent = isParent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitItemId != null ? unitItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsEstateItem)) {
            return false;
        }
        TmsEstateItem other = (TmsEstateItem) object;
        if ((this.unitItemId == null && other.unitItemId != null) || (this.unitItemId != null && !this.unitItemId.equals(other.unitItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsEstateItem[ unitItemId=" + unitItemId + " ]";
    }
    
}
