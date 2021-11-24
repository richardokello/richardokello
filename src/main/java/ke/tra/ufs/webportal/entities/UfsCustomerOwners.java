/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_CUSTOMER_OWNERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomerOwners.findAll", query = "SELECT u FROM UfsCustomerOwners u"),
    @NamedQuery(name = "UfsCustomerOwners.findById", query = "SELECT u FROM UfsCustomerOwners u WHERE u.id = :id"),
    @NamedQuery(name = "UfsCustomerOwners.findByCreatedAt", query = "SELECT u FROM UfsCustomerOwners u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UfsCustomerOwners.findByAction", query = "SELECT u FROM UfsCustomerOwners u WHERE u.action = :action"),
    @NamedQuery(name = "UfsCustomerOwners.findByActionStatus", query = "SELECT u FROM UfsCustomerOwners u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsCustomerOwners.findByIntrash", query = "SELECT u FROM UfsCustomerOwners u WHERE u.intrash = :intrash")})
public class UfsCustomerOwners implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
            @GenericGenerator(
            name = "CUSTOMER_OWNERS_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_OWNERS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "CUSTOMER_OWNERS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
//    @NotNull
    @Filter
    @Searchable
    @ModifiableField
//    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String directorName;
    @JoinColumn(name = "DESIGNATION", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne
    private UfsBusinessDesignations designation;
    @Column(name = "DESIGNATION")
    @ModifiableField
    private Long directorDesignationId;
    @Basic(optional = false)
//    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 15)
    @Column(name = "PHONE_NUMBER")
    private String directorPrimaryContactNumber;
    @Basic(optional = false)
//    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 15)
    @Column(name = "ID_NUMBER")
    private String directorIdNumber;
//    @NotNull
    @Filter
    @Searchable
    @ModifiableField
    @Size(min = 1, max = 15)
    @Column(name = "SECONDARY_PHONE")
    private String directorSecondaryContactNumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Filter
    @Searchable
    @ModifiableField
    @Column(name = "EMAIL")
    private String directorEmailAddress;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter(isDateRange = true)
    private Date createdAt;
    @Size(max = 20)
    @Column(name = "ACTION", insertable = false)
    private String action;
    @Size(max = 20)
    @Filter
    @Column(name = "ACTION_STATUS", insertable = false)
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH", insertable = false)
    private String intrash;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne(optional = true)
    private UfsCustomer customerId;

    @Column(name = "CUSTOMER_ID")
    @ModifiableField
    @Filter
    private BigDecimal customerIds;

    @Transient
    private CustomerOwnersCrime ownersCrime;

    @Column(name = "USER_NAME")
    @Filter
    @ModifiableField
    private String userName;

    public UfsCustomerOwners() {
    }

    public UfsCustomerOwners(Long id) {
        this.id = id;
    }

    public UfsCustomerOwners(Long id, String directorName, String directorPrimaryContactNumber) {
        this.id = id;
        this.directorName = directorName;
        this.directorPrimaryContactNumber = directorPrimaryContactNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(BigDecimal customerIds) {
        this.customerIds = customerIds;
    }

    public CustomerOwnersCrime getOwnersCrime() {
        return ownersCrime;
    }

    public void setOwnersCrime(CustomerOwnersCrime ownersCrime) {
        this.ownersCrime = ownersCrime;
    }


    public UfsBusinessDesignations getDesignation() {
        return designation;
    }

    public void setDesignation(UfsBusinessDesignations designation) {
        this.designation = designation;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Long getDirectorDesignationId() {
        return directorDesignationId;
    }

    public void setDirectorDesignationId(Long directorDesignationId) {
        this.directorDesignationId = directorDesignationId;
    }

    public String getDirectorPrimaryContactNumber() {
        return directorPrimaryContactNumber;
    }

    public void setDirectorPrimaryContactNumber(String directorPrimaryContactNumber) {
        this.directorPrimaryContactNumber = directorPrimaryContactNumber;
    }

    public String getDirectorIdNumber() {
        return directorIdNumber;
    }

    public void setDirectorIdNumber(String directorIdNumber) {
        this.directorIdNumber = directorIdNumber;
    }

    public String getDirectorSecondaryContactNumber() {
        return directorSecondaryContactNumber;
    }

    public void setDirectorSecondaryContactNumber(String directorSecondaryContactNumber) {
        this.directorSecondaryContactNumber = directorSecondaryContactNumber;
    }

    public String getDirectorEmailAddress() {
        return directorEmailAddress;
    }

    public void setDirectorEmailAddress(String directorEmailAddress) {
        this.directorEmailAddress = directorEmailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        if (!(object instanceof UfsCustomerOwners)) {
            return false;
        }
        UfsCustomerOwners other = (UfsCustomerOwners) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.UfsCustomerOwners[ id=" + id + " ]";
    }
    
}
