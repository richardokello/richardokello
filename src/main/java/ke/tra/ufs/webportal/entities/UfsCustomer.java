/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_CUSTOMER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomer.findAll", query = "SELECT u FROM UfsCustomer u"),
    @NamedQuery(name = "UfsCustomer.findById", query = "SELECT u FROM UfsCustomer u WHERE u.id = :id"),
    @NamedQuery(name = "UfsCustomer.findByAccountNumber", query = "SELECT u FROM UfsCustomer u WHERE u.accountNumber = :accountNumber"),
    @NamedQuery(name = "UfsCustomer.findByPin", query = "SELECT u FROM UfsCustomer u WHERE u.pin = :pin"),
    @NamedQuery(name = "UfsCustomer.findByLocalRegNumber", query = "SELECT u FROM UfsCustomer u WHERE u.localRegNumber = :localRegNumber"),
    @NamedQuery(name = "UfsCustomer.findByDateIssued", query = "SELECT u FROM UfsCustomer u WHERE u.dateIssued = :dateIssued"),
    @NamedQuery(name = "UfsCustomer.findByValidTo", query = "SELECT u FROM UfsCustomer u WHERE u.validTo = :validTo"),
    @NamedQuery(name = "UfsCustomer.findByAddress", query = "SELECT u FROM UfsCustomer u WHERE u.address = :address"),
    @NamedQuery(name = "UfsCustomer.findByPhonenumber", query = "SELECT u FROM UfsCustomer u WHERE u.phonenumber = :phonenumber"),
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Size(max = 20)
    @Column(name = "PIN")
    private String pin;
    @Size(max = 30)
    @Column(name = "LOCAL_REG_NUMBER")
    private String localRegNumber;
    @Column(name = "DATE_ISSUED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIssued;
    @Column(name = "VALID_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;
    @Size(max = 15)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 15)
    @Column(name = "PHONENUMBER")
    private String phonenumber;
    @Column(name = "CREATED_AT",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Size(max = 20)
    @Column(name = "BUSINESS_LICENCE_NUMBER")
    private String businessLicenceNumber;
    @JoinColumn(name = "CLASS_TYPE_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne
    private UfsCustomerClass classTypeId;
    @Column(name = "CLASS_TYPE_ID")
    private BigDecimal classTypeIds;
    @JoinColumn(name = "GEOGRAPHICAL_REG_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne
    private UfsGeographicalRegion geographicalRegId;
    @Column(name = "GEOGRAPHICAL_REG_ID")
    private BigDecimal geographicalRegIds;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @Column(name = "TENANT_ID")
    private BigDecimal tenantIds;

    public UfsCustomer() {
    }

    public UfsCustomer(Long id) {
        this.id = id;
    }

    public UfsCustomer(Long id, String accountNumber) {
        this.id = id;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getLocalRegNumber() {
        return localRegNumber;
    }

    public void setLocalRegNumber(String localRegNumber) {
        this.localRegNumber = localRegNumber;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public UfsCustomerClass getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(UfsCustomerClass classTypeId) {
        this.classTypeId = classTypeId;
    }

    public UfsGeographicalRegion getGeographicalRegId() {
        return geographicalRegId;
    }

    public void setGeographicalRegId(UfsGeographicalRegion geographicalRegId) {
        this.geographicalRegId = geographicalRegId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public BigDecimal getClassTypeIds() {
        return classTypeIds;
    }

    public void setClassTypeIds(BigDecimal classTypeIds) {
        this.classTypeIds = classTypeIds;
    }

    public BigDecimal getGeographicalRegIds() {
        return geographicalRegIds;
    }

    public void setGeographicalRegIds(BigDecimal geographicalRegIds) {
        this.geographicalRegIds = geographicalRegIds;
    }

    public BigDecimal getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(BigDecimal tenantIds) {
        this.tenantIds = tenantIds;
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
    
}
