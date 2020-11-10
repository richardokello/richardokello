/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import entities.UfsBankBranches;
import entities.UfsCustomer;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "UFS_CUSTOMER_TRANSFER")
@NamedQueries({
    @NamedQuery(name = "UfsCustomerTransfer.findAll", query = "SELECT u FROM UfsCustomerTransfer u")})
public class UfsCustomerTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "REASON")
    private String reason;
    @Basic(optional = false)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsCustomer customerId;
    @JoinColumn(name = "CURRENT_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankBranches currentBranchId;
    @JoinColumn(name = "DESTINATION_BRANCH_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UfsBankBranches destinationBranchId;

    public UfsCustomerTransfer() {
    }

    public UfsCustomerTransfer(Long id) {
        this.id = id;
    }

    public UfsCustomerTransfer(Long id, String reason, String action, String actionStatus, Date createdAt, String intrash) {
        this.id = id;
        this.reason = reason;
        this.action = action;
        this.actionStatus = actionStatus;
        this.createdAt = createdAt;
        this.intrash = intrash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public UfsBankBranches getCurrentBranchId() {
        return currentBranchId;
    }

    public void setCurrentBranchId(UfsBankBranches currentBranchId) {
        this.currentBranchId = currentBranchId;
    }

    public UfsBankBranches getDestinationBranchId() {
        return destinationBranchId;
    }

    public void setDestinationBranchId(UfsBankBranches destinationBranchId) {
        this.destinationBranchId = destinationBranchId;
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
        if (!(object instanceof UfsCustomerTransfer)) {
            return false;
        }
        UfsCustomerTransfer other = (UfsCustomerTransfer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsCustomerTransfer[ id=" + id + " ]";
    }
    
}
