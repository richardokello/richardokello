/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.entities.wrappers;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author ASUS
 */
public class TerminalsWrapper {

    private BigDecimal terminalsId;

    private String terminalNum;

    private String terminalSerial;

    private BigInteger payUserId;

    private BigInteger approved;

    private Date approveDate;

    private BigInteger approvedBy;

    private BigInteger deleted;

    private Date deleteDate;

    private BigInteger deletedBy;

    private Date editDate;

    private BigInteger editedBy;

    private BigInteger flagDelete;

    private BigInteger deleteApproved;

    private BigInteger deleteRejected;

    private BigInteger flagDeleteBy;

    private BigInteger deleteApprovedBy;

    private BigInteger deleteRejectedBy;

    private String updatetime;

    private String imeiNum;

    private String actionReason;

    private BigInteger subCountyId;

    private BigInteger revenueSourceId;

    private BigInteger terminalModelId;

    private BigInteger termId;

    private BigInteger agentOutletId;

    private AgentWrapper agentId;

    @Transient
    private AgentOutletWrapper agentOutlet;

    public TerminalsWrapper() {
    }

    public TerminalsWrapper(BigDecimal terminalsId) {
        this.terminalsId = terminalsId;
    }

    public TerminalsWrapper(BigDecimal terminalsId, String terminalNum) {
        this.terminalsId = terminalsId;
        this.terminalNum = terminalNum;
    }

    public BigDecimal getTerminalsId() {
        return terminalsId;
    }

    public void setTerminalsId(BigDecimal terminalsId) {
        this.terminalsId = terminalsId;
    }

    public String getTerminalNum() {
        return terminalNum;
    }

    public void setTerminalNum(String terminalNum) {
        this.terminalNum = terminalNum;
    }

    public String getTerminalSerial() {
        return terminalSerial;
    }

    public void setTerminalSerial(String terminalSerial) {
        this.terminalSerial = terminalSerial;
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

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getImeiNum() {
        return imeiNum;
    }

    public void setImeiNum(String imeiNum) {
        this.imeiNum = imeiNum;
    }

    public String getActionReason() {
        return actionReason;
    }

    public void setActionReason(String actionReason) {
        this.actionReason = actionReason;
    }

    public BigInteger getSubCountyId() {
        return subCountyId;
    }

    public void setSubCountyId(BigInteger subCountyId) {
        this.subCountyId = subCountyId;
    }

    public BigInteger getRevenueSourceId() {
        return revenueSourceId;
    }

    public void setRevenueSourceId(BigInteger revenueSourceId) {
        this.revenueSourceId = revenueSourceId;
    }

    public BigInteger getTerminalModelId() {
        return terminalModelId;
    }

    public void setTerminalModelId(BigInteger terminalModelId) {
        this.terminalModelId = terminalModelId;
    }

    public BigInteger getTermId() {
        return termId;
    }

    public void setTermId(BigInteger termId) {
        this.termId = termId;
    }

    public BigInteger getAgentOutletId() {
        return agentOutletId;
    }

    public void setAgentOutletId(BigInteger agentOutletId) {
        this.agentOutletId = agentOutletId;
    }

    public AgentWrapper getAgentId() {
        return agentId;
    }

    public void setAgentId(AgentWrapper agentId) {
        this.agentId = agentId;
    }

    public AgentOutletWrapper getAgentOutlet() {
        return agentOutlet;
    }

    public void setAgentOutlet(AgentOutletWrapper agentOutlet) {
        this.agentOutlet = agentOutlet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (terminalsId != null ? terminalsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TerminalsWrapper)) {
            return false;
        }
        TerminalsWrapper other = (TerminalsWrapper) object;
        if ((this.terminalsId == null && other.terminalsId != null) || (this.terminalsId != null && !this.terminalsId.equals(other.terminalsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ufs.tidservice.tidfetch.entities.TerminalsWrapper[ terminalsId=" + terminalsId + " ]";
    }

}
