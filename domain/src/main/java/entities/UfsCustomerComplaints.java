/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
@Table(name = "UFS_CUSTOMER_COMPLAINTS")
@NamedQueries({
    @NamedQuery(name = "UfsCustomerComplaints.findAll", query = "SELECT u FROM UfsCustomerComplaints u")})
public class UfsCustomerComplaints implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "COMPLAINT_NATURE")
    private String complaintNature;
    @Basic(optional = false)
    @Column(name = "COMPLAINTS")
    private String complaints;
    @Basic(optional = false)
    @Column(name = "AGENT_COMPLAINED")
    private String agentComplained;
    @Basic(optional = false)
    @Column(name = "AGENT_PHONENUMBER")
    private String agentPhonenumber;
    @Basic(optional = false)
    @Column(name = "AGENT_LOCATION")
    private String agentLocation;
    @Column(name = "DATE_OF_OCCURENCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfOccurence;
    @Basic(optional = false)
    @Column(name = "REMEDIAL_ACTIONS")
    private String remedialActions;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @JoinColumn(name = "BATCH_ID", referencedColumnName = "BATCH_ID")
    @ManyToOne
    private UfsCustomerComplaintsBatch batchId;

    public UfsCustomerComplaints() {
    }

    public UfsCustomerComplaints(Long id) {
        this.id = id;
    }

    public UfsCustomerComplaints(Long id, String complaintNature, String complaints, String agentComplained, String agentPhonenumber, String agentLocation, String remedialActions) {
        this.id = id;
        this.complaintNature = complaintNature;
        this.complaints = complaints;
        this.agentComplained = agentComplained;
        this.agentPhonenumber = agentPhonenumber;
        this.agentLocation = agentLocation;
        this.remedialActions = remedialActions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintNature() {
        return complaintNature;
    }

    public void setComplaintNature(String complaintNature) {
        this.complaintNature = complaintNature;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getAgentComplained() {
        return agentComplained;
    }

    public void setAgentComplained(String agentComplained) {
        this.agentComplained = agentComplained;
    }

    public String getAgentPhonenumber() {
        return agentPhonenumber;
    }

    public void setAgentPhonenumber(String agentPhonenumber) {
        this.agentPhonenumber = agentPhonenumber;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    public Date getDateOfOccurence() {
        return dateOfOccurence;
    }

    public void setDateOfOccurence(Date dateOfOccurence) {
        this.dateOfOccurence = dateOfOccurence;
    }

    public String getRemedialActions() {
        return remedialActions;
    }

    public void setRemedialActions(String remedialActions) {
        this.remedialActions = remedialActions;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public UfsCustomerComplaintsBatch getBatchId() {
        return batchId;
    }

    public void setBatchId(UfsCustomerComplaintsBatch batchId) {
        this.batchId = batchId;
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
        if (!(object instanceof UfsCustomerComplaints)) {
            return false;
        }
        UfsCustomerComplaints other = (UfsCustomerComplaints) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.oracleufs.UfsCustomerComplaints[ id=" + id + " ]";
    }
    
}
