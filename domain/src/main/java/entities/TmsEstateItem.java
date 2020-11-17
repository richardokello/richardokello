/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_ESTATE_ITEM")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TmsEstateItem.findAll", query = "SELECT t FROM TmsEstateItem t"),
        @NamedQuery(name = "TmsEstateItem.findByUnitItemId", query = "SELECT t FROM TmsEstateItem t WHERE t.unitItemId = :unitItemId"),
        @NamedQuery(name = "TmsEstateItem.findByName", query = "SELECT t FROM TmsEstateItem t WHERE t.name = :name"),
        @NamedQuery(name = "TmsEstateItem.findByDescription", query = "SELECT t FROM TmsEstateItem t WHERE t.description = :description"),
        @NamedQuery(name = "TmsEstateItem.findByStatus", query = "SELECT t FROM TmsEstateItem t WHERE t.status = :status"),
        @NamedQuery(name = "TmsEstateItem.findByIsParent", query = "SELECT t FROM TmsEstateItem t WHERE t.isParent = :isParent"),
        @NamedQuery(name = "TmsEstateItem.findByCreationDate", query = "SELECT t FROM TmsEstateItem t WHERE t.creationDate = :creationDate"),
        @NamedQuery(name = "TmsEstateItem.findByAction", query = "SELECT t FROM TmsEstateItem t WHERE t.action = :action"),
        @NamedQuery(name = "TmsEstateItem.findByActionStatus", query = "SELECT t FROM TmsEstateItem t WHERE t.actionStatus = :actionStatus"),
        @NamedQuery(name = "TmsEstateItem.findByIntrash", query = "SELECT t FROM TmsEstateItem t WHERE t.intrash = :intrash")})
public class TmsEstateItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_ITEM_ID")
    private BigDecimal unitItemId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_PARENT")
    private BigInteger isParent;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "estateId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @JoinColumn(name = "UNIT_ID", referencedColumnName = "UNIT_ID")
    @ManyToOne(optional = false)
    private TmsEstateHierachy unitId;
    @OneToMany(mappedBy = "parentId")
    private Collection<TmsEstateItem> tmsEstateItemCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    private TmsEstateItem parentId;

    public TmsEstateItem() {
    }

    public TmsEstateItem(BigDecimal unitItemId) {
        this.unitItemId = unitItemId;
    }

    public TmsEstateItem(BigDecimal unitItemId, String name, String description, String status, BigInteger isParent) {
        this.unitItemId = unitItemId;
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

    @XmlTransient
    @com.fasterxml.jackson.annotation.JsonIgnore
    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    public TmsEstateHierachy getUnitId() {
        return unitId;
    }

    public void setUnitId(TmsEstateHierachy unitId) {
        this.unitId = unitId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<TmsEstateItem> getTmsEstateItemCollection() {
        return tmsEstateItemCollection;
    }

    public void setTmsEstateItemCollection(Collection<TmsEstateItem> tmsEstateItemCollection) {
        this.tmsEstateItemCollection = tmsEstateItemCollection;
    }

    public TmsEstateItem getParentId() {
        return parentId;
    }

    public void setParentId(TmsEstateItem parentId) {
        this.parentId = parentId;
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
        return "TmsEstateItem[ unitItemId=" + unitItemId + " ]";
    }

}
