/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tracom9
 */
@Entity
@Table(name = "TMS_ESTATE_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmsEstateItem.findAll", query = "SELECT t FROM TmsEstateItem t")
    , @NamedQuery(name = "TmsEstateItem.findByUnitItemId", query = "SELECT t FROM TmsEstateItem t WHERE t.unitItemId = :unitItemId")
    , @NamedQuery(name = "TmsEstateItem.findByUnitId", query = "SELECT t FROM TmsEstateItem t WHERE t.unitId = :unitId")
    , @NamedQuery(name = "TmsEstateItem.findByParentId", query = "SELECT t FROM TmsEstateItem t WHERE t.parentId = :parentId")
    , @NamedQuery(name = "TmsEstateItem.findByName", query = "SELECT t FROM TmsEstateItem t WHERE t.name = :name")
    , @NamedQuery(name = "TmsEstateItem.findByDescription", query = "SELECT t FROM TmsEstateItem t WHERE t.description = :description")
    , @NamedQuery(name = "TmsEstateItem.findByStatus", query = "SELECT t FROM TmsEstateItem t WHERE t.status = :status")
    , @NamedQuery(name = "TmsEstateItem.findByIsParent", query = "SELECT t FROM TmsEstateItem t WHERE t.isParent = :isParent")
    , @NamedQuery(name = "TmsEstateItem.findByCreationDate", query = "SELECT t FROM TmsEstateItem t WHERE t.creationDate = :creationDate")
    , @NamedQuery(name = "TmsEstateItem.findByAction", query = "SELECT t FROM TmsEstateItem t WHERE t.action = :action")
    , @NamedQuery(name = "TmsEstateItem.findByActionStatus", query = "SELECT t FROM TmsEstateItem t WHERE t.actionStatus = :actionStatus")
    , @NamedQuery(name = "TmsEstateItem.findByIntrash", query = "SELECT t FROM TmsEstateItem t WHERE t.intrash = :intrash")})

public class TmsEstateItem implements Serializable {

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "unitItemId")
    private List<TmsVoidedDevice> tmsVoidedDeviceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<TmsDeviceLevelParent> tmsDeviceLevelParentList;
    @JoinColumn(name = "UNIT_ID", referencedColumnName = "UNIT_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private TmsEstateHierarchy unitId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER)
    private List<TmsEstateItem> tmsEstateItemList;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private TmsEstateItem parentId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unitItemId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<TmsScheduleEstate> tmsScheduleEstateList;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "estateId")
    private List<TmsDevice> tmsDeviceList;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "TMS_BUSINESS_UNIT_ITEM_SEQ", sequenceName = "TMS_BUSINESS_UNIT_ITEM_SEQ")
    @GeneratedValue(generator = "TMS_BUSINESS_UNIT_ITEM_SEQ")
    @Basic(optional = false)
    @Column(name = "UNIT_ITEM_ID")//
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
    @Size(min = 1, max = 10)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
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
    @Transient
    private List<TmsEstateItem> children;
    @Transient
    private TmsEstateHierarchy unitSource;
    @Transient
    private String text;
    @Transient
    private BigDecimal id;

    public TmsEstateItem() {
    }

    public TmsEstateItem(BigDecimal unitItemId) {
        this.unitItemId = unitItemId;
    }

    public TmsEstateItem(BigDecimal unitItemId, TmsEstateHierarchy unitId, TmsEstateItem parentId, String name, String description, String status, BigInteger isParent) {
        this.unitItemId = unitItemId;
        this.unitId = unitId;
        this.parentId = parentId;
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
        return "TmsEstateItem{" + "tmsVoidedDeviceList=" + tmsVoidedDeviceList + ", tmsDeviceLevelParentList=" + tmsDeviceLevelParentList + ", unitId=" + unitId + ", tmsEstateItemList=" + tmsEstateItemList + ", parentId=" + parentId + ", tmsScheduleEstateList=" + tmsScheduleEstateList + ", tmsDeviceList=" + tmsDeviceList + ", unitItemId=" + unitItemId + ", name=" + name + ", description=" + description + ", status=" + status + ", isParent=" + isParent + ", creationDate=" + creationDate + ", action=" + action + ", actionStatus=" + actionStatus + ", intrash=" + intrash + ", children=" + children + ", unitSource=" + unitSource + '}';
    }

    

    @XmlTransient
    @JsonIgnore
    public List<TmsVoidedDevice> getTmsVoidedDeviceList() {
        return tmsVoidedDeviceList;
    }

    public void setTmsVoidedDeviceList(List<TmsVoidedDevice> tmsVoidedDeviceList) {
        this.tmsVoidedDeviceList = tmsVoidedDeviceList;
    }

    @XmlTransient
    @JsonIgnore
    public List<TmsDeviceLevelParent> getTmsDeviceLevelParentList() {
        return tmsDeviceLevelParentList;
    }

    public void setTmsDeviceLevelParentList(List<TmsDeviceLevelParent> tmsDeviceLevelParentList) {
        this.tmsDeviceLevelParentList = tmsDeviceLevelParentList;
    }

    public TmsEstateHierarchy getUnitId() {
        return unitId;
    }

    public void setUnitId(TmsEstateHierarchy unitId) {
        this.unitId = unitId;
    }

    @XmlTransient
    public List<TmsEstateItem> getTmsEstateItemList() {
        return tmsEstateItemList;
    }

    public void setTmsEstateItemList(List<TmsEstateItem> tmsEstateItemList) {
        this.tmsEstateItemList = tmsEstateItemList;
    }

    
    public String getText() {
        return name;
    }

    
    public void setText(String text) {
        this.text = text;
    }

   
    public BigDecimal getId() {
        return unitItemId;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }
    
    
    
    

    public List<TmsEstateItem> getChildren() {
        return tmsEstateItemList;
    }

    public TmsEstateItem getParentId() {
        return parentId;
    }

    public void setParentId(TmsEstateItem parentId) {
        this.parentId = parentId;
    }

    @XmlTransient
    @JsonIgnore
    public List<TmsScheduleEstate> getTmsScheduleEstateList() {
        return tmsScheduleEstateList;
    }

    public void setTmsScheduleEstateList(List<TmsScheduleEstate> tmsScheduleEstateList) {
        this.tmsScheduleEstateList = tmsScheduleEstateList;
    }

    @XmlTransient
    @JsonIgnore
    public List<TmsDevice> getTmsDeviceList() {
        return tmsDeviceList;
    }

    public void setTmsDeviceList(List<TmsDevice> tmsDeviceList) {
        this.tmsDeviceList = tmsDeviceList;
    }

    public TmsEstateHierarchy getUnitSource() {
        return unitId;
    }

}
