/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
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
import java.util.Collection;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UFS_CUSTOMER")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomer.findAll", query = "SELECT u FROM UfsCustomer u"),
        @NamedQuery(name = "UfsCustomer.findById", query = "SELECT u FROM UfsCustomer u WHERE u.id = :id"),
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
        @NamedQuery(name = "UfsCustomer.findByBusinessLicenceNumber", query = "SELECT u FROM UfsCustomer u WHERE u.businessLicenceNumber = :businessLicenceNumber"),
        @NamedQuery(name = "UfsCustomer.findByCustomerName", query = "SELECT u FROM UfsCustomer u WHERE u.customerName = :customerName"),
        @NamedQuery(name = "UfsCustomer.findByTerminationReason", query = "SELECT u FROM UfsCustomer u WHERE u.terminationReason = :terminationReason"),
        @NamedQuery(name = "UfsCustomer.findByTerminationDate", query = "SELECT u FROM UfsCustomer u WHERE u.terminationDate = :terminationDate"),
        @NamedQuery(name = "UfsCustomer.findBySecondaryPhonenumber", query = "SELECT u FROM UfsCustomer u WHERE u.secondaryPhonenumber = :secondaryPhonenumber"),
        @NamedQuery(name = "UfsCustomer.findByEmailAddress", query = "SELECT u FROM UfsCustomer u WHERE u.emailAddress = :emailAddress")})
public class UfsCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
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
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
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
    @Size(max = 45)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 15)
    @Column(name = "PHONENUMBER")
    private String phonenumber;
    @Column(name = "CREATED_AT")
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
    @Size(max = 50)
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Size(max = 100)
    @Column(name = "TERMINATION_REASON")
    private String terminationReason;
    @Column(name = "TERMINATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date terminationDate;
    @Size(max = 15)
    @Column(name = "SECONDARY_PHONENUMBER")
    private String secondaryPhonenumber;
    @Size(max = 50)
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
    @OneToMany(mappedBy = "customerId")
    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;
    @OneToMany(mappedBy = "customerId")
    private Collection<UfsCustomerOwners> ufsCustomerOwnersCollection;
    @JoinColumn(name = "BUSINESS_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsBusinessType businessTypeId;

//    @JoinColumn(name = "CLASS_TYPE_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private UfsCustomerClass classTypeId;

    @JoinColumn(name = "CUSTOMER_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomerType customerTypeId;

    @JoinColumn(name = "TENANT_ID", referencedColumnName = "U_UID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getSecondaryPhonenumber() {
        return secondaryPhonenumber;
    }

    public void setSecondaryPhonenumber(String secondaryPhonenumber) {
        this.secondaryPhonenumber = secondaryPhonenumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
        return ufsCustomerOutletCollection;
    }

    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsCustomerOwners> getUfsCustomerOwnersCollection() {
        return ufsCustomerOwnersCollection;
    }

    public void setUfsCustomerOwnersCollection(Collection<UfsCustomerOwners> ufsCustomerOwnersCollection) {
        this.ufsCustomerOwnersCollection = ufsCustomerOwnersCollection;
    }

    public UfsBusinessType getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(UfsBusinessType businessTypeId) {
        this.businessTypeId = businessTypeId;
    }


    public UfsCustomerType getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(UfsCustomerType customerTypeId) {
        this.customerTypeId = customerTypeId;
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
        return "UfsCustomer[ id=" + id + " ]";
    }

}
