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
@Table(name = "CUSTOMER_OWNERS_CRIME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerOwnersCrime.findAll", query = "SELECT c FROM CustomerOwnersCrime c"),
    @NamedQuery(name = "CustomerOwnersCrime.findById", query = "SELECT c FROM CustomerOwnersCrime c WHERE c.id = :id"),
    @NamedQuery(name = "CustomerOwnersCrime.findByDescription", query = "SELECT c FROM CustomerOwnersCrime c WHERE c.description = :description"),
    @NamedQuery(name = "CustomerOwnersCrime.findByIntrash", query = "SELECT c FROM CustomerOwnersCrime c WHERE c.intrash = :intrash"),
    @NamedQuery(name = "CustomerOwnersCrime.findByCreatedAt", query = "SELECT c FROM CustomerOwnersCrime c WHERE c.createdAt = :createdAt")})
public class CustomerOwnersCrime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
        @GenericGenerator(
            name = "CUSTOMER_OWNERS_CRIME_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_OWNERS_CRIME_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "CUSTOMER_OWNERS_CRIME_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "CUSTOMER_OWNER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomerOwners customerOwnerId;
    @Column(name = "CUSTOMER_OWNER_ID")
    private BigDecimal customerOwnerIds;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomer customerId;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerIds;


    public CustomerOwnersCrime() {
    }

    public CustomerOwnersCrime(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public UfsCustomerOwners getCustomerOwnerId() {
        return customerOwnerId;
    }

    public void setCustomerOwnerId(UfsCustomerOwners customerOwnerId) {
        this.customerOwnerId = customerOwnerId;
    }

    public BigDecimal getCustomerOwnerIds() {
        return customerOwnerIds;
    }

    public void setCustomerOwnerIds(BigDecimal customerOwnerIds) {
        this.customerOwnerIds = customerOwnerIds;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerOwnersCrime)) {
            return false;
        }
        CustomerOwnersCrime other = (CustomerOwnersCrime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tra.ufs.webportal.entities.CustomerOwnersCrime[ id=" + id + " ]";
    }
    
}
