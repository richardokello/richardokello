/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "UFS_CUSTOMER_OUTLET")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsCustomerOutlet.findAll", query = "SELECT u FROM UfsCustomerOutlet u"),
        @NamedQuery(name = "UfsCustomerOutlet.findById", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomerOutlet.findByOutletId", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.id = :id"),
        @NamedQuery(name = "UfsCustomerOutlet.findByOutletName", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.outletName = :outletName"),
        @NamedQuery(name = "UfsCustomerOutlet.findByOutletCode", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.outletCode = :outletCode"),
        @NamedQuery(name = "UfsCustomerOutlet.findByContactPerson", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.contactPerson = :contactPerson"),
        @NamedQuery(name = "UfsCustomerOutlet.findByIdNumber", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.idNumber = :idNumber"),
        @NamedQuery(name = "UfsCustomerOutlet.findByPhoneNumber", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.phoneNumber = :phoneNumber"),
        @NamedQuery(name = "UfsCustomerOutlet.findByAssistantRole", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.assistantRole = :assistantRole"),
        @NamedQuery(name = "UfsCustomerOutlet.findByCreatedAt", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.createdAt = :createdAt"),
        @NamedQuery(name = "UfsCustomerOutlet.findByAction", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.action = :action"),
        @NamedQuery(name = "UfsCustomerOutlet.findByActionStatus", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.actionStatus = :actionStatus"),
        @NamedQuery(name = "UfsCustomerOutlet.findByIntrash", query = "SELECT u FROM UfsCustomerOutlet u WHERE u.intrash = :intrash")})
public class UfsCustomerOutlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "CUSTOMER_OUTLET_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_OUTLET_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "CUSTOMER_OUTLET_SEQ")

    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "OUTLET_NAME")
    private String outletName;
    @Size(max = 15)
    @Column(name = "OUTLET_CODE")
    private String outletCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;
    @Size(max = 20)
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @Size(max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ASSISTANT_ROLE")
    private String assistantRole;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Filter
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "BANK_BRANCH_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsBankBranches bankBranchId;
    @Column(name = "BANK_BRANCH_ID")
    private BigDecimal bankBranchIds;
    @JoinColumn(name = "BANK_REGION_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsBankRegion bankRegionId;
    @Column(name = "BANK_REGION_ID")
    private BigDecimal bankRegionIds;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomer customerId;
    @Column(name = "CUSTOMER_ID")
    @Filter
    private BigDecimal customerIds;
    @JoinColumn(name = "GEOGRAPHICAL_REGION_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private UfsGeographicalRegion geographicalRegionId;
    @Column(name = "GEOGRAPHICAL_REGION_ID")
    private BigDecimal geographicalRegionIds;
    @JsonIgnore
    @OneToMany(mappedBy = "outletId")
    private Set<TmsDevice> tmsDeviceList;

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

    public UfsGeographicalRegion getGeographicalRegionId() {
        return geographicalRegionId;
    }

    public void setGeographicalRegionId(UfsGeographicalRegion geographicalRegionId) {
        this.geographicalRegionId = geographicalRegionId;
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

    public BigDecimal getGeographicalRegionIds() {
        return geographicalRegionIds;
    }

    public void setGeographicalRegionIds(BigDecimal geographicalRegionIds) {
        this.geographicalRegionIds = geographicalRegionIds;
    }

    public Set<TmsDevice> getTmsDeviceList() {
        return tmsDeviceList;
    }

    public void setTmsDeviceList(Set<TmsDevice> tmsDeviceList) {
        this.tmsDeviceList = tmsDeviceList;
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
        return "ke.tra.com.tsync.entities.entities.UfsCustomerOutlet[ id=" + id + " ]";
    }

}
