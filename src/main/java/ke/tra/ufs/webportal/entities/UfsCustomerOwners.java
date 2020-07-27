/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ke.axle.chassis.annotations.Filter;
import org.codehaus.jackson.annotate.JsonIgnore;
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
    @NamedQuery(name = "UfsCustomerOwners.findByName", query = "SELECT u FROM UfsCustomerOwners u WHERE u.name = :name"),
    @NamedQuery(name = "UfsCustomerOwners.findByPhoneNumber", query = "SELECT u FROM UfsCustomerOwners u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "UfsCustomerOwners.findByEmail", query = "SELECT u FROM UfsCustomerOwners u WHERE u.email = :email"),
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
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "DESIGNATION", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne
    private UfsBusinessDesignations designation;
    @Column(name = "DESIGNATION")
    private Long designationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "SECONDARY_PHONE")
    private String secondary_phone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CREATED_AT",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
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
    @ManyToOne
    private UfsCustomer customerId;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerIds;

    @Filter
    @Column(name = "CUSTOMER_ID",insertable = false,updatable = false)
    private String customerIdsStr;


    @Transient
    private CustomerOwnersCrime ownersCrime;

    public UfsCustomerOwners() {
    }

    public UfsCustomerOwners(Long id) {
        this.id = id;
    }

    public UfsCustomerOwners(Long id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCustomerIdsStr() {
        return customerIdsStr;
    }

    public void setCustomerIdsStr(String customerIdsStr) {
        this.customerIdsStr = customerIdsStr;
    }

    public UfsBusinessDesignations getDesignation() {
        return designation;
    }

    public void setDesignation(UfsBusinessDesignations designation) {
        this.designation = designation;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public String getSecondary_phone() {
        return secondary_phone;
    }

    public void setSecondary_phone(String secondary_phone) {
        this.secondary_phone = secondary_phone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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
