/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.tra.com.tsync.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mwagiru Kamoni
 */
@Entity
@Table(name = "CUSTOMER_OWNERS_CRIME")
@NamedQueries({
    @NamedQuery(name = "CustomerOwnersCrime.findAll", query = "SELECT c FROM CustomerOwnersCrime c")})
public class CustomerOwnersCrime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "CUSTOMER_OWNER_ID")
    private long customerOwnerId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "INTRASH")
    private String intrash;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "CUSTOMER_ID")
    private long customerId;

    public CustomerOwnersCrime() {
    }

    public CustomerOwnersCrime(Long id) {
        this.id = id;
    }

    public CustomerOwnersCrime(Long id, long customerOwnerId, long customerId) {
        this.id = id;
        this.customerOwnerId = customerOwnerId;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCustomerOwnerId() {
        return customerOwnerId;
    }

    public void setCustomerOwnerId(long customerOwnerId) {
        this.customerOwnerId = customerOwnerId;
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

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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
        return "com.mycompany.oracleufs.CustomerOwnersCrime[ id=" + id + " ]";
    }
    
}
