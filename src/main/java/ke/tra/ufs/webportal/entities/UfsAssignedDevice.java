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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_ASSIGNED_DEVICE", catalog = "", schema = "UFS_SMART_SUITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsAssignedDevice.findAll", query = "SELECT u FROM UfsAssignedDevice u"),
    @NamedQuery(name = "UfsAssignedDevice.findById", query = "SELECT u FROM UfsAssignedDevice u WHERE u.id = :id"),
    @NamedQuery(name = "UfsAssignedDevice.findByTerminalId", query = "SELECT u FROM UfsAssignedDevice u WHERE u.terminalId = :terminalId"),
    @NamedQuery(name = "UfsAssignedDevice.findByMerchantId", query = "SELECT u FROM UfsAssignedDevice u WHERE u.merchantId = :merchantId"),
    @NamedQuery(name = "UfsAssignedDevice.findByImei", query = "SELECT u FROM UfsAssignedDevice u WHERE u.imei = :imei"),
    @NamedQuery(name = "UfsAssignedDevice.findBySerialNumber", query = "SELECT u FROM UfsAssignedDevice u WHERE u.serialNumber = :serialNumber"),
    @NamedQuery(name = "UfsAssignedDevice.findByEstateId", query = "SELECT u FROM UfsAssignedDevice u WHERE u.estateId = :estateId"),
    @NamedQuery(name = "UfsAssignedDevice.findByAction", query = "SELECT u FROM UfsAssignedDevice u WHERE u.action = :action"),
    @NamedQuery(name = "UfsAssignedDevice.findByActionStatus", query = "SELECT u FROM UfsAssignedDevice u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsAssignedDevice.findByIntrash", query = "SELECT u FROM UfsAssignedDevice u WHERE u.intrash = :intrash"),
    @NamedQuery(name = "UfsAssignedDevice.findByCreatedAt", query = "SELECT u FROM UfsAssignedDevice u WHERE u.createdAt = :createdAt")})
public class UfsAssignedDevice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
        @GenericGenerator(
            name = "UFS_ASSIGNED_DEVICE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_ASSIGNED_DEVICE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_ASSIGNED_DEVICE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "TERMINAL_ID")
    private String terminalId;
    @Size(max = 30)
    @Column(name = "MERCHANT_ID")
    private String merchantId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "IMEI")
    private String imei;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;
    @Column(name = "ESTATE_ID")
    private Long estateId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Basic(optional = false)
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBankBranches bankBranchId;
    @Column(name = "BANK_BRANCH_ID")
    private BigDecimal bankBranchIds;
    
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBankRegion bankRegionId;
    @Column(name = "BANK_REGION_ID")
    private BigDecimal bankRegionIds;
    
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomer customerId;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerIds;
    
    @JoinColumn(name = "OUTLET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomerOutlet outletId;
    @Column(name = "OUTLET_ID")
    private BigDecimal outletIds;
    
    @JoinColumn(name = "MODEL_ID", referencedColumnName = "MODEL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsDeviceModel modelId;
    
    @Column(name = "MODEL_ID")
    private BigDecimal modelIds;
    
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsGeographicalRegion geographicalRegionId;
    
        
    @Column(name = "GEOGRAPHICAL_REGION_ID")
    private BigDecimal geographicalRegionIds;
    
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsOrganizationUnits tenantId;
    
    @Column(name = "TENANT_ID")
    private BigDecimal tenantIds;
    
    @Transient
    private List<UfsAssignedSimdetails> assignedSimDetails;
    

    public UfsAssignedDevice() {
    }

    public UfsAssignedDevice(Long id) {
        this.id = id;
    }

    public UfsAssignedDevice(Long id, String terminalId, String imei, String serialNumber, String action, String actionStatus, String intrash, Date createdAt) {
        this.id = id;
        this.terminalId = terminalId;
        this.imei = imei;
        this.serialNumber = serialNumber;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getEstateId() {
        return estateId;
    }

    public void setEstateId(Long estateId) {
        this.estateId = estateId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
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

    public BigDecimal getModelIds() {
        return modelIds;
    }

    public void setModelIds(BigDecimal modelIds) {
        this.modelIds = modelIds;
    }

    public BigDecimal getGeographicalRegionIds() {
        return geographicalRegionIds;
    }

    public void setGeographicalRegionIds(BigDecimal geographicalRegionIds) {
        this.geographicalRegionIds = geographicalRegionIds;
    }

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
    }

    public List<UfsAssignedSimdetails> getAssignedSimDetails() {
        return assignedSimDetails;
    }

    public void setAssignedSimDetails(List<UfsAssignedSimdetails> assignedSimDetails) {
        this.assignedSimDetails = assignedSimDetails;
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
        if (!(object instanceof UfsAssignedDevice)) {
            return false;
        }
        UfsAssignedDevice other = (UfsAssignedDevice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsAssignedDevice[ id=" + id + " ]";
    }
    
}
