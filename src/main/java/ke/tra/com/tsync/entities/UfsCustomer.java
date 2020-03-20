/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
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
@Table(name = "UFS_CUSTOMER")
@NamedQueries({
    @NamedQuery(name = "UfsCustomer.findAll", query = "SELECT u FROM UfsCustomer u")})
public class UfsCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "LOCAL_REG_NUMBER")
    private String localRegNumber;
    @Column(name = "DATE_ISSUED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIssued;
    @Column(name = "VALID_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PHONENUMBER")
    private String phonenumber;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "BUSINESS_LICENCE_NUMBER")
    private String businessLicenceNumber;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "TERMINATION_REASON")
    private String terminationReason;
    @Column(name = "TERMINATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date terminationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<FieldQuestionsCustomers> fieldQuestionsCustomersCollection;
    @OneToMany(mappedBy = "customerId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(mappedBy = "customerId")
    private Collection<UfsCustomerOwners> ufsCustomerOwnersCollection;
    @OneToMany(mappedBy = "customerId")
    private Collection<FieldTickets> fieldTicketsCollection;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsOrganizationUnits tenantId;
    @JoinColumn(name = "GEOGRAPHICAL_REG_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsGeographicalRegion geographicalRegId;
    @JoinColumn(name = "CLASS_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomerClass classTypeId;
    @OneToMany(mappedBy = "customerId")
    private Collection<UfsCustomerOutlet> ufsCustomerOutletCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<UfsCustomerHistory> ufsCustomerHistoryCollection;
    @OneToMany(mappedBy = "customerId")
    private Collection<FieldQuestionsFeedback> fieldQuestionsFeedbackCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<UfsCustomerTransfer> ufsCustomerTransferCollection;

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

    public Collection<FieldQuestionsCustomers> getFieldQuestionsCustomersCollection() {
        return fieldQuestionsCustomersCollection;
    }

    public void setFieldQuestionsCustomersCollection(Collection<FieldQuestionsCustomers> fieldQuestionsCustomersCollection) {
        this.fieldQuestionsCustomersCollection = fieldQuestionsCustomersCollection;
    }

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    public Collection<UfsCustomerOwners> getUfsCustomerOwnersCollection() {
        return ufsCustomerOwnersCollection;
    }

    public void setUfsCustomerOwnersCollection(Collection<UfsCustomerOwners> ufsCustomerOwnersCollection) {
        this.ufsCustomerOwnersCollection = ufsCustomerOwnersCollection;
    }

    public Collection<FieldTickets> getFieldTicketsCollection() {
        return fieldTicketsCollection;
    }

    public void setFieldTicketsCollection(Collection<FieldTickets> fieldTicketsCollection) {
        this.fieldTicketsCollection = fieldTicketsCollection;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public UfsGeographicalRegion getGeographicalRegId() {
        return geographicalRegId;
    }

    public void setGeographicalRegId(UfsGeographicalRegion geographicalRegId) {
        this.geographicalRegId = geographicalRegId;
    }

    public UfsCustomerClass getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(UfsCustomerClass classTypeId) {
        this.classTypeId = classTypeId;
    }

    public Collection<UfsCustomerOutlet> getUfsCustomerOutletCollection() {
        return ufsCustomerOutletCollection;
    }

    public void setUfsCustomerOutletCollection(Collection<UfsCustomerOutlet> ufsCustomerOutletCollection) {
        this.ufsCustomerOutletCollection = ufsCustomerOutletCollection;
    }

    public Collection<UfsCustomerHistory> getUfsCustomerHistoryCollection() {
        return ufsCustomerHistoryCollection;
    }

    public void setUfsCustomerHistoryCollection(Collection<UfsCustomerHistory> ufsCustomerHistoryCollection) {
        this.ufsCustomerHistoryCollection = ufsCustomerHistoryCollection;
    }

    public Collection<FieldQuestionsFeedback> getFieldQuestionsFeedbackCollection() {
        return fieldQuestionsFeedbackCollection;
    }

    public void setFieldQuestionsFeedbackCollection(Collection<FieldQuestionsFeedback> fieldQuestionsFeedbackCollection) {
        this.fieldQuestionsFeedbackCollection = fieldQuestionsFeedbackCollection;
    }

    public Collection<UfsCustomerTransfer> getUfsCustomerTransferCollection() {
        return ufsCustomerTransferCollection;
    }

    public void setUfsCustomerTransferCollection(Collection<UfsCustomerTransfer> ufsCustomerTransferCollection) {
        this.ufsCustomerTransferCollection = ufsCustomerTransferCollection;
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
        return "com.mycompany.oracleufs.UfsCustomer[ id=" + id + " ]";
    }
    
}
