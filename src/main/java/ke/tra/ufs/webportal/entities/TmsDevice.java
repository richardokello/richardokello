package ke.tra.ufs.webportal.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Owori Juma
 */
@Entity
@Table(name = "TMS_DEVICE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TmsDevice.findAll", query = "SELECT t FROM TmsDevice t")
        , @NamedQuery(name = "TmsDevice.findByDeviceId", query = "SELECT t FROM TmsDevice t WHERE t.deviceId = :deviceId")
        , @NamedQuery(name = "TmsDevice.findBySerialNo", query = "SELECT t FROM TmsDevice t WHERE t.serialNo = :serialNo")
        , @NamedQuery(name = "TmsDevice.findByStatus", query = "SELECT t FROM TmsDevice t WHERE t.status = :status")
        , @NamedQuery(name = "TmsDevice.findByCreationDate", query = "SELECT t FROM TmsDevice t WHERE t.creationDate = :creationDate")
        , @NamedQuery(name = "TmsDevice.findByAction", query = "SELECT t FROM TmsDevice t WHERE t.action = :action")
        , @NamedQuery(name = "TmsDevice.findByActionStatus", query = "SELECT t FROM TmsDevice t WHERE t.actionStatus = :actionStatus")
        , @NamedQuery(name = "TmsDevice.findByIntrash", query = "SELECT t FROM TmsDevice t WHERE t.intrash = :intrash")
        , @NamedQuery(name = "TmsDevice.findByPartNumber", query = "SELECT t FROM TmsDevice t WHERE t.partNumber = :partNumber")
        , @NamedQuery(name = "TmsDevice.findByDeviceFieldName", query = "SELECT t FROM TmsDevice t WHERE t.deviceFieldName = :deviceFieldName")
        , @NamedQuery(name = "TmsDevice.findByDeviceOwnerName", query = "SELECT t FROM TmsDevice t WHERE t.deviceOwnerName = :deviceOwnerName")})
public class TmsDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "TMS_DEVICE_SEQ2", sequenceName = "TMS_DEVICE_SEQ2")
    @GeneratedValue(generator = "TMS_DEVICE_SEQ2")
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
    private String deviceFieldName;
    @Column(name = "DEVICE_OWNER_NAME")
    private String deviceOwnerName;

    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    private TmsEstateItem estateId;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsBankRegion bankRegionId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomer customerId;
    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerOutlet outletId;
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsOrganizationUnits tenantId;

    @Column(name = "TENANT_ID")
    private BigDecimal tenantIds;
    @Column(name = "BANK_BRANCH_ID")
    private BigDecimal bankBranchIds;
    @Column(name = "BANK_REGION_ID")
    private BigDecimal bankRegionIds;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerIds;
    @Column(name = "OUTLET_ID")
    private BigDecimal outletIds;

    @JoinColumn(name = "GEOGRAPH_REG_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsGeographicalRegion geographicalRegion;

    @Column(name = "GEOGRAPH_REG_ID")
    private BigDecimal geographicalRegionIds;


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

    public String getDeviceFieldName() {
        return deviceFieldName;
    }

    public void setDeviceFieldName(String deviceFieldName) {
        this.deviceFieldName = deviceFieldName;
    }

    public String getDeviceOwnerName() {
        return deviceOwnerName;
    }

    public void setDeviceOwnerName(String deviceOwnerName) {
        this.deviceOwnerName = deviceOwnerName;
    }

    public TmsEstateItem getEstateId() {
        return estateId;
    }

    public void setEstateId(TmsEstateItem estateId) {
        this.estateId = estateId;
    }

    public UfsBankBranches getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(UfsBankBranches bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public UfsBankRegion getBankRegionId() {
        return bankRegionId;
    }

    public void setBankRegionId(UfsBankRegion bankRegionId) {
        this.bankRegionId = bankRegionId;
    }

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public UfsCustomerOutlet getOutletId() {
        return outletId;
    }

    public void setOutletId(UfsCustomerOutlet outletId) {
        this.outletId = outletId;
    }

    public UfsDeviceModel getModelId() {
        return modelId;
    }

    public void setModelId(UfsDeviceModel modelId) {
        this.modelId = modelId;
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

    public BigDecimal getBankBranchIds() {
        return bankBranchIds;
    }

    public void setBankBranchIds(BigDecimal bankBranchIds) {
        this.bankBranchIds = bankBranchIds;
    }

    public BigDecimal getBankRegionIds() {
        return bankRegionIds;
    }

    public void setBankRegionIds(BigDecimal bankRegionIds) {
        this.bankRegionIds = bankRegionIds;
    }

    public BigDecimal getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(BigDecimal customerIds) {
        this.customerIds = customerIds;
    }

    public BigDecimal getOutletIds() {
        return outletIds;
    }

    public void setOutletIds(BigDecimal outletIds) {
        this.outletIds = outletIds;
    }

    public UfsGeographicalRegion getGeographicalRegion() {
        return geographicalRegion;
    }

    public void setGeographicalRegion(UfsGeographicalRegion geographicalRegion) {
        this.geographicalRegion = geographicalRegion;
    }

    public BigDecimal getGeographicalRegionIds() {
        return geographicalRegionIds;
    }

    public void setGeographicalRegionIds(BigDecimal geographicalRegionIds) {
        this.geographicalRegionIds = geographicalRegionIds;
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
        return "masdemo.entities.TmsDevice[ deviceId=" + deviceId + " ]";
    }

}
