/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mwagiru Kamoni
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
        @NamedQuery(name = "UfsCustomerOwners.findByIntrash", query = "SELECT u FROM UfsCustomerOwners u WHERE u.intrash = :intrash"),
        @NamedQuery(name = "UfsCustomerOwners.findBySecondaryPhone", query = "SELECT u FROM UfsCustomerOwners u WHERE u.secondaryPhone = :secondaryPhone"),
        @NamedQuery(name = "UfsCustomerOwners.findByIdNumber", query = "SELECT u FROM UfsCustomerOwners u WHERE u.idNumber = :idNumber")})
public class UfsCustomerOwners implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Size(max = 20)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Size(max = 15)
    @Column(name = "SECONDARY_PHONE")
    private String secondaryPhone;
    @Size(max = 15)
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @JoinColumn(name = "DESIGNATION", referencedColumnName = "ID")
    @ManyToOne
    private UfsBusinessDesignations designation;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private UfsCustomer customerId;
    @Column(name = "CUSTOMER_ID")
    private Long customerIds;
    @OneToMany(mappedBy = "customerOwnerId")
    private Collection<UfsPosUser> ufsPosUserCollection;

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

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public UfsBusinessDesignations getDesignation() {
        return designation;
    }

    public void setDesignation(UfsBusinessDesignations designation) {
        this.designation = designation;
    }

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(Long customerIds) {
        this.customerIds = customerIds;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UfsPosUser> getUfsPosUserCollection() {
        return ufsPosUserCollection;
    }

    public void setUfsPosUserCollection(Collection<UfsPosUser> ufsPosUserCollection) {
        this.ufsPosUserCollection = ufsPosUserCollection;
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
        return "UfsCustomerOwners[ id=" + id + " ]";
    }

}
