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
import ke.axle.chassis.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Tracom
 */
@Entity
@Table(name = "UFS_CUSTOMER_TRANSFER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsCustomerTransfer.findAll", query = "SELECT u FROM UfsCustomerTransfer u"),
    @NamedQuery(name = "UfsCustomerTransfer.findById", query = "SELECT u FROM UfsCustomerTransfer u WHERE u.id = :id"),
    @NamedQuery(name = "UfsCustomerTransfer.findByReason", query = "SELECT u FROM UfsCustomerTransfer u WHERE u.reason = :reason"),
    @NamedQuery(name = "UfsCustomerTransfer.findByAction", query = "SELECT u FROM UfsCustomerTransfer u WHERE u.action = :action"),
    @NamedQuery(name = "UfsCustomerTransfer.findByActionStatus", query = "SELECT u FROM UfsCustomerTransfer u WHERE u.actionStatus = :actionStatus"),
    @NamedQuery(name = "UfsCustomerTransfer.findByCreatedAt", query = "SELECT u FROM UfsCustomerTransfer u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UfsCustomerTransfer.findByIntrash", query = "SELECT u FROM UfsCustomerTransfer u WHERE u.intrash = :intrash")})
public class UfsCustomerTransfer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
                @GenericGenerator(
            name = "UFS_CUSTOMER_TRANSFER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_CUSTOMER_TRANSFER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "UFS_CUSTOMER_TRANSFER_SEQ")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "REASON")
    private String reason;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @Filter
    @Size(min = 1, max = 20)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Basic(optional = false)
    @Column(name = "CREATED_AT",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Size(min = 1, max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "CURRENT_BRANCH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBankBranches currentBranchId;
    @Column(name = "CURRENT_BRANCH_ID")
    private BigDecimal currentBranchIds;
    @JoinColumn(name = "DESTINATION_BRANCH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UfsBankBranches destinationBranchId;
    @Column(name = "DESTINATION_BRANCH_ID")
    private BigDecimal destinationBranchIds;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    @ManyToOne(optional = false)
    private UfsCustomer customerId;
    
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerIds;

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

    public UfsCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UfsCustomer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getCurrentBranchIds() {
        return currentBranchIds;
    }

    public void setCurrentBranchIds(BigDecimal currentBranchIds) {
        this.currentBranchIds = currentBranchIds;
    }

    public BigDecimal getDestinationBranchIds() {
        return destinationBranchIds;
    }

    public void setDestinationBranchIds(BigDecimal destinationBranchIds) {
        this.destinationBranchIds = destinationBranchIds;
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
        return "ke.tra.ufs.webportal.entities.UfsCustomerTransfer[ id=" + id + " ]";
    }
    
}
