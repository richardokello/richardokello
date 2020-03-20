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
@Table(name = "UFS_CUSTOMER_OUTLET")
@NamedQueries({
    @NamedQuery(name = "UfsCustomerOutlet.findAll", query = "SELECT u FROM UfsCustomerOutlet u")})
public class UfsCustomerOutlet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "OUTLET_NAME")
    private String outletName;
    @Column(name = "OUTLET_CODE")
    private String outletCode;
    @Basic(optional = false)
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic(optional = false)
    @Column(name = "ASSISTANT_ROLE")
    private String assistantRole;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(mappedBy = "outletId")
    private Collection<TmsDevice> tmsDeviceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UfsPosUser> ufsPosUserCollection;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsGeographicalRegion geographicalRegionId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne
    private UfsCustomer customerId;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankRegion bankRegionId;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankBranches bankBranchId;

    public UfsCustomerOutlet() {
    }

    public UfsCustomerOutlet(Long id) {
        this.id = id;
    }

    public UfsCustomerOutlet(Long id, String outletName, String contactPerson, String assistantRole) {
        this.id = id;
        this.outletName = outletName;
        this.contactPerson = contactPerson;
        this.assistantRole = assistantRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAssistantRole() {
        return assistantRole;
    }

    public void setAssistantRole(String assistantRole) {
        this.assistantRole = assistantRole;
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

    public Collection<TmsDevice> getTmsDeviceCollection() {
        return tmsDeviceCollection;
    }

    public void setTmsDeviceCollection(Collection<TmsDevice> tmsDeviceCollection) {
        this.tmsDeviceCollection = tmsDeviceCollection;
    }

    public Collection<UfsPosUser> getUfsPosUserCollection() {
        return ufsPosUserCollection;
    }

    public void setUfsPosUserCollection(Collection<UfsPosUser> ufsPosUserCollection) {
        this.ufsPosUserCollection = ufsPosUserCollection;
    }

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsCustomerOutlet)) {
            return false;
        }
        UfsCustomerOutlet other = (UfsCustomerOutlet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsCustomerOutlet[ id=" + id + " ]";
    }
    
}
