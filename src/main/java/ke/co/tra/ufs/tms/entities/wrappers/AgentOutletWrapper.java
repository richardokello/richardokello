/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author ASUS
 */
public class AgentOutletWrapper {
    private BigDecimal agentOutletId;

    private String name;

    private String email;

    private String phoneNumber;

    private String contactPerson;

    private String agentOutletCode;

    private BigInteger payUserId;

    private BigInteger approved;

    private Date approveDate;

    private BigInteger approvedBy;

    private BigInteger deleted;

    private Date deleteDate;

    private BigInteger deletedBy;

    private Date editDate;

    private BigInteger editedBy;

    private String address;

    private BigInteger flagDelete;

    private BigInteger deleteApproved;

    private BigInteger deleteRejected;

    private BigInteger flagDeleteBy;

    private BigInteger deleteApprovedBy;

    private BigInteger deleteRejectedBy;

    private String refNo;

    private Double commissionRate;

    private String accountNumber;

    private String actionReason;

    private BigInteger transactionLimit;

    private BigInteger dailyLimit;

    private BigInteger floatLimit;

    private String location;

    public AgentOutletWrapper() {
    }

    public AgentOutletWrapper(BigDecimal agentOutletId) {
        this.agentOutletId = agentOutletId;
    }

    public AgentOutletWrapper(BigDecimal agentOutletId, String name, String agentOutletCode) {
        this.agentOutletId = agentOutletId;
        this.name = name;
        this.agentOutletCode = agentOutletCode;
    }

    public BigDecimal getAgentOutletId() {
        return agentOutletId;
    }

    public void setAgentOutletId(BigDecimal agentOutletId) {
        this.agentOutletId = agentOutletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAgentOutletCode() {
        return agentOutletCode;
    }

    public void setAgentOutletCode(String agentOutletCode) {
        this.agentOutletCode = agentOutletCode;
    }

    public BigInteger getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(BigInteger payUserId) {
        this.payUserId = payUserId;
    }

    public BigInteger getApproved() {
        return approved;
    }

    public void setApproved(BigInteger approved) {
        this.approved = approved;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public BigInteger getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(BigInteger approvedBy) {
        this.approvedBy = approvedBy;
    }

    public BigInteger getDeleted() {
        return deleted;
    }

    public void setDeleted(BigInteger deleted) {
        this.deleted = deleted;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public BigInteger getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(BigInteger deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public BigInteger getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(BigInteger editedBy) {
        this.editedBy = editedBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getFlagDelete() {
        return flagDelete;
    }

    public void setFlagDelete(BigInteger flagDelete) {
        this.flagDelete = flagDelete;
    }

    public BigInteger getDeleteApproved() {
        return deleteApproved;
    }

    public void setDeleteApproved(BigInteger deleteApproved) {
        this.deleteApproved = deleteApproved;
    }

    public BigInteger getDeleteRejected() {
        return deleteRejected;
    }

    public void setDeleteRejected(BigInteger deleteRejected) {
        this.deleteRejected = deleteRejected;
    }

    public BigInteger getFlagDeleteBy() {
        return flagDeleteBy;
    }

    public void setFlagDeleteBy(BigInteger flagDeleteBy) {
        this.flagDeleteBy = flagDeleteBy;
    }

    public BigInteger getDeleteApprovedBy() {
        return deleteApprovedBy;
    }

    public void setDeleteApprovedBy(BigInteger deleteApprovedBy) {
        this.deleteApprovedBy = deleteApprovedBy;
    }

    public BigInteger getDeleteRejectedBy() {
        return deleteRejectedBy;
    }

    public void setDeleteRejectedBy(BigInteger deleteRejectedBy) {
        this.deleteRejectedBy = deleteRejectedBy;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getActionReason() {
        return actionReason;
    }

    public void setActionReason(String actionReason) {
        this.actionReason = actionReason;
    }

    public BigInteger getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigInteger transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public BigInteger getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigInteger dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public BigInteger getFloatLimit() {
        return floatLimit;
    }

    public void setFloatLimit(BigInteger floatLimit) {
        this.floatLimit = floatLimit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (agentOutletId != null ? agentOutletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgentOutletWrapper)) {
            return false;
        }
        AgentOutletWrapper other = (AgentOutletWrapper) object;
        if ((this.agentOutletId == null && other.agentOutletId != null) || (this.agentOutletId != null && !this.agentOutletId.equals(other.agentOutletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ufs.tidservice.tidfetch.entities.AgentOutletWrapper[ agentOutletId=" + agentOutletId + " ]";
    }

}
