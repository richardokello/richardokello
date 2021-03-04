/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Tracom
 */
@Entity
@Table(name = "UFS_CUSTOMER")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomer.findAll", query = "SELECT u FROM UfsCustomer u"),
        @NamedQuery(name = "UfsCustomer.findById", query = "SELECT u FROM UfsCustomer u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomer.findByCustomerId", query = "SELECT u FROM UfsCustomer u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomer.findByDateIssued", query = "SELECT u FROM UfsCustomer u WHERE u.dateIssued = :dateIssued"),
        @NamedQuery(name = "UfsCustomer.findByValidTo", query = "SELECT u FROM UfsCustomer u WHERE u.validTo = :validTo"),
        @NamedQuery(name = "UfsCustomer.findByCreatedAt", query = "SELECT u FROM UfsCustomer u WHERE u.createdAt = :createdAt"),
        @NamedQuery(name = "UfsCustomer.findByAction", query = "SELECT u FROM UfsCustomer u WHERE u.action = :action"),
        @NamedQuery(name = "UfsCustomer.findByActionStatus", query = "SELECT u FROM UfsCustomer u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsCustomer.findByIntrash", query = "SELECT u FROM UfsCustomer u WHERE u.intrash = :intrash"),
        @NamedQuery(name = "UfsCustomer.findByBusinessLicenceNumber", query = "SELECT u FROM UfsCustomer u WHERE u.businessLicenceNumber = :businessLicenceNumber")})
public class UfsCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_CUSTOMER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "PIN")
    private String pinNumber;
    @Size(max = 30)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "LOCAL_REG_NUMBER")
    private String localRegistrationNumber;
    @ModifiableField
    @Column(name = "DATE_ISSUED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIssued;
    @ModifiableField
    @Column(name = "VALID_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;
    @ModifiableField
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 15)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "PHONENUMBER")
    private String businessPrimaryContactNo;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date createdAt;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
    @Size(max = 20)
    @ModifiableField
    @Column(name = "BUSINESS_LICENCE_NUMBER")
    private String businessLicenceNumber;
    @JoinColumn(name = "CUSTOMER_CLASS", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    //@JsonIgnore
    private UfsCustomerClass customerClass;
    @ModifiableField
    @Column(name = "CUSTOMER_CLASS")
    private BigInteger customerClassId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private UfsOrganizationUnits tenantId;
    @Column(name = "TENANT_ID")
    @ModifiableField
    @Filter
    @Searchable
    private String tenantIds;
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "CUSTOMER_NAME")
    private String businessName;
    @Column(name = "TERMINATION_REASON")
    @Filter
    @Searchable
    private String terminationReason;

    @Column(name = "TERMINATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date terminationDate;

    @JoinColumn(name = "BUSINESS_TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    //@JsonIgnore
    private UfsBusinessType businessTypeId;
    @ModifiableField
    @Column(name = "BUSINESS_TYPE_ID")
    private Long businessTypeIds;
    @Size(max = 15)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "SECONDARY_PHONENUMBER")
    private String businessSecondaryContactNo;
    @Size(max = 50)
    @ModifiableField
    @Column(name = "EMAIL_ADDRESS")
    private String businessEmailAddress;
    @JoinColumn(name = "CUSTOMER_TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomerType customerType;
    @ModifiableField
    @Column(name = "CUSTOMER_TYPE_ID")
    private BigDecimal customerTypeId;
    @JoinColumn(name = "COMMERCIAL_ACTIVITY", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    //@JsonIgnore
    private UfsCommercialActivities commercialActivity;
    @ModifiableField
    @Column(name = "COMMERCIAL_ACTIVITY")
    private Long commercialActivityId;
    @JoinColumn(name = "ESTATE_ID", referencedColumnName = "UNIT_ITEM_ID", insertable = false, updatable = false)
    @ManyToOne
    @JsonIgnore
    private TmsEstateItem estate;
    @Column(name = "ESTATE_ID")
    @ModifiableField
    private BigDecimal estateId;
    @Size(max = 20)
    @Filter
    @Searchable
    @Column(name = "STATUS", insertable = false)
    private String status;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    @Filter
    private String createdBy;
    @Size(max = 40)
    @ModifiableField
    @Filter
    @Searchable
    @Column(name = "MID")
    private String mid;


    @JoinColumn(name = "MCC", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsMcc mccId;
    @ModifiableField
    @Column(name = "MCC")
    private BigDecimal mccIds;

    public UfsCustomer() {
    }

    public UfsCustomer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public String getBusinessLicenceNumber() {
        return businessLicenceNumber;
    }

    public void setBusinessLicenceNumber(String businessLicenceNumber) {
        this.businessLicenceNumber = businessLicenceNumber;
    }

    public UfsCustomerClass getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(UfsCustomerClass customerClass) {
        this.customerClass = customerClass;
    }

    public BigInteger getCustomerClassId() {
        return customerClassId;
    }

    public void setCustomerClassId(BigInteger customerClassId) {
        this.customerClassId = customerClassId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }



    public String getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(String tenantIds) {
        this.tenantIds = tenantIds;
    }


    public String getTerminationReason() {
        return terminationReason;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public UfsBusinessType getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(UfsBusinessType businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public Long getBusinessTypeIds() {
        return businessTypeIds;
    }

    public void setBusinessTypeIds(Long businessTypeIds) {
        this.businessTypeIds = businessTypeIds;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getLocalRegistrationNumber() {
        return localRegistrationNumber;
    }

    public void setLocalRegistrationNumber(String localRegistrationNumber) {
        this.localRegistrationNumber = localRegistrationNumber;
    }

    public String getBusinessPrimaryContactNo() {
        return businessPrimaryContactNo;
    }

    public void setBusinessPrimaryContactNo(String businessPrimaryContactNo) {
        this.businessPrimaryContactNo = businessPrimaryContactNo;
    }


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessSecondaryContactNo() {
        return businessSecondaryContactNo;
    }

    public void setBusinessSecondaryContactNo(String businessSecondaryContactNo) {
        this.businessSecondaryContactNo = businessSecondaryContactNo;
    }

    public String getBusinessEmailAddress() {
        return businessEmailAddress;
    }

    public void setBusinessEmailAddress(String businessEmailAddress) {
        this.businessEmailAddress = businessEmailAddress;
    }

    public UfsCustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(UfsCustomerType customerType) {
        this.customerType = customerType;
    }

    public BigDecimal getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(BigDecimal customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public UfsCommercialActivities getCommercialActivity() {
        return commercialActivity;
    }

    public void setCommercialActivity(UfsCommercialActivities commercialActivity) {
        this.commercialActivity = commercialActivity;
    }

    public Long getCommercialActivityId() {
        return commercialActivityId;
    }

    public void setCommercialActivityId(Long commercialActivityId) {
        this.commercialActivityId = commercialActivityId;
    }

    public TmsEstateItem getEstate() {
        return estate;
    }

    public void setEstate(TmsEstateItem estate) {
        this.estate = estate;
    }

    public BigDecimal getEstateId() {
        return estateId;
    }

    public void setEstateId(BigDecimal estateId) {
        this.estateId = estateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
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
        if (!(object instanceof UfsCustomer)) {
            return false;
        }
        UfsCustomer other = (UfsCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomer[ id=" + id + " ]";
    }

    public UfsMcc getMccId() {
        return mccId;
    }

    public void setMccId(UfsMcc mccId) {
        this.mccId = mccId;
    }

    public BigDecimal getMccIds() {
        return mccIds;
    }

    public void setMccIds(BigDecimal mccIds) {
        this.mccIds = mccIds;
    }
}
