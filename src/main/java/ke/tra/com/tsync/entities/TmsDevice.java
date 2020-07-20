/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "TMS_DEVICE")
@NamedQueries({
    @NamedQuery(name = "TmsDevice.findAll", query = "SELECT t FROM TmsDevice t")})
public class TmsDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DEVICE_ID")
    private BigDecimal deviceId;
    @Basic(optional = false)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "PART_NUMBER")
    private String partNumber;
    @Column(name = "DEVICE_OUTLET_NAME")
    private String deviceOutletName;
    @Column(name = "DEVICE_OWNER_NAME")
    private String deviceOwnerName;
    @Column(name = "CUSTOMER_OWNER_NAME")
    private String customerOwnerName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceSimcard> tmsDeviceSimcardCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceStatus> tmsDeviceStatusCollection;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @JoinColumn(name = "GEOGRAPH_REG_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsGeographicalRegion geographRegId;
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;

    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsCustomerOutlet outletId;
    @Column(name = "OUTLET_ID")
    private BigDecimal outletIds;

    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomer customerId;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankRegion bankRegionId;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    private TmsEstateItem estateId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceMids> tmsDeviceMidsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceLevelParent> tmsDeviceLevelParentCollection;
    @OneToMany(mappedBy = "tmsDeviceId")
    private Collection<TmsUpdateLogs> tmsUpdateLogsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceTask> tmsDeviceTaskCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceTids> tmsDeviceTidsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceId")
    private Collection<TmsDeviceCurrency> tmsDeviceCurrencyCollection;

    public TmsDevice() {
    }

    public TmsDevice(BigDecimal deviceId) {
        this.deviceId = deviceId;
    }

    public TmsDevice(BigDecimal deviceId, String serialNo) {
        this.deviceId = deviceId;
        this.serialNo = serialNo;
    }

    public BigDecimal getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(BigDecimal deviceId) {
        this.deviceId = deviceId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getDeviceOutletName() {
        return deviceOutletName;
    }

    public void setDeviceOutletName(String deviceOutletName) {
        this.deviceOutletName = deviceOutletName;
    }

    public String getDeviceOwnerName() {
        return deviceOwnerName;
    }

    public void setDeviceOwnerName(String deviceOwnerName) {
        this.deviceOwnerName = deviceOwnerName;
    }

    public String getCustomerOwnerName() {
        return customerOwnerName;
    }

    public void setCustomerOwnerName(String customerOwnerName) {
        this.customerOwnerName = customerOwnerName;
    }

    public Collection<TmsDeviceSimcard> getTmsDeviceSimcardCollection() {
        return tmsDeviceSimcardCollection;
    }

    public void setTmsDeviceSimcardCollection(Collection<TmsDeviceSimcard> tmsDeviceSimcardCollection) {
        this.tmsDeviceSimcardCollection = tmsDeviceSimcardCollection;
    }

    public Collection<TmsDeviceStatus> getTmsDeviceStatusCollection() {
        return tmsDeviceStatusCollection;
    }

    public void setTmsDeviceStatusCollection(Collection<TmsDeviceStatus> tmsDeviceStatusCollection) {
        this.tmsDeviceStatusCollection = tmsDeviceStatusCollection;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public UfsGeographicalRegion getGeographRegId() {
        return geographRegId;
    }

    public void setGeographRegId(UfsGeographicalRegion geographRegId) {
        this.geographRegId = geographRegId;
    }

    public UfsDeviceModel getModelId() {
        return modelId;
    }

    public void setModelId(UfsDeviceModel modelId) {
        this.modelId = modelId;
    }

    public UfsCustomerOutlet getOutletId() {
        return outletId;
    }

    public void setOutletId(UfsCustomerOutlet outletId) {
        this.outletId = outletId;
    }

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public UfsBankRegion getBankRegionId() {
        return bankRegionId;
    }

    public void setBankRegionId(UfsBankRegion bankRegionId) {
        this.bankRegionId = bankRegionId;
    }

    public UfsBankBranches getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(UfsBankBranches bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public TmsEstateItem getEstateId() {
        return estateId;
    }

    public void setEstateId(TmsEstateItem estateId) {
        this.estateId = estateId;
    }

    public Collection<TmsDeviceMids> getTmsDeviceMidsCollection() {
        return tmsDeviceMidsCollection;
    }

    public BigDecimal getOutletIds() {
        return outletIds;
    }

    public void setOutletIds(BigDecimal outletIds) {
        this.outletIds = outletIds;
    }

    public void setTmsDeviceMidsCollection(Collection<TmsDeviceMids> tmsDeviceMidsCollection) {
        this.tmsDeviceMidsCollection = tmsDeviceMidsCollection;
    }

    public Collection<TmsDeviceLevelParent> getTmsDeviceLevelParentCollection() {
        return tmsDeviceLevelParentCollection;
    }

    public void setTmsDeviceLevelParentCollection(Collection<TmsDeviceLevelParent> tmsDeviceLevelParentCollection) {
        this.tmsDeviceLevelParentCollection = tmsDeviceLevelParentCollection;
    }

    public Collection<TmsUpdateLogs> getTmsUpdateLogsCollection() {
        return tmsUpdateLogsCollection;
    }

    public void setTmsUpdateLogsCollection(Collection<TmsUpdateLogs> tmsUpdateLogsCollection) {
        this.tmsUpdateLogsCollection = tmsUpdateLogsCollection;
    }

    public Collection<TmsDeviceTask> getTmsDeviceTaskCollection() {
        return tmsDeviceTaskCollection;
    }

    public void setTmsDeviceTaskCollection(Collection<TmsDeviceTask> tmsDeviceTaskCollection) {
        this.tmsDeviceTaskCollection = tmsDeviceTaskCollection;
    }

    public Collection<TmsDeviceTids> getTmsDeviceTidsCollection() {
        return tmsDeviceTidsCollection;
    }

    public void setTmsDeviceTidsCollection(Collection<TmsDeviceTids> tmsDeviceTidsCollection) {
        this.tmsDeviceTidsCollection = tmsDeviceTidsCollection;
    }

    public Collection<TmsDeviceCurrency> getTmsDeviceCurrencyCollection() {
        return tmsDeviceCurrencyCollection;
    }

    public void setTmsDeviceCurrencyCollection(Collection<TmsDeviceCurrency> tmsDeviceCurrencyCollection) {
        this.tmsDeviceCurrencyCollection = tmsDeviceCurrencyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deviceId != null ? deviceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmsDevice)) {
            return false;
        }
        TmsDevice other = (TmsDevice) object;
        if ((this.deviceId == null && other.deviceId != null) || (this.deviceId != null && !this.deviceId.equals(other.deviceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.TmsDevice[ deviceId=" + deviceId + " ]";
    }
    
}
