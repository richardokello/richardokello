/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author cotuoma
 */
@Entity
@Table(name = "TMS_DEVICE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TmsDevice.findAll", query = "SELECT t FROM TmsDevice t"),
        @NamedQuery(name = "TmsDevice.findByDeviceId", query = "SELECT t FROM TmsDevice t WHERE t.deviceId = :deviceId"),
        @NamedQuery(name = "TmsDevice.findBySerialNo", query = "SELECT t FROM TmsDevice t WHERE t.serialNo = :serialNo"),
        @NamedQuery(name = "TmsDevice.findByStatus", query = "SELECT t FROM TmsDevice t WHERE t.status = :status"),
        @NamedQuery(name = "TmsDevice.findByCreationDate", query = "SELECT t FROM TmsDevice t WHERE t.creationDate = :creationDate"),
        @NamedQuery(name = "TmsDevice.findByAction", query = "SELECT t FROM TmsDevice t WHERE t.action = :action"),
        @NamedQuery(name = "TmsDevice.findByActionStatus", query = "SELECT t FROM TmsDevice t WHERE t.actionStatus = :actionStatus"),
        @NamedQuery(name = "TmsDevice.findByIntrash", query = "SELECT t FROM TmsDevice t WHERE t.intrash = :intrash"),
        @NamedQuery(name = "TmsDevice.findByPartNumber", query = "SELECT t FROM TmsDevice t WHERE t.partNumber = :partNumber"),
        @NamedQuery(name = "TmsDevice.findByDeviceOutletName", query = "SELECT t FROM TmsDevice t WHERE t.deviceOutletName = :deviceOutletName"),
        @NamedQuery(name = "TmsDevice.findByCustomerOwnerName", query = "SELECT t FROM TmsDevice t WHERE t.customerOwnerName = :customerOwnerName"),
        @NamedQuery(name = "TmsDevice.findByImeiNo", query = "SELECT t FROM TmsDevice t WHERE t.imeiNo = :imeiNo")})
public class TmsDevice implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GenericGenerator(
            name = "TMS_DEVICE_SEQ2",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TMS_DEVICE_SEQ2")
                    ,
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0")
                    ,
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "TMS_DEVICE_SEQ2")
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEVICE_ID")
    private BigDecimal deviceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Size(max = 10)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 5)
    @Column(name = "INTRASH")
    private String intrash;
    @Size(max = 50)
    @Column(name = "PART_NUMBER")
    private String partNumber;
    @Size(max = 100)
    @Column(name = "DEVICE_OUTLET_NAME")
    private String deviceOutletName;
    @Size(max = 30)
    @Column(name = "CUSTOMER_OWNER_NAME")
    private String customerOwnerName;
    @Size(max = 50)
    @Column(name = "IMEI_NO")
    private String imeiNo;
    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID")
    @ManyToOne
    private TmsEstateItem estateId;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBankBranches bankBranchId;
    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerOutlet outletId;

    @Column(name = "OUTLET_ID")
    private BigDecimal outletIds;

    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID")
    @ManyToOne
    private UfsDeviceModel modelId;
    @JoinColumn(name = "DEVICE_TYPE", referencedColumnName = "DEVICE_TYPE_ID")
    @ManyToOne
    private UfsDeviceType deviceType;
    @JoinColumn(name = "GEOGRAPH_REG_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsGeographicalRegion geographRegId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "deviceId")
    private Collection<UfsPosUser> ufsPosUserCollection;

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

    public String getCustomerOwnerName() {
        return customerOwnerName;
    }

    public void setCustomerOwnerName(String customerOwnerName) {
        this.customerOwnerName = customerOwnerName;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
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

    public UfsDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(UfsDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public UfsGeographicalRegion getGeographRegId() {
        return geographRegId;
    }

    public void setGeographRegId(UfsGeographicalRegion geographRegId) {
        this.geographRegId = geographRegId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsPosUser> getUfsPosUserCollection() {
        return ufsPosUserCollection;
    }

    public void setUfsPosUserCollection(Collection<UfsPosUser> ufsPosUserCollection) {
        this.ufsPosUserCollection = ufsPosUserCollection;
    }

    public BigDecimal getOutletIds() {
        return outletIds;
    }

    public void setOutletIds(BigDecimal outletIds) {
        this.outletIds = outletIds;
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
        return "TmsDevice[ deviceId=" + deviceId + " ]";
    }

}
